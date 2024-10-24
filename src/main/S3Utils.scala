import cats.effect.IO
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Paths

object S3Utils {
  val client: S3Client = S3Client.builder().build()

  def saveToS3(pdfBlob: Array[Byte], key: String): IO[Unit] = IO {
    val request = PutObjectRequest.builder()
      .bucket("your-s3-bucket-name")
      .key(key)
      .build()

    // You may want to write the blob to a temporary file, or use streams depending on the size
    // This example assumes you have saved the blob to a path
    client.putObject(request, Paths.get("/path/to/pdf"))
  }
}