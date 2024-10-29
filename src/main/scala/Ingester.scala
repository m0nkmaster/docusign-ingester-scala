import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import io.circe.generic.auto._
import org.http4s.circe._
import io.circe.syntax._
import io.circe.Json
import java.time.Instant
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import models._
import org.slf4j.LoggerFactory
import fs2.Stream
import io.circe.parser.decode
import fs2.text

object Ingester {
  private val logger = LoggerFactory.getLogger(this.getClass)

  implicit val docuSignWebhookEncoder: EntityEncoder[IO, DocuSignWebhook] =
    jsonEncoderOf
  implicit val docuSignWebhookDecoder: EntityDecoder[IO, DocuSignWebhook] =
    jsonOf

  val routes = HttpRoutes.of[IO] {
    case req @ POST -> Root / "ingest" =>
      logger.info("Received POST request to /ingest")

      req.body
        .through(text.utf8.decode)
        .compile
        .string
        .flatMap { body =>
          decode[DocuSignWebhook](body) match {
            case Right(webhookData) =>
              logger.info(
                s"Successfully parsed webhook data - EnvelopeId: ${webhookData.data.envelopeId}"
              )
              val pdfBytesBase64 =
                webhookData.data.envelopeSummary.envelopeDocuments.head.PDFBytes
              val pdfBytes = java.util.Base64.getDecoder.decode(pdfBytesBase64)
              val s3Key = generateS3Key(webhookData.data.envelopeId)
              logger.info(s"Generated S3 key: $s3Key")

              S3Utils.saveToS3(pdfBytes, s3Key).attempt.flatMap {
                case Right(_) =>
                  logger.info(
                    s"Successfully processed request for envelope: ${webhookData.data.envelopeId}"
                  )
                  Ok(webhookData)
                case Left(error) =>
                  logger.error(
                    s"Error parsing webhook data: ${error.getMessage}",
                    error
                  )
                  logger.error(s"Error details: ${error.getClass.getName}")
                  logger.error(
                    s"Stack trace: ${error.getStackTrace.mkString("\n")}"
                  )
                  BadRequest(
                    Json.obj(
                      "error" -> Json.fromString(error.getMessage),
                      "errorType" -> Json.fromString(error.getClass.getName)
                    )
                  )

              }

            case Left(error) =>
              logger.error("Error parsing webhook data", error)
              BadRequest(Json.obj("error" -> Json.fromString(error.getMessage)))

          }
        }

    case GET -> Root / "health-check" =>
      logger.info("Received GET request to /health-check")
      val healthStatus = Json.obj(
        "serverTime" -> Json.fromString(Instant.now().toString),
        "status" -> Json.fromString("healthy"),
        "version" -> Json.fromString(BuildInfo.version),
        "buildDate" -> Json.fromString(BuildInfo.buildDate)
      )
      Ok(healthStatus)
  }

  def generateS3Key(field1: String): String = {
    s"$field1-${System.currentTimeMillis}.pdf"
  }
}
