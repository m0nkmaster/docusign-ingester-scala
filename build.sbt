ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "docusign-ingester",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % "0.23.15",
      "org.http4s" %% "http4s-circe" % "0.23.15",
      "org.http4s" %% "http4s-dsl" % "0.23.15",
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1",
      "org.typelevel" %% "cats-effect" % "3.5.2",
      "software.amazon.awssdk" % "s3" % "2.21.33",
      "software.amazon.awssdk" % "dynamodb" % "2.21.33",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.12.100",
     //  "io.github.cdimascio" % "java-dotenv" % "5.2.2",
      "software.amazon.awssdk" % "apache-client" % "2.20.68",
      "ch.qos.logback" % "logback-classic" % "1.2.11"    )
  )

assembly / assemblyJarName := "docusign-ingester-assembly.jar"

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf"              => MergeStrategy.concat
  case x                             => MergeStrategy.first
}

enablePlugins(BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](
  version,
  "buildDate" -> java.time.Instant.now().toString
)
buildInfoPackage := "models"
