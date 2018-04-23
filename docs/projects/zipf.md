---
title: "Do neumes follow Zipf's law?"
layout: page
---

Do neumes obey Zipf's Law?  Let's find out.

## Get a neumes edition

Load the libraries you'll use, and create a digital text corpus as [explained in this tutorial](../corpus), then select an edition of neumes.

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
val neumesUrn = CtsUrn("urn:cts:chant:antiphonary.einsiedeln121.neumes_xml:")
val eins121neumeCorpus  = repo.corpus ~~ neumesUrn
```



## Collect neume names from edition


We begin by extracting the text content of each citable passage, then use a `Syllabifier` to parse the text contents into neumed syllables containing 1 or more neumes.   (We will ignore syllables that fail to parse.)

The result is a vector composed of entries for each citable passage consisting of a vector of syllables. We can flatten those to get a single vector of all neuemed syllables in our edition.

```scala:silent

val neumeText = eins121neumeCorpus.nodes.map(cn => CitableNode(cn.urn, edu.holycross.shot.mid.latinmodel.collectText(cn.text)))

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
// organized by citable passage:
val passagesReal = eins121neumeOptions.flatten
val syllablesReal = passagesReal.flatten
```



Now we want to extract from each neumed syllable its invidivual neuemes.  For counting frequencies, it will be easiest to work with the names of each neume, so we'll collect those.

```scala:silent

val neumesReal = for (s <- syllablesReal) yield {
  s.neumes
}
val neumes = neumesReal.flatten
val neumeNames = neumes.map(_.name)

println("Passages " + s", ${passagesReal.size} citable passages")
println("Syllables " + s"${syllablesReal.size} syllables")
println("Neumes " + s"${neumeNames.size} neumes")
```




### A handy function

Before we start counting neumes, we'll define a short recursive function to total up a vector of integer values.  That will be useful for displaying our counts as propoprtions or percentages (ie, as the number of occurrences of the individual neume divided by the total number of neumes).

```scala:silent
/** Sum up a vector of integers recursively.
*
*  @param v Vector to sum up.
*  @param subTotal Total seen so far.
*/
def sum(v: Vector[Int], subTotal: Int = 0): Int = {
  v.size match {
    case 0 => subTotal
    case _ => v.head + sum(v.tail)
  }
}
```


## Compute frequencies

This is one way in Scala to count frequencies of individual elements in a list, sort the result by frequency, and pretty print a table of percentages.

```scala:silent

val grouped = neumeNames.groupBy(w => w)
val freqs = grouped.map { case (k,v) => (k,v.size) }
//Make a sequence out of the map and sort by the second part
// (ie, the *value*, not the key)
val sorted = freqs.toSeq.sortBy(_._2).reverse

val totalNeumes = sum (sorted.map(_._2).toVector)

for (kvpair <- sorted) {
  val pct = (kvpair._2 / totalNeumes.toFloat) * 100
  println(kvpair._1 +  "\t" + f"$pct%1.1f")
}
```
