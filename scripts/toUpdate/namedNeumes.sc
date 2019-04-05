// THIS SCRIPT NEEDS UPDATING TO USE AN MidReader and MidOrthography
// for extracting plain-text from XML and for tokenization, respectively.
//
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._
import edu.holycross.shot.virgapes._

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

val plainText = neumes.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))


def nameNeumes(cn: CitableNode) : CitableNode ={

  val neumeText = cn.text.split("[ \t\n\\-]").toVector.filter(_.nonEmpty)
  val neumes = for (n <- neumeText) yield {
    val parts = n.split("\\.")
    Neume(parts(0).toInt, parts(1).toInt, parts(2).toInt, parts(3).toInt)
  }
  CitableNode(cn.urn, neumes.map(_.name).mkString(" "))
}

val namedCorpus = Corpus(plainText.map(nameNeumes(_)))

import java.io.PrintWriter
def printNamed = {
  new java.io.PrintWriter("named-neumes.cex"){ write(namedCorpus.cex("#")); close;}
}
