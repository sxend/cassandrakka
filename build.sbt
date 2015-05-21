import bintray.Keys._

organization := "arimitsu.sf"

name := "cassandrakka"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "sxend repo releases" at "http://dl.bintray.com/sxend/releases"

resolvers += "sxend repo snapshots" at "http://dl.bintray.com/sxend/snapshots"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC2",
  "org.apache.cassandra" % "cassandra-all" % "2.1.5",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

publishMavenStyle := false

Seq(bintraySettings: _*)

repository in bintray := {
  if (version.value.matches("^[0-9]\\.[0-9]*\\.[0-9]*$")) "releases" else "snapshots"
}

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

javacOptions ++= Seq("-source", "1.8")