import cats.effect.IO
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import io.circe.syntax._

object DynamoDBUtils {
  val client: DynamoDbClient = DynamoDbClient.builder().build()

  def saveToDynamoDB(ingestRequest: IngestRequest): IO[Unit] = IO {
    val item = Map(
      "field1" -> ingestRequest.field1.asJson.noSpaces
    ).asJava
    
    val request = PutItemRequest.builder()
      .tableName("your-dynamodb-table-name")
      .item(item)
      .build()

    client.putItem(request)
  }
} 