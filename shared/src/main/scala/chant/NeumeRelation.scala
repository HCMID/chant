package io.github.hcmid.chant

case class NeumeRelation(pitches: Vector[RelativePitch]) {
  def ++ (nr: NeumeRelation): NeumeRelation = {
    NeumeRelation(pitches ++ nr.pitches)
  }
}

object NeumeRelation {

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
