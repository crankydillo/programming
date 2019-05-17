import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.beeherd"
ThisBuild / organizationName := "beeherd"

lazy val root = (project in file("."))
  .settings(
    name := "jam",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
    )
  )
