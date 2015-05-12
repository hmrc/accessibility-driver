organization := "uk.gov.hmrc"

name := "accessibility-driver"

version := "1.1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "org.seleniumhq.selenium" % "selenium-java" % "2.45.0",
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "2.45.0",
  "org.littleshoot" % "littleproxy" % "1.0.0-beta8",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "commons-codec" % "commons-codec" % "1.10",
  "commons-io" % "commons-io" % "2.4",
  "org.jsoup" % "jsoup" % "1.8.2"
)

crossScalaVersions := Seq("2.10.4", "2.11.5")