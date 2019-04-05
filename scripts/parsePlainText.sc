// THIS SCRIPT NEEDS UPDATING TO USE AN MidReader and MidOrthography
// for extracting plain-text from XML and for tokenization, respectively.
//
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"

val editions = "editions"


val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
val c = repo.corpus

val neumes = c ~~ CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")

val txt1 = c ~~  CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")
val txt2 = c ~~  CtsUrn("urn:cts:chant:antiphonary.sg359.text_xml:")
val txt = txt1 ++ txt2
val txtUrns = txt.nodes.map(_.urn)

//val tokens= LatinTeiReader.fromCorpus(txt)

val plainTextLatin = txt.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))

import java.io.PrintWriter
/** Print Latin plain text edition to CEX.
*/
def printPlainText : Unit= {
   new PrintWriter("plaintext-latin.cex"){write(Corpus(plainTextLatin).cex("#"));close;}
}
