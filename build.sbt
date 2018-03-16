

crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last



resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


libraryDependencies ++= Seq(

  "edu.holycross.shot.cite" %% "xcite" % "3.2.2",
  "edu.holycross.shot" %% "scm" % "5.3.2",
  "edu.holycross.shot" %% "ohco2" % "10.5.4",
  "edu.holycross.shot" %% "citeobj" % "6.1.1",
  "edu.holycross.shot" %% "cex" % "6.2.1"
)

enablePlugins(TutPlugin)
