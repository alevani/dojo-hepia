val ScalatraVersion = "2.6.5"

organization := "hepia"

name := "dojo_test"

version := "1.0"

scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
