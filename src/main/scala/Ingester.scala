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

  val routes = HttpRoutes.of[IO] {
    case req @ POST -> Root / "ingest" =>
      logger.info("Received POST request to /ingest")

      req.body
        .through(text.utf8.decode)
        .compile
        .string
        .flatMap { body =>
          val userNameRegex = """userName":"([^"]+)"""".r
          val envelopeIdRegex = """envelopeId":"([^"]+)"""".r
          val pdfBytesRegex = """PDFBytes":"([^"]+)"""".r

          (for {
            envelopeId <- envelopeIdRegex.findFirstMatchIn(body).map(_.group(1))
            pdfBytes <- pdfBytesRegex.findFirstMatchIn(body).map(_.group(1))
            userName <- userNameRegex.findFirstMatchIn(body).map(_.group(1))
          } yield {
            logger.info(s"Found PDF bytes starting with: ${pdfBytes.take(100)}")
            val decodedPdfBytes = java.util.Base64.getDecoder.decode(pdfBytes)
            val s3Key = generateS3Key(userName, envelopeId)
            logger.info(s"Generated S3 key: $s3Key")

            S3Utils.saveToS3(decodedPdfBytes, s3Key).attempt.flatMap {
              case Right(_) =>
                logger.info(s"Successfully saved document to S3: $s3Key")
                Ok(Json.obj("envelopeId" -> Json.fromString(envelopeId)))
              case Left(error) =>
                logger
                  .error(s"Failed to save document to S3: ${error.getMessage}")
                InternalServerError(
                  Json.obj(
                    "error" -> Json.fromString("Failed to save document"),
                    "details" -> Json.fromString(error.getMessage)
                  )
                )
            }
          }).getOrElse {
            logger.error("Failed to extract required fields from payload")
            BadRequest(
              Json.obj(
                "error" -> Json.fromString("Missing required fields in payload")
              )
            )
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

  def generateS3Key(username: String, envelopeId: String): String = {
    val date = java.time.LocalDate.now.toString
    s"$username-$envelopeId-$date.pdf"
  }
}