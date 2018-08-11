import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._
import edu.holycross.shot.latin._
import edu.holycross.shot.virgapes._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
val txt = repo.corpus ~~  CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")
val neumes = repo.corpus ~~ CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")


def alignedWords (psg: String = "11.introit.1") = {
  val neumesUrnString = "urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:"
  val textUrnString = "urn:cts:chant:antiphonary.einsiedeln121.text_xml:"
  val textCorpus = txt ~~ CtsUrn(textUrnString + psg)
  val neumesCorpus = neumes ~~ CtsUrn(neumesUrnString + psg)

  val alignment = Aligner.alignMidCorpora(neumesCorpus, textCorpus)
  alignment.map(AlignedWord(_))
}

def alignVertical(psg: String = "11.introit.1") = {
  val words = alignedWords(psg)
  for (wd <- words) {
    println("\t"+ wd.word + "\n" + wd.interleave() + "\n")
  }
}

def plainText(psg: String = "11.introit.1") = {
  val words = alignedWords(psg)
  words.map(_.word).mkString(" ")
}
