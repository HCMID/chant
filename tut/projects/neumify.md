---
title: "Work with a neumes edition"
layout: page
---


## Prepare a corpus

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus), and tokenize the text (as explained in [this tutorial]()../tokenize)).

```scala:silent
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.latinmodel._
import edu.holycross.shot.latin._
import edu.holycross.shot.virgapes._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
```


## Select a neumes edition

```scala:silent
val urn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")
val eins121neumeNodes = repo.corpus ~~ urn
val neumeText = eins121neumeNodes.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))

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

val eins121neumes = eins121neumeOptions.flatten


///val neumeNames = eins121neumes.map(_.name)

```
