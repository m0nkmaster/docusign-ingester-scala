import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import io.circe.generic.auto._
import org.http4s.circe._

case class IngestRequest(field1: String, pdfBlob: Array[Byte])

object Ingester {
  val routes = HttpRoutes.of[IO] {
    case req @ POST -> Root / "ingest" =>
      for {
        ingestRequest <- req.as[IngestRequest]
        _ <- DynamoDBUtils.saveToDynamoDB(ingestRequest)
        _ <- S3Utils.saveToS3(ingestRequest.pdfBlob, generateS3Key(ingestRequest.field1))
        resp <- Ok("Data processed")
      } yield resp
  }

  def generateS3Key(field1: String): String = {
    s"$field1-${System.currentTimeMillis}.pdf"
  }
}
