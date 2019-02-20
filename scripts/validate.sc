import edu.holycross.shot.mid.validator._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.latin._
import scala.io.Source
import java.io.PrintWriter


val readerMap =   Map(
  "MidProseABReader" ->   MidProseABReader.readers,
  "MidNeumeReader" -> MidNeumeReader.readers
)


import edu.holycross.shot.virgapes._

val orthoMap = Map(
  "Latin23" -> Latin23Alphabet,
  "Virgapes" -> Virgapes
)



def readersForString(readerName: String): Vector[MidMarkupReader] = {
  if (readerMap.keySet.contains(readerName)){
    readerMap(readerName)
  } else {
    throw (new Exception(s"${readerName} is not a recognized MidReader in this project."))
  }
}

def orthoForString(orthoName: String): MidOrthography = {
  if (orthoMap.keySet.contains(orthoName)){
    orthoMap(orthoName)
  } else {
    throw (new Exception(s"${orthoName} is not a recognized Orthography in this project."))
  }
}


def readerMappings(csvSource : String = "editions/readers.csv") = {
  // drop header row:
  val csvRows = Source.fromFile(csvSource).getLines.toVector.tail
  val pairs = for (row <- csvRows) yield {
    val parts = row.split(",").toVector
    ReadersPairing(CtsUrn(parts(0)), readersForString(parts(1)))
  }
  pairs.toVector
}

def orthoMappings(csvSource : String = "editions/orthographies.csv") = {
  val csvRows = Source.fromFile(csvSource).getLines.toVector.tail
  val pairs = for (row <- csvRows) yield {
    val parts = row.split(",").toVector
    OrthoPairing(CtsUrn(parts(0)), orthoForString(parts(1)))
  }
  pairs.toVector
}


val repo = EditorsRepo(".")
val midValidator = Validator(repo, readerMappings(), orthoMappings())
val reporter = ValidationReporter(midValidator)



def validate(uString : String) = {
  reporter.validate(uString)
}


def paleo: Unit = {
  val f = "paleography/bern88.cex"
  val cex = Source.fromFile(f).getLines.toVector.tail.mkString("\n")
  val  paleoResults = PaleographyResults(cex)

  val home = StringBuilder.newBuilder
  home.append(s"#Paleography validation\n\n")


  // 1.  Paleography validation
  if (paleoResults.bad.isEmpty ) {
    home.append(s"-  ![errors](${okImg}) Paleography validation: there were no errors. \n")
  } else {
    println("There were errors finding paleographic observations.")
    home.append("-  Paleography validation: there were errors. ")
  }

  val hdr = "| reading     | image     |\n| :------------- | :------------- |\n"


  val imgs = ImageManager()
  val rows = for (obs <- paleoResults.good) yield {
    s"| ${obs.reading} | ${imgs.markdown(obs.img)} |"
  }
  home.append("\n\n"+ hdr + rows.mkString)

  new PrintWriter("validation/paleography.md"){write(home.toString);close;}
  println("\nPaleography report written to  file validation/paleography.md.")
}


println("\n\nValidate paleography:")
println("\n\tpaleo")

println("\n\nValidate editorial work for a given page:")
println("\n\tvalidate(\"PAGEURN\")\n\n")
