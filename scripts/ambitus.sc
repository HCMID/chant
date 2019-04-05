
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.latin._
import edu.holycross.shot.virgapes._
import java.io.PrintWriter
import io.github.hcmid.chant._



// 1. Create a text repository from files on disk:
val catalog = "editions/catalog.cex"
val configFile = "editions/citation.cex"
val baseDir = "editions"
val repo =  TextRepositorySource.fromFiles(catalog, configFile, baseDir)
// reader for getting diplomatic edition from XML source
val textDiplomaticReader = MidProseABReader(MidDiplomaticEdition)
val neumeDiplomaticReader = MidNeumeReader(MidDiplomaticEdition)

val sg359textUrn = CtsUrn("urn:cts:chant:massordinary.sg359.text_xml:")
val sg359neumeUrn = CtsUrn("urn:cts:chant:massordinary.sg359.neumes_xml:")

// XML editions:
val sg359Text = repo.corpus ~~ sg359textUrn
val sg359Neumes = repo.corpus ~~ sg359neumeUrn

val sg359diplomaticNeumes  = textDiplomaticReader.edition(sg359Neumes)


// Citable nodes from a plain-text edition of neumes
val neumes =  sg359diplomaticNeumes.nodes





/** Drop signifactive letters from the string value of a ciitable node.
*
* @param cn Citable node with neumes data.
 */
def dropLetters(cn: CitableNode) : CitableNode ={
  val neumeText = cn.text.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumeList = for (n <- neumeText) yield {
    val parts = n.split("\\.").toVector
    //println("MAKE NEUME FROM PARTS " + parts)
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
  CitableNode(cn.urn, neumeList.filter(_.pitches > 0 ).mkString(" "))
}

/** Relative movement of each neume in a citable node.
*
* @param cn Citable node with neumes data.
*/
def movement (cn : CitableNode) = {
  val neumeText = cn.text.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumeList = for (n <- neumeText) yield {
    val parts = n.split("\\.")
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
  neumeList.map( n =>  NeumeRelation.relationForName(n.name) )
}



// All pitched neumes (no letters) in SG 359
val noLetters = sg359diplomaticNeumes.nodes.map(dropLetters(_))
// Relative movement of all neuemes in SG 359
val mvs = noLetters.map(movement(_))
val deOptioned = mvs.map(_.flatten)
// Sum of movement
val summed = deOptioned.map(NeumeRelation.sum(_))
// Movement as a series of High/Neutral/Low labels
val labelled = summed.map( nr => nr.pitches.map(_.label).mkString(" ") )


// Zip URNs teogether with labels.
val urnList = neumes.map(_.urn)
val  citableNeumeRelations =  urnList zip labelled
val relationCorpus =  Corpus(citableNeumeRelations.map{ case (u,s) => CitableNode(u,s) })



/**  Model of the ambitus of a given passage.
*
* @param total Total number of steps.
* @param max Maximum (relative) difference in steps.
*/
case class PassageRange(total: Int, max: Int)

def scorePitches(
  pitches: Vector[RelativePitch],
  total : Int = 0,
  maxSeen : Int = 0): PassageRange = {

  if (pitches.isEmpty ) {
    PassageRange(total, maxSeen)
  } else {
    val score = pitches.head match {
      case High => 1
      case Low => -1
      case Neutral => 0
    }
    val subTotal = score + total
    //println("Compare new sub " + subTotal + " with prev max " + maxSeen)
    val newMax  : Int = if (subTotal > maxSeen) {
      subTotal
    } else {
      maxSeen
    }

    //println(pitches.head + " sub now " + subTotal + " and  max " + newMax )
    scorePitches(pitches.tail, subTotal, newMax)
  }
}


def ambitus(nr: NeumeRelation) : PassageRange = {
  scorePitches(nr.pitches, 0)
}

def slides = {
  println("")
  val ambit = ambitus(summed(0))
  println(urnList(0).passageComponent)
  println("\tStepwise total:" + ambit.max + ". Changefrom initial pitch: "+ ambit.total)
  println("")
  val ambit2 = ambitus(summed(1))
  println(urnList(1).passageComponent)
  println("\tStepwise total:" + ambit2.max + ". Changefrom initial pitch: "+ ambit2.total)
}
