import org.http4s.server.blaze._
import cats.effect.IOApp

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