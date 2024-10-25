import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.circe.jsonOf
import io.circe.Json
import java.time.Instant

case class Ingester(field1: String, pdfBlob: Array[Byte])

object Ingester {
  implicit val ingestRequestDecoder = jsonOf[IO, IngestRequest]
  
  val routes = HttpRoutes.of[IO] {
    case req @ POST -> Root / "ingest" =>
      for {
        ingestRequest <- req.as[IngestRequest]
        _ <- DynamoDBUtils.saveToDynamoDB(ingestRequest)
        _ <- S3Utils.saveToS3(ingestRequest.pdfBlob, generateS3Key(ingestRequest.field1))
        resp <- Ok("Data processed")
      } yield resp
      
    case GET -> Root / "health-check" =>
      val healthStatus = Json.obj(
        "serverTime" -> Json.fromString(Instant.now().toString),
        "status" -> Json.fromString("healthy")
      )
      Ok(healthStatus)
  }

  def generateS3Key(field1: String): String = {
    s"$field1-${System.currentTimeMillis}.pdf"
  }
}
