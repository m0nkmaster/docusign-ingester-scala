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

object Ingester {
  private val logger = LoggerFactory.getLogger(this.getClass)

  implicit val docuSignWebhookEncoder: EntityEncoder[IO, DocuSignWebhook] =
    jsonEncoderOf
  implicit val docuSignWebhookDecoder: EntityDecoder[IO, DocuSignWebhook] =
    jsonOf

  val routes = HttpRoutes.of[IO] {
    case req @ POST -> Root / "ingest" =>
      logger.info("Received POST request to /ingest")
      (for {
        _ <- IO.pure(())
        body <- req.bodyText.compile.string
        _ = logger.info(s"Raw request body length: ${body.length}")
        _ = logger.info(
          s"Document count in payload: ${body.split("PDFBytes").length - 1}"
        )
        _ = logger.info(
          s"Has envelopeDocuments: ${body.contains("envelopeDocuments")}"
        )

        _ = {
          logger.info("=== DOCUSIGN PAYLOAD START ===")
          body.grouped(4000).zipWithIndex.foreach { case (chunk, index) =>
            logger.info(s"CHUNK $index: $chunk")
          }
          logger.info("=== DOCUSIGN PAYLOAD END ===")
        }
        
        webhookData <- req.as[DocuSignWebhook]
        _ = logger.info(
          s"Parsed webhook data - EnvelopeId: ${webhookData.data.envelopeId}"
        )
        pdfBytesBase64 =
          webhookData.data.envelopeSummary.envelopeDocuments.head.PDFBytes
        pdfBytes = java.util.Base64.getDecoder.decode(pdfBytesBase64)
        s3Key = generateS3Key(webhookData.data.envelopeId)
        _ = logger.info(s"Generated S3 key: $s3Key")
        _ <- S3Utils.saveToS3(pdfBytes, s3Key)
      } yield webhookData).attempt.flatMap {
        case Right(data) =>
          logger.info(
            s"Successfully processed request for envelope: ${data.data.envelopeId}"
          )
          Ok(data)
        case Left(error) =>
          logger.error("Error processing request", error)
          InternalServerError(
            Json.obj("error" -> Json.fromString(error.getMessage))
          )
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
