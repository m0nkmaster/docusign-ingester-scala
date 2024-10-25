import cats.effect.IO
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import io.circe.syntax._
import scala.jdk.CollectionConverters._


object DynamoDBUtils {
  val client: DynamoDbClient = DynamoDbClient.builder().build()

  def saveToDynamoDB(ingestRequest: IngestRequest): IO[Unit] = IO {
    import software.amazon.awssdk.services.dynamodb.model.AttributeValue

    val item = Map(
      "field1" -> ingestRequest.field1.asJson.noSpaces
    )
    
    val attributeValueMap = item.map { case (key, value) =>
      key -> AttributeValue.builder().s(value).build()
    }.asJava

    val request = PutItemRequest.builder()
      .tableName("your-dynamodb-table-name")
      .item(attributeValueMap)
      .build()

    client.putItem(request)
  }
} 