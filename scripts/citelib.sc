// The data in the analyses diretory need updating: see the README in that dir.
import edu.holycross.shot.ohco2._
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
//import edu.holycross.shot.mid.latinmodel._


val libCex = "analyses/multicorpus.cex"

val cite = CiteLibrarySource.fromFile(libCex, "#", ",")
