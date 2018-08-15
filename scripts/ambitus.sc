import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._
import io.github.hcmid.chant._
import edu.holycross.shot.virgapes._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"


val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
val c = repo.corpus

// XML corpus
val neumes = c ~~ CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")

// Convert Vector of XML nodes to
// Vector of virgapes plain-text nodes
val plainText = neumes.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))

/**Drop signifactive letters from neume string */
def dropLetters(cn: CitableNode) : CitableNode ={
  val neumeText = cn.text.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumes = for (n <- neumeText) yield {
    val parts = n.split("\\.")
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
  CitableNode(cn.urn, neumes.filter(_.pitches > 0 ).mkString(" "))
}

// Only pitched neuemes, no letters
val noLetters = plainText.map(dropLetters(_))

def movement (cn : CitableNode) = {
  val neumeText = cn.text.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumes = for (n <- neumeText) yield {
    val parts = n.split("\\.")
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
  neumes.map( n =>  NeumeRelation.relationForName(n.name) )
}

val mvs = noLetters.map(movement(_))

val deOptioned = mvs.map(_.flatten)
val summed = deOptioned.map(NeumeRelation.sum(_))
val urnList = neumes.nodes.map(_.urn)
/*
val labelled = summed.map( nr => nr.pitches.map(_.label).mkString(" ") )

val urnList = neumes.nodes.map(_.urn)
val  citableNeumeRelations =  urnList zip labelled
val relationCorpus =  Corpus(citableNeumeRelations.map{ case (u,s) => CitableNode(u,s) })
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
