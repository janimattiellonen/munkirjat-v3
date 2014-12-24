import play.PlayScala


name := """munkirjat"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  ws,
  "com.typesafe.slick" %% "slick-codegen" % "2.1.0-RC3",
  "com.mohiva" %% "play-silhouette" % "1.0",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" % "jquery" % "1.11.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "net.codingwell" %% "scala-guice" % "4.0.0-beta4",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "mysql" % "mysql-connector-java" % "5.1.32",
cache
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)