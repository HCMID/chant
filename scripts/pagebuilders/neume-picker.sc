/*
Read neume-list.cex, and write out an html file
with js for picking URNs from table.
*/
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
val header = """
<!doctype html>
<html lang="en">

<head>
  <title>Select neume from table</title>
  <style media="screen" type="text/css">
  html {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 16px;
    line-height: 1.5;
  }
  body {
    max-width: 38rem;
    padding-left:  1rem;
    padding-right: 1rem;
    margin-left:  auto;
    margin-right: auto;
    color: #515151;
    background-color: #fff;
    -webkit-text-size-adjust: 100%;
        -ms-text-size-adjust: 100%;
  }
  table {
    margin-bottom: 1rem;
    width: 100%;
    border: 1px solid #e5e5e5;
    border-collapse: collapse;
  }
  td,
  th {
    padding: .25rem .5rem;
    border: 1px solid #e5e5e5;
  }
  .selected {
    background-color: #66F;
    color: #FFF;
  }

  </style>
</head>

<body>

<h1>Virgapes neume picker</h1>

<p>Click on an entry in the table of neuemes to copy its ID to your clipboard.
</p>
<table id="table"><tr><th>Virgapes ID</th><th>Name</th><th>Example</th></tr>
"""

val trail = """
</table>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script>
$("#table tr").click(function(){
   $(this).addClass('selected').siblings().removeClass('selected');
   var idValue=$(this).find('td:first').html();
      var textArea = document.createElement("textarea");
      textArea.style.position = 'fixed';
      textArea.style.top = 0;
      textArea.style.left = 0;
      textArea.style.width = '2em';
      textArea.style.height = '2em';
      textArea.style.padding = 0;
      textArea.style.border = 'none';
      textArea.style.outline = 'none';
      textArea.style.boxShadow = 'none';
      textArea.style.background = 'transparent';
      textArea.value = idValue;
      document.body.appendChild(textArea);
      textArea.select();
      try {
        var successful = document.execCommand('copy');
        var msg = successful ? 'successful' : 'unsuccessful';
        console.log('Copying text command was ' + msg);
      } catch (err) {
        console.log('Oops, unable to copy');
      }
      document.body.removeChild(textArea);
});

$('.ok').on('click', function(e){
    alert($("#table tr.selected td:first").html());
});
</script>
</body>
</html>
"""

// format CEX columns as a row of HTML table
def formatLine(s: String) : String = {
  val cols = s.split("#")
  if (cols.size == 3) {
    val num = cols(0)
    val neumeName = cols(1)
    val illustration = cols(2)
    val width = 100

    try  {
      val urn = Cite2Urn(illustration.trim)
      val thumb = "<img src=\"" + s"http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/${urn.namespace}/${urn.collection}/${urn.version}/${urn.dropExtensions.objectComponent}.tif&WID=${width}&RGN=${urn.objectExtension}&CVT=JPEG" + "\"/>"

      s"<tr> <td>${num}</td><td>${neumeName}</td><td>${thumb}</td> </tr>"

    } catch {
      case t: Throwable => { println(s"Bad urn value: ${illustration}? ${t} "); ""}
    }

  } else {
    ""
  }
}


val srcLines = Source.fromFile(f).getLines.toVector.drop(1)
val htmlLines = for (l <- srcLines) yield {
  formatLine(l)
}

def printTable = {
  val viewFile = new File("docs/neume-picker.html")
  new PrintWriter(viewFile) { write(header + htmlLines.filter(_.nonEmpty).mkString("\n") + trail); close }
}
