
scalaVersion := "2.12.3"


resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


libraryDependencies ++= Seq(

  "edu.holycross.shot.cite" %% "xcite" % "3.2.1",
  "edu.holycross.shot" %% "scm" % "5.1.5",
  "edu.holycross.shot" %% "ohco2" % "10.3.0",
  "edu.holycross.shot" %% "citeobj" % "5.0.0",
  "edu.holycross.shot" %% "cex" % "6.1.0"


)
