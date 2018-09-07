---
title: "Extract syllables of neumes from XML"
layout: page
---


## Prepare a corpus

Load the libraries you'll use, create a digital text corpus as [explained in this tutorial](../corpus), and select an edition of neumes.




```scala
// after doing all imports, defining catalog, citation
// and editions values...
val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
val neumeCorpus = repo.corpus ~~ CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")
```


## Extract string values from XML

The `latinmodel` package has a `collectText` function that extracts the content of text nodes from an XML string.  We use that to take all the nodes in an XML corpus, and create a sequence of nodes with the same URNs, but just the extracted text.

```scala
val plainText = neumeCorpus.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))
```

The `virgapes` library includes a `Syllabifier` object that will create a sequence of `Syllable`s from a citable node.

```scala
val syllabifiled = plainText.map(Syllabifier(_))
```
