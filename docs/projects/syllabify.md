---
title: "Syllabify a Latin text"
layout: page
---


## Prepare a corpus

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus), and tokenize the text (as explained in [this tutorial]()../tokenize)).

```scala:silent
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.latinmodel._
import edu.holycross.shot.latin._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
```


## Select a Latin text

Select a text identified by URN, tokenize this token, and read it.  The example below chooses a diplomatic reading.

```scala:silent
val urn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")
val eins121 = repo.corpus ~~ urn
val tokens = LatinTeiReader.fromCorpus(eins121)
val diplomaticTokens = tokens.map(_.readWithDiplomatic)
```

## Syllabify


```scala
val alphabet = edu.holycross.shot.latin.Latin23Alphabet
val diplomaticLC  = diplomaticTokens.map(_.text.toLowerCase).map(_.replaceAll("v", "u"))
val syllables = for (diplToken <- diplomaticLC) yield {
  val latinString = LatinString(diplToken, alphabet)
  latinString.syllabify
}
```

You can read your tokens with a purely diplomatic reading:

```scala:silent

```
