---
title: "Tokenize a Latin edition"
layout: page
---

## Prepare a corpus

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus)

```tut:silent
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.latinmodel._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"

val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
```


## Select and tokenize an XML edition


The text repository includes a catalog and a corpus of textual data.
Identify an XML edition you want to tokenize with a `CtsUrn`, and select its contents with the twiddle operator `~~`.  This creates a new corpus containing only the text contents with matching URNs.

The `LatinTeiReader` object can tokenize an entire latin corpus in a single line:

```tut:silent
val urn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.text_xml:")

val eins121 = repo.corpus ~~ urn
val tokens = LatinTeiReader.fromCorpus(eins121)
```
