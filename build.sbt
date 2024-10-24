name := "DocuSign-Ingester"
version := "0.1"
scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % "0.23.12",
  "org.http4s" %% "http4s-circe" % "0.23.12",
  "io.circe" %% "circe-generic" % "0.14.2",
  "software.amazon.awssdk" % "s3" % "2.20.9",
  "software.amazon.awssdk" % "dynamodb" % "2.20.9"
)