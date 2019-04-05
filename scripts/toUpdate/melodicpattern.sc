// THIS SCRIPT NEEDS UPDATING TO USE AN MidReader and MidOrthography
// for extracting plain-text from XML and for tokenization, respectively.
//
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
val labelled = summed.map( nr => nr.pitches.map(_.label).mkString(" ") )

val urnList = neumes.nodes.map(_.urn)
val  citableNeumeRelations =  urnList zip labelled
val relationCorpus =  Corpus(citableNeumeRelations.map{ case (u,s) => CitableNode(u,s) })


import java.io.PrintWriter
new PrintWriter("pitchCorpus.cex"){write(relationCorpus.cex("#")); close;}


/*
val totalSeq = mvs.map(_.flatten).flatten
val singleRelation = NeumeRelation.sum(totalSeq)
val relationString = singleRelation.pitches.map(_.label)

*/


/*
def zipfUpDown(s: String) ={
  val neumeText = s.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumes = for (n <- neumeText) yield {
    val parts = n.split("\\.")
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }

}*/

// -  classify neumes: up, down, neutral
//-  n-gram these or search




// FOR FINAL DISPLAY
/** Map virgapes strings to neume names */
def nameNeumes(cn: CitableNode) : CitableNode ={
  val neumeText = cn.text.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumes = for (n <- neumeText) yield {
    val parts = n.split("\\.")
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
  CitableNode(cn.urn, neumes.map(_.name).mkString(" "))
}



//val neumeSeq = plainText.map(_.text).mkString(" ")

//val namedCorpus = Corpus(plainText.map(dropLetters(_)).map(nameNeumes(_)))



//1.  filter out signifactive letters (ie, syllables == 0)

// -  classify neumes: up, down, neutral
//-  n-gram these or search
