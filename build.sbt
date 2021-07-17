import Dependencies._

ThisBuild / organization := "dev.aoiroaoino"
ThisBuild / scalaVersion := "3.0.2-RC1"

lazy val root = (project in file("."))
  .settings(name := "nanolens")
  .settings(
    libraryDependencies ++= Seq(
      scalatest_funsuite % Test,
      scalacheck_1_15    % Test
    )
  )
