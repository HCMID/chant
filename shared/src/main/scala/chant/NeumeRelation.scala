package io.github.hcmid.chant

case class NeumeRelation(pitches: Vector[RelativePitch]) {

  /** Create a new [[NeumeRelation]] comprising in order
  * the relative pitches of this relation followed by the
  * relative pitches of the second relation.
  *
  * @paramnr NeumeRelation to add tothis one.
  */
  def ++ (nr: NeumeRelation): NeumeRelation = {
    NeumeRelation(pitches ++ nr.pitches)
  }

  /** Create a new [[NeumeRelation]] comprising in order
  * the relative pitches of this relation followed by the
  * relative pitches of a vector of neume relations.
  *
  * @paramnr NeumeRelation to add tothis one.
  */
  def ++ (v: Vector[NeumeRelation]): NeumeRelation = {
    if (v.isEmpty) {
      this
    } else {
      val subTotal = this ++ v.head
      subTotal ++ v.tail
    }
  }
}

object NeumeRelation {

  def sum(v: Vector[NeumeRelation]): NeumeRelation = {
    v.head ++ v.tail
  }

  def relationForName(s: String) : Option[NeumeRelation] = {
    s match {
      case "virga" => Some(NeumeRelation(Vector(Neutral)))
      case "punctum" => Some(NeumeRelation(Vector(Neutral)))
      case "pes" => Some(NeumeRelation(Vector(High)))
      case "quilisma" => Some(NeumeRelation(Vector(Neutral)))

      case "clivis" => Some(NeumeRelation(Vector(High, Low)))
      case "distropha" => Some(NeumeRelation(Vector(Neutral, Neutral)))


      case "torculus" => Some(NeumeRelation(Vector(Neutral,High, Low)))
      case "porrectus" => Some(NeumeRelation(Vector(High, Low,High)))
      case "tristropha" => Some(NeumeRelation(Vector(Neutral, Neutral, Neutral)))
      case "trigon" => Some(NeumeRelation(Vector(Neutral, High, Low)))

      case "porrectus flexus" => Some(NeumeRelation(Vector(High, Low,High,Low)))


      case _ => None
    }
  }
}
