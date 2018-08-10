package io.github.hcmid.chant

case class NeumeRelation(pitches: Vector[RelativePitch]) {

}

object NeumeRelation {

  def relationForName(s: String) : Option[NeumeRelation] = {
    /*
    pes, virga, torculus, punctum, tristropha, porrectus flexus, clivis, porrectus, trigon, distropha, quilisma
    */
    s match {
      case "pes" => Some(NeumeRelation(Vector(Neutral)))
      case _ => None
    }
  }
}
