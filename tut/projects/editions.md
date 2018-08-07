---
title: "Generate editions from XML source"
layout: page
---


## Prepare a corpus

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus), and tokenize the text (as explained in [this tutorial]()../tokenize)).

```tut:silent
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.latinmodel._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)

val urn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")

val eins121 = repo.corpus ~~ urn
val tokens = LatinTeiReader.fromCorpus(eins121)
```

## Create a diplomatic edition

You can read your tokens with a purely diplomatic reading:

```tut:silent
val diplomaticTokens = tokens.map(_.readWithDiplomatic)
```
