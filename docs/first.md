---
title: "What you always do first"
layout: page
---

All our material is cited canonically using URN notation. We will always import the `cite` library to work with URNs:

```scala:silent
import edu.holycross.shot.cite._
```

Our editions follow the OHCO2 model for the structure of citable texts. We use the `ohco2` library to work with citable text documents:

```scala:silent
import edu.holycross.shot.ohco2._
```

The contents of our citable texts follow editorial standards developed at HC MID. We use the `latinmodel` library to analyze the contents of Latin texts following this model:

```scala:silent
import edu.holycross.shot.mid.latinmodel._
```

## For more information


If you're interested in learning more about using these libraries beyond the examples in these tutorials, the `cite` and `ohco2` libraries each have web sites with extensive documentation:


-   `cite` library:  see <https://cite-architecture.github.io/xcite/>
-   `ohco2` library:  see <https://cite-architecture.github.io/ohco2/>

Limited information about the `latinmodel` library is available from the github repository at <https://github.com/HCMID/latin-text-model>.
