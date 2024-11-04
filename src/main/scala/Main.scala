import org.http4s.server.blaze._
import cats.effect.{IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder
import scala.concurrent.ExecutionContext.global

object Main extends IOApp.Simple {

  def run: IO[Unit] = {
    // Initialize and start the HTTP server here
    val httpApp = Ingester.routes.orNotFound
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
  }
}
