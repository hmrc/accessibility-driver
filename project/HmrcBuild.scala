import sbt.Keys._
import sbt._

object HmrcBuild extends Build {
  import uk.gov.hmrc.DefaultBuildSettings._
  import uk.gov.hmrc.SbtAutoBuildPlugin
  import uk.gov.hmrc.versioning.SbtGitVersioning
  import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
  import uk.gov.hmrc.SbtArtifactory.autoImport.makePublicallyAvailableOnBintray
  import uk.gov.hmrc.SbtArtifactory


  val nameApp = "accessibility-driver"

  val appDependencies = Seq(
    "org.seleniumhq.selenium" % "selenium-java" % "2.45.0",
    "org.seleniumhq.selenium" % "selenium-firefox-driver" % "2.45.0",
    "org.littleshoot" % "littleproxy" % "1.0.0-beta8",
    "joda-time" % "joda-time" % "2.7",
    "org.joda" % "joda-convert" % "1.7",
    "commons-codec" % "commons-codec" % "1.10",
    "commons-io" % "commons-io" % "2.4",
    "org.jsoup" % "jsoup" % "1.8.2",
    "org.pegdown" % "pegdown" % "1.4.2" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )

  lazy val project = Project(nameApp, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
    .settings(
      targetJvm := "jvm-1.7",
      libraryDependencies ++= appDependencies,
      scalaVersion := "2.11.5",
      crossScalaVersions := Seq("2.10.4", "2.11.5"),
      resolvers := Seq(
        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/",
        Resolver.bintrayRepo("hmrc", "releases")
      ),
      majorVersion := 1,
      makePublicallyAvailableOnBintray := true
    )
}