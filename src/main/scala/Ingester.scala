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
          // Get first document's PDF bytes
          webhookData.data.envelopeSummary.envelopeDocuments.headOption match {
            case Some(document) =>
              val pdfBytes = java.util.Base64.getDecoder.decode(document.PDFBytes)
              val s3Key = generateS3Key(webhookData.data.envelopeId)
              logger.info(s"Generated S3 key: $s3Key")

              S3Utils.saveToS3(pdfBytes, s3Key).attempt.flatMap {
                case Right(_) =>
                  logger.info(s"Successfully saved document to S3: $s3Key")
                  Ok(webhookData)
                case Left(error) =>
                  logger.error(s"Failed to save document to S3: ${error.getMessage}")
                  InternalServerError(Json.obj(
                    "error" -> Json.fromString("Failed to save document"),
                    "details" -> Json.fromString(error.getMessage)
                  ))
              }
            
            case None =>
              BadRequest(Json.obj(
                "error" -> Json.fromString("No documents found in envelope")
              ))
          }

        case Left(error) => 
          logger.error(s"Failed to parse webhook data: ${error.getMessage}")
          BadRequest(Json.obj(
            "error" -> Json.fromString("Invalid webhook data format"),
            "details" -> Json.fromString(error.getMessage)
          ))
      }
    }

    case GET -> Root / "health-check" =>
      logger.info("Received GET request to /health-check")
      val healthStatus = Json.obj(
        "serverTime" -> Json.fromString(Instant.now().toString),
        "status" -> Json.fromString("healthy"),
        "version" -> Json.fromString(BuildInfo.version),
        "buildDate" -> Json.fromString(BuildInfo.buildDate),
        "awsRegion" -> Json.fromString(
          sys.env.getOrElse("AWS_REGION", "not set")
        )
      )
      Ok(healthStatus)

  }

  def generateS3Key(field1: String): String = {
    s"$field1-${System.currentTimeMillis}.pdf"
  }
}
