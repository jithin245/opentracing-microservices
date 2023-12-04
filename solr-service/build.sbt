name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

libraryDependencies ++= Seq(
  "io.opentelemetry" % "opentelemetry-api" % "1.9.0",
  "io.opentelemetry" % "opentelemetry-sdk" % "1.9.0",
  "io.opentelemetry" % "opentelemetry-exporter-otlp" % "1.9.0",
  "io.opentelemetry" % "opentelemetry-exporter-jaeger" % "1.9.0",
  "com.typesafe.akka" %% "akka-http" % "10.2.6",
  "com.typesafe.akka" %% "akka-stream" % "2.6.16",
  "io.jaegertracing" % "jaeger-client" % "1.8.1",
  "ch.qos.logback" % "logback-classic" % "1.2.6"
)

libraryDependencies += "io.jaegertracing" % "jaeger-client" % "0.38.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
