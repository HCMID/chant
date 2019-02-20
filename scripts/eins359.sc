import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.latin._
import edu.holycross.shot.virgapes._


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
// Diplomatic editions:
val sg359DiplText = textDiplomaticReader.edition(sg359Text)
val sg359DiplNeumes  = textDiplomaticReader.edition(sg359Neumes)


// Toeknized to words
val sg359diplomaticWords= Latin23Alphabet.tokenizedCorpus(sg359DiplText)
// Make lower-case version before syllabifying:
val wordNodes = sg359diplomaticWords.nodes.map(n => CitableNode(n.urn, n.text.toLowerCase))
val sg359lcWords = Corpus(wordNodes)
// Tokenize lower-case to syllables:
val sg359textSyllables = Latin23Syllable.tokenizedCorpus(sg359lcWords)


/////////// Neume editions:
// Diplomatic editions:
val sg359diplomaticNeumes  = textDiplomaticReader.edition(sg359Neumes)
val sg359neumeSyllables = Virgapes.tokenizedCorpus(sg359diplomaticNeumes)
/*


// 2. Create text corpora for diplomatic text, words tokenization and syllable tokenization
val seikilosTextUrn = CtsUrn("urn:cts:greekMusic:seikilos.1.text:")
val seikilosTextCorpus = repo.corpus ~~ seikilosTextUrn
val diplomaticText = reader.edition(seikilosTextCorpus)
val seikilosWordsCorpus = LiteraryGreekString.tokenizedCorpus(diplomaticText)
val seikilosSyllabusCorpus = LGSyllable.tokenizedCorpus(seikilosWordsCorpus)




// 3. Create musical notation corpora for diplomatic edition and syllables
val seikilosNotesUrn = CtsUrn("urn:cts:greekMusic:seikilos.1.notation:")
val seikilosNotesCorpus = repo.corpus ~~ seikilosNotesUrn
val diplomaticNotes = reader.edition(seikilosNotesCorpus)
val seikilosNoteSyllablesCorpus = GreekMusic.tokenizedCorpus(diplomaticNotes)



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
