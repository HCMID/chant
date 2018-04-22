

crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last



resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


libraryDependencies ++= Seq(

  "edu.holycross.shot.cite" %% "xcite" % "3.3.0",
  "edu.holycross.shot" %% "scm" % "6.0.0",
  "edu.holycross.shot" %% "ohco2" % "10.7.0",
  "edu.holycross.shot" %% "citeobj" % "7.0.1",
  "edu.holycross.shot" %% "cex" % "6.2.1",
  "edu.holycross.shot.mid" %% "latin-text-model" % "1.0.1"
)

tutTargetDirectory := file("docs")
tutSourceDirectory := file("tut")
enablePlugins(TutPlugin)
