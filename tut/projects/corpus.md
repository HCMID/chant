---
title: "Load a text corpus"
layout: page
---


Load the libraries you'll use:

```tut:silent
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.latinmodel._
```


To create a digital repository, we identify a `cex` file cataloging our texts, a second `cex` file documenting how each text's citation scheme is represented in XML, and a root directory for finding XML files.  This is how they're defined for our github repository:


````tut:silent
val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"
```

Now you can create digital text repository in a single line:

````tut:silent
val repo = TextRepositorySource.fromFiles(catalog,citation,editions)
```
