---
title: "Align neumes and text"
layout: page
---


## Prepare a corpus

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus), and tokenize the text (as explained in [this tutorial]()../tokenize)).




```scala
// after doing all imports, defining catalog, citation
// and editions values...
val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
```


## Select a neumes and text edition

```scala
val neumesUrn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")
val textUrn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")

val eins121neumeCorpus  = repo.corpus ~~ neumesUrn
val textCorpus = repo.corpus ~~ textUrn
```


## Tokenize text edition


```scala
val tokens = LatinTeiReader.fromCorpus(textCorpus)
```


## Align text and neumes

Define an aligning function

```scala


val neumedPassages = eins121neumeCorpus.nodes.map(_.urn.passageComponent)

val alphabet = edu.holycross.shot.latin.Latin23Alphabet
for (psg <- neumedPassages) {
  val psgTokens = tokens.filter(_.textNode ~~ CtsUrn(textUrn.toString + psg))
  val diplomaticTokens = psgTokens.map(_.readWithDiplomatic)
  val diplomaticLC  = diplomaticTokens.map(_.text.toLowerCase).map(_.replaceAll("v", "u"))

  val syllables = for (diplToken <- diplomaticLC) yield {
    val latinString = LatinString(diplToken, alphabet)
    latinString.syllabify
  }
  val syllableWords = syllables.map( v => v.mkString("-"))
  println("\n\n\nFOR " + psg )
  println("\tTEXT " + CtsUrn(textUrn.toString + psg) + s",  ${syllables.size} syllables")
  println(syllableWords.mkString(" "))
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
  val neumesReal = eins121neumeOptions.flatten

  println("\tNEUMES " +  CtsUrn(neumesUrn.toString + psg) + s", ${neumesReal.flatten.size} neumes")
  println(neumesReal.flatten)
}


```
