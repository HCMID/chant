import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._

val catalog = "editions/catalog.cex"
val citation = "editions/citation.cex"
val editions = "editions"


val repo = TextRepositorySource.fromFiles(catalog,citation,editions)


val tokens = TeiReader.fromCorpus(repo.corpus)
