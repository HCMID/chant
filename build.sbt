
scalaVersion := "2.12.3"


resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


libraryDependencies ++= Seq(

  "edu.holycross.shot.cite" %% "xcite" % "3.6.0",
  "edu.holycross.shot" %% "scm" % "6.1.1",
  "edu.holycross.shot" %% "ohco2" % "10.9.0",
  "edu.holycross.shot" %% "citeobj" % "7.1.1",
  "edu.holycross.shot" %% "cex" % "6.2.1",

  "edu.holycross.shot.mid" %% "latin-text-model" % "1.3.0"
)
tutTargetDirectory := file("docs")
tutSourceDirectory := file("tut")
enablePlugins(TutPlugin)
