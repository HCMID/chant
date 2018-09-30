package edu.holycross.shot.mid.chant
import edu.holycross.shot.mid.validator._

// Shared by both neumes and Latin:

/** A punctuation token.*/
case object PunctuationToken extends MidTokenCategory {
  def name = "punctuation"
}


// Unique to neumes editions:

/** A neume token.*/
case object NeumeToken extends MidTokenCategory {
  def name = "neume"
}


// Unique to text editions:
/** A neume token.*/
case object LexicalToken extends MidTokenCategory {
  def name = "lexical"
}

/** A numeric token.*/
case object NumericToken extends MidTokenCategory {
  def name = "numeric"
}

/** A praenomen token.*/
case object PraenomenToken extends MidTokenCategory {
  def name = "praenomen"
}
