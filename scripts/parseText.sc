import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"

val editions = "editions"


val repo = TextRepositorySource.fromFiles(catalog,citation,editions)

val c = repo.corpus

val neumes = c ~~ CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")

val txt = c ~~  CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")

val txtUrns = txt.nodes.map(_.urn)

/*
for (u <- txtUrns)  {
  println(u)
  print("\ttokens:  ")
  val oneNode = txt ~~ u
  val nodeTokens = LatinTeiReader.fromCorpus(oneNode)
  println(nodeTokens.size)

}
*/
val tokens= LatinTeiReader.fromCorpus(txt)

//
