
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
val diplomaticReader = MidProseABReader(MidDiplomaticEdition)

case class EditionPair(textUrn: CtsUrn, neumeUrn: CtsUrn)

val sg359textUrn = CtsUrn("urn:cts:chant:massordinary.sg359.text_xml:")
val sg359neumeUrn = CtsUrn("urn:cts:chant:massordinary.sg359.neumes_xml:")
val sg359 =  EditionPair(sg359textUrn,sg359neumeUrn)



val einstextUrn = CtsUrn("urn:cts:chant:massordinary.eins121.text_xml:")
val einsneumeUrn = CtsUrn("urn:cts:chant:massordinary.eins121.neumes_xml:")
val eins121 = EditionPair(einstextUrn, einsneumeUrn)




def melisma(edPair : EditionPair, corpus: Corpus = repo.corpus) = {
    val textCorpus = corpus ~~ edPair.textUrn
    val tokens = Latin23Alphabet.tokenizeCorpus(diplomaticReader.edition(textCorpus))


    val neumesCorpus = corpus ~~ edPair.neumeUrn
    val txtPsgSet =  (corpus ~~ edPair.textUrn).nodes.map(_.urn.passageComponent).distinct
    val neumePsgSet =  (corpus ~~ edPair.neumeUrn).nodes.map(_.urn.passageComponent).distinct
    val bothLists = txtPsgSet.intersect(neumePsgSet)




    //println("Compare editing:")
    //println("Text nodes: " + textCorpus.size)
    //println("Neume nodes: " + neumesCorpus.size)
    println("Total nodes in text and neume editions: " + bothLists.size)
    for (psg <- bothLists) {
      val u = CtsUrn(edPair.textUrn.toString + psg).dropVersion
      println("Edit " + u)
      val psgTokens = tokens.filter(_.urn ~~ u)
      val syllabified = for (tkn <- psgTokens) yield {
         Latin23Alphabet.syllabify(tkn.string)
      }
      println(syllabified)

    }


}


/*

val neumedPassages = eins121neumeCorpus.nodes.map(_.urn.passageComponent)

val alphabet = edu.holycross.shot.latin.Latin23Alphabet
for (psg <- neumedPassages) {
  val psgTokens = tokens.filter(edPair.textNode ~~ CtsUrn(edPair.textUrn.toString + psg))
  val diplomaticTokens = psgTokens.map(_.readWithDiplomatic)
  val diplomaticLC  = diplomaticTokens.map(_.text.toLowerCase).map(_.replaceAll("v", "u"))

  val syllabified = for (diplToken <- diplomaticLC) yield {
    val latinString = LatinString(diplToken, alphabet)
    latinString.syllabify
  }
  val syllableCount = syllabified.flatten.size

  val syllableWords = syllabified.map( v => v.mkString("-"))
  val nodeCorpus = eins121neumeCorpus ~~ CtsUrn(neumesUrn.toString + psg)
  val neumeText = nodeCorpus.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))

  val eins121neumeOptions = for (n <- neumeText) yield {
    try {
      val neume = Syllabifier(n)
      Some(neume)
    } catch {
      case t: Throwable => {
        println("FAILED on " + n)
        None
      }
    }
  }
  val passagesReal = eins121neumeOptions.flatten
  val syllablesReal = passagesReal.flatten
  val neumeCount = syllablesReal.map(_.neumes).flatten.size

  val melisma = neumeCount / syllableCount.toFloat

  println("\n\n\nPassage: " + psg )
  println(syllableWords.mkString(" "))
  println("SYLLABLES of text:  " + syllableCount)
  println("NEUMES " + neumeCount)

  println("Degree of melisma: " + f"$melisma%1.1f")
}

*/


def instructions = {

  println("\n\n")
  println("This script loads and pairs text and neume corpora for two MSS.")
  println("The pairs are named \n\n \tsg359\n\teins121\n")
  println("To display  a melismatic analysis of each citable node,")
  println("\n\tmelisma(MSNAME)")
}
