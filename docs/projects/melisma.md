---
title: "Measuring the melismatic quality of a text"
layout: page
---


## Prepare a corpus

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus), and tokenize the text (as explained in [this tutorial](../tokenize)).

```scala:silent
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.latinmodel._
import edu.holycross.shot.latin._
import edu.holycross.shot.virgapes._

val catalog = "editions/catalog.cex"
val citation = "editions/citation-hacked.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
```


## Select a neumes and text edition

```scala:silent
val neumesUrn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")
val textUrn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")

val eins121neumeCorpus  = repo.corpus ~~ neumesUrn
val textCorpus = repo.corpus ~~ textUrn

val tokens = LatinTeiReader.fromCorpus(textCorpus)
val neumedPassages = eins121neumeCorpus.nodes.map(_.urn.passageComponent)

val alphabet = edu.holycross.shot.latin.Latin23Alphabet
for (psg <- neumedPassages) {
  val psgTokens = tokens.filter(_.textNode ~~ CtsUrn(textUrn.toString + psg))
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


```
