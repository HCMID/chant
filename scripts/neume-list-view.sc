import java.io.File
import java.io.PrintWriter
import scala.io.Source
import edu.holycross.shot.cite._



val f = new File("datasets/neume-lists.cex")

// Run this script from root directory of repository.
//
// get files in images directory.
// display with thumbnails.
//



// format two CEX columns as a row of markdown table
def formatLine(s: String) : String = {
  val cols = s.split("#")
  if (cols.size == 3) {
    val num = cols(0)
    val neumeName = cols(1)
    val illustration = cols(2)
    val width = 100
    println("Illuystration is #" + illustration.trim + "#")

    try  {
      val urn = Cite2Urn(illustration.trim)
      println("Made URN " + urn)
      urn.toString

      val thumb = s"http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/${urn.namespace}/${urn.collection}/${urn.version}/${urn.dropExtensions.objectComponent}.tif&WID=${width}&RGN=${urn.objectExtension}&CVT=JPEG"
      val ict2 = "http://www.homermultitext.org/ict2?urn="

      s"| ${num} | ${neumeName} | [![](${thumb})](${ict2}${urn}) |"

    } catch {
      case t: Throwable => { println(s"Bad urn value: ${illustration}? ${t} "); ""}
    }

  } else {
    ""
  }
}


val base = f.getName.replaceAll(".cex", "")

val lines = Source.fromFile(f).getLines.toVector.drop(1)
val header = "| Neume number | Neume | Illustration    | \n|:---------|:---------|:---------|\n"
val markdown = for (l <- lines) yield {
  formatLine(l)
}

def printTable(outfile: String) = {
  val viewFile = new File("views/" + outfile + ".md")
  new PrintWriter(viewFile) { write(header + markdown.filter(_.nonEmpty).mkString("\n")); close }
}
