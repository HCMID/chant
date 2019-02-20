import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.latin._
import edu.holycross.shot.virgapes._
import java.io.PrintWriter

// 1. Create a text repository from files on disk:
val catalog = "editions/catalog.cex"
val configFile = "editions/citation.cex"
val baseDir = "editions"
val repo =  TextRepositorySource.fromFiles(catalog, configFile, baseDir)
// reader for getting diplomatic edition from XML source
val textDiplomaticReader = MidProseABReader(MidDiplomaticEdition)
val neumeDiplomaticReader = MidNeumeReader(MidDiplomaticEdition)

val sg359textUrn = CtsUrn("urn:cts:chant:massordinary.sg359.text_xml:")
val sg359neumeUrn = CtsUrn("urn:cts:chant:massordinary.sg359.neumes_xml:")

// XML editions:
val sg359Text = repo.corpus ~~ sg359textUrn
val sg359Neumes = repo.corpus ~~ sg359neumeUrn


/////////// Text editions:
// Diplomatic edition:
val sg359DiplText = textDiplomaticReader.edition(sg359Text)
// Toeknized to words
val sg359diplomaticWordTokens =
Latin23Alphabet.tokenizeCorpus(sg359DiplText).filter(_.tokenCategory.toString == "Some(LexicalToken)")

// Make lower-case version before syllabifying:
val wordNodes = sg359diplomaticWordTokens.map(tkn => CitableNode(tkn.urn, tkn.string.toLowerCase))
val sg359lcWords = Corpus(wordNodes)
// Tokenize lower-case to syllables:
val sg359textSyllables = Latin23Syllable.tokenizedCorpus(sg359lcWords)


/////////// Neume editions:
// Diplomatic edition:
val sg359diplomaticNeumes  = textDiplomaticReader.edition(sg359Neumes)
val sg359neumeSyllables = VirgapesSyllables.tokenizedCorpus(sg359diplomaticNeumes)
//val sg359neumes = Virgapes.tokenizedCorpus(sg359diplomaticNeumes)


val imgDir = "neumes/"

def neumeGlyph(neumeCode: String, baseDir : String = imgDir) : String = {
  "![" + neumeCode + "](" + baseDir +  neumeCode + ".png)"
}

def neumeGlyphHtml(neumeCode: String, baseDir : String = imgDir) : String = {
  val html = "<img src=\"" +  baseDir +  neumeCode + ".png\" width=\"20\"/>"
  html
}


// For a passage idetnified by urn, try to
// pair up syllables:
def pairPassages(urn: CtsUrn): String = {
  val md = new StringBuilder()
  md ++= "\n**" + urn.dropVersion + "**\n"
  val text = sg359textSyllables ~~ urn.dropVersion
  val neumes = sg359neumeSyllables ~~ urn.dropVersion
  if (text.size == 0) {
    md ++= ">No text node matching urn " + urn.passageComponent + "\n"
  }
  if (neumes.size == 0) {
    md ++= ">No neumes node matching urn " + urn.passageComponent + "\n"
  }
  if ((text.size >= 1) && (neumes.size >= 1)) {
    if (text.size != neumes.size) {
      md ++= ">Hmm...\n"
    md ++= s">${text.size} text syllables\n${neumes.size} neume syllables\n"
    }
    val shortLimit = if (text.size > neumes.size) {neumes.size} else {text.size}
    val max = if (text.size > neumes.size) {text.size} else {neumes.size}
    for (i <- 0 until shortLimit) {

      val neumeList = Virgapes.tokenizeNode(neumes.nodes(i)).filter(_.tokenCategory.toString == "Some(NeumeToken)").map(_.string)
      md ++= "\n*" + text.nodes(i).urn.passageComponent + "*:  **" + text.nodes(i).text + "** " + neumes.nodes(i).text
      for (n <- neumeList) {
        md ++= " " + neumeGlyphHtml(n)
      }
      md ++= "\n"
    }
    md ++= "\n>Unpaired syllables:\n"
    for (i <- shortLimit until max) {
      if (text.size > neumes.size) {
        md ++=  "\n*" + text.nodes(i).urn.passageComponent + "*: " + "**" + text.nodes(i).text + "**\n"
      } else {
        md ++=  "*" + neumes.nodes(i).urn.passageComponent + "*: " + neumes.nodes(i).text + "\n"
      }
    }
  } else {
    //println("\tNO matches in one edition for " + urn.dropVersion)
  }
  md.toString
}




def summary = {
  println("\n\nOverview of Einsiedeln 359, text and neumes")
  println("--------------------------------------------")
  println("\nText edition:")
  println(s"${sg359DiplText.size} citable units")
  println(s"${wordNodes.size} word tokens")
  println(s"${sg359textSyllables.size} syllable tokens")

  println("\nNeumes edition:")
  println(s"${sg359diplomaticNeumes.size} citable units")
  println(s"${sg359neumeSyllables.size} syllable tokens")
}


def testEditions = {
  summary
  println("\n\nStarting comparison...")
  val outputFile = "validation/eins359-comparison.md"
  val lines = for (u <- sg359DiplText.nodes.map(_.urn)) yield {
    pairPassages(u)
  }
  val header = "# Comparison of Einsiedeln 359, text and neumes\n\n"
  new PrintWriter(outputFile){write(header + lines.mkString("\n")); close;}
  println("Done.\nReport written to " + outputFile)
}


println("\n\n")
println("Display summary of text and neume editions on screen:")
println("\tsummary")
println("\nWrite report comparing every node in text edition with neume edition:")
println("\ttestEditions")



/*



// 4.  Ayllable objects
val seikilosNotesSyllables = diplomaticNotes.nodes.map(n =>  {
    val tkns = GreekMusic.tokenizeNode(n)
    GreekMusic.syllablesForTokens(tkns)
  }
).flatten



def sumVector(v: Vector[Int], total : Int = 0) : Int = {
  if (v.isEmpty) { total }  else {
    sumVector(v.tail, total + v.head)
  }
}
val noteCount = sumVector(seikilosNotesSyllables.map(_.notes.size))

println("\n\nLoaded data:")
println("Citable nodes of Seikilos music: " + seikilosNotesCorpus.size)
println("Number of syllables:  " + seikilosNotesSyllables.size)
println("Number of notes: " + noteCount)



println("\nCitable nodes of Seikilos text: "+ seikilosTextCorpus.size)
println("Number of words: " + seikilosWordsCorpus.size)
println("Number of syllables: " + seikilosSyllabusCorpus.size)


println("\nExample 1: find ratios of notes to syllables for a whole line:\n")


val baseRef = "urn:cts:greekMusic:seikilos.1:"
val urns = for (line <- 1 to 4) yield {
  CtsUrn(baseRef + line)
}
val ratios = for (lineUrn <- urns) yield {
  val relevant = seikilosNotesSyllables.filter(_.urn ~~ lineUrn)
  println("\nRef.: " + lineUrn)
  val sylls = relevant.size
  val notes = sumVector(relevant.map(_.notes.size))
  println("Syllables: " + sylls)
  println("Notes: " + notes)
  val ratio = (notes.toFloat / sylls)
  println("Ratio: " + ratio )
  ratio
}


println("\n\nExample 2: find words with most melismatic syllable:")
// Find musical syllables with largest number of notes
val maxNotes = seikilosNotesSyllables.map(_.notes.size).max
val maxMelisma = seikilosNotesSyllables.filter(_.notes.size == maxNotes)


// This function takes a URN for a single musical syllable and finds the
// corresponding node in the edition of text tokenized to words
def wordForNoteSyllable(noteUrn: CtsUrn, syllables: Corpus = seikilosSyllabusCorpus, words :Corpus = seikilosWordsCorpus) = { //: CitableNode = {
  val citableUnit = noteUrn.collapsePassageTo(1).dropVersion
  val dotParts = noteUrn.passageComponent.split("\\.")
  val lastIndex = dotParts.last.toInt
  val textSyllables = syllables ~~ citableUnit
  val wordUrn = textSyllables.nodes(lastIndex).urn.dropVersion.collapsePassageBy(1)
  println(wordUrn)
  (words ~~ wordUrn).nodes(0)
}

// get correspoonsding words:
val melismaticWords = for (noteSyllable <- maxMelisma) yield {
  wordForNoteSyllable(noteSyllable.urn)
}

println(s"\n\nWords with syllables containing maximum (${maxNotes}) number of notes:" )
for ( (word,index) <- melismaticWords.zipWithIndex) {
  println(word.text + " containing syllable " + maxMelisma(index).s )
}
*/
