ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/aoiroaoino/nanolens"),
    "scm:git@github.com:aoiroaoino/nanolens.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "aoiroaoino",
    name = "Naoki Aoyama",
    email = "aoiro.aoino@gmail.com",
    url = url("https://github.com/aoiroaoino")
  )
)
ThisBuild / licenses := Seq("MIT License" -> url("https://opensource.org/licenses/mit-license"))
ThisBuild / homepage := Some(url("https://github.com/aoiroaoino/nanolens"))
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
publishTo := sonatypePublishToBundle.value
sonatypeCredentialHost := "s01.oss.sonatype.org"

import ReleaseTransformations._

releaseCrossBuild := true
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
