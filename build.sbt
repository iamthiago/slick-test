name := "slick-test"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.4.189",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.zaxxer" % "HikariCP-java6" % "2.3.9",
  "com.walmart" %% "common" % "0.0.1"
)