organization := "arimitsu.sf"

name := "cassandrakka"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "org.apache.cassandra" % "cassandra-all" % "2.1.5",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
