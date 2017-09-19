import java.io.File
import java.io.PrintWriter
import scala.io.Source
import edu.holycross.shot.cite._



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
    val urnString = cols(1)
    val caption = cols(2)
    val width = 100

    try  {
      val urn = Cite2Urn(urnString)
      val thumb = s"http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/${urn.namespace}/${urn.collection}/${urn.version}/${urn.dropExtensions.objectComponent}.tif&WID=${width}&RGN=${urn.objectExtension}&CVT=JPEG"
      val ict2 = "http://www.homermultitext.org/ict2?urn="

      s"| ![](${thumb}) | [${num}: ${caption}](${ict2}${urn}) |"
    } catch {
      case t: Throwable => { println(s"Bad urn value: ${urnString}. Skipping"); ""}
    }
  } else {
    ""
  }
}


val f = new File("datasets/neumes.cex")
val base = f.getName.replaceAll(".cex", "")

val lines = Source.fromFile(f).getLines.toVector.drop(1)
val header = "| illustration    | neume  |\n |:---------|:---------|\n"
val markdown = for (l <- lines) yield {
  formatLine(l)
}
val viewFile = new File("views/" + base + ".md")
new PrintWriter(viewFile) { write(header + markdown.filter(_.nonEmpty).mkString("\n")); close }
