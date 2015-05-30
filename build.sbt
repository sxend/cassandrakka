import bintray.Keys._

organization := "arimitsu.sf"

name := "cassandrakka"

version := "0.0.1-001"

scalaVersion := "2.11.6"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "sxend repo releases" at "http://dl.bintray.com/sxend/releases"

resolvers += "sxend repo snapshots" at "http://dl.bintray.com/sxend/snapshots"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC2",
  "com.chuusai" %% "shapeless" % "2.2.0-RC6",
  "net.jpountz.lz4" % "lz4" % "1.3.0",
  "org.xerial.snappy" % "snappy-java" % "1.1.1.7",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

publishMavenStyle := false

Seq(bintraySettings: _*)

repository in bintray := {
  if (version.value.matches("^[0-9]\\.[0-9]*\\.[0-9]*$")) "releases" else "snapshots"
}

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

javacOptions ++= Seq("-source", "1.8")