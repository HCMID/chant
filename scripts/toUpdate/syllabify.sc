// THIS SCRIPT NEEDS UPDATING TO USE AN MidReader and MidOrthography
// for extracting plain-text from XML and for tokenization, respectively.
//
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._
import edu.holycross.shot.latin._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
val txt = repo.corpus ~~  CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")
val tokens= LatinTeiReader.fromCorpus(txt)

val alphabet = edu.holycross.shot.latin.Latin23Alphabet
val diplomaticTokens = tokens.map(_.readWithDiplomatic)
val diplomaticLC  = diplomaticTokens.map(_.text.toLowerCase).map(_.replaceAll("v", "u"))
val syllables = for (diplToken <- diplomaticLC) yield {
  val latinString = LatinString(diplToken, alphabet)
  latinString.syllabify
}
