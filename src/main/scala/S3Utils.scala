import cats.effect.IO
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.{S3Client, S3ClientBuilder}
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.http.apache.ApacheHttpClient

object S3Utils {
  private val accessKeyId = sys.env("AWS_ACCESS_KEY_ID")
  private val secretAccessKey = sys.env("AWS_SECRET_ACCESS_KEY")
  private val region = sys.env("AWS_REGION")

  private val credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)
  private val client: S3Client = S3Client.builder()
    .credentialsProvider(StaticCredentialsProvider.create(credentials))
    .httpClient(ApacheHttpClient.builder().build())
    .region(Region.of(region))
    .build()

  def saveToS3(pdfBlob: Array[Byte], key: String): IO[Unit] = IO {
    val request = PutObjectRequest.builder()
      .bucket("docusign-pdfs-databonds")
      .key(key)
      .contentType("application/pdf")
      .build()

    client.putObject(request, RequestBody.fromBytes(pdfBlob))
  }
}
