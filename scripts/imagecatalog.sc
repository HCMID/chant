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
  val urnString = cols(0)
  val caption = cols(1)
  val width = 100



  try  {
    val urn = Cite2Urn(urnString)
    val thumb = s"http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/${urn.namespace}/${urn.collection}/${urn.version}/${urn.objectComponent}.tif&WID=${width}&CVT=JPEG"
    val ict2 = "http://www.homermultitext.org/ict2?urn="

    s"| ![](${thumb}) | [${caption}](${ict2}${urn}) |"
  } catch {
    case t: Throwable => { println(s"Bad urn value: ${urnString}. Skipping"); ""}
  }



}


// get list of CEX files in a directory
def listFiles(dir: String): Vector[File] = {
  val d = new File(dir)
  if (d.exists && d.isDirectory) {
    val realFiles =  d.listFiles.filter(_.isFile).toVector
    realFiles.filter(_.getName.matches(".+cex"))
  } else {
      Vector[File]()
  }
}




val fList = listFiles("images")

for (f <- fList) {
  val base = f.getName.replaceAll(".cex", "")

  val lines = Source.fromFile(f).getLines.toVector.drop(1)
  val header = "| image    | caption  |\n |:---------|:---------|\n"
  val markdown = for (l <- lines) yield {
    formatLine(l)
  }
  val viewFile = new File("views/" + base + ".md")
  //println(markdown.mkString("\n"))
  new PrintWriter(viewFile) { write(header + markdown.mkString("\n")); close }

}
