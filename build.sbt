name := "funlet"

organization := "com.verknowsys"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.0-1"

crossScalaVersions := Seq("2.9.0", "2.9.0-1")

libraryDependencies ++= Seq(
    "javax.servlet" % "servlet-api" % "2.5",
    "org.fusesource.scalate" % "scalate-core" % "1.5.0",
    "org.scalatest" %% "scalatest" % "1.6.1" % "test"
)
