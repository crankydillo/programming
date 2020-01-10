import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.beeherd"
ThisBuild / organizationName := "beeherd"

lazy val root = (project in file("."))
  .settings(
    name := "codejam",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
      scalaTest % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
    )
  )
