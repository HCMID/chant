package io.github.hcmid.chant

import org.scalatest.FlatSpec

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.latinmodel._

/**
*/
class NeumeRelationSpec extends FlatSpec {

  "The NeumeRelation object" should "find an optional NeumeRelation for a neume name" in {
    val pes = NeumeRelation(Vector(High))
    assert (NeumeRelation.relationForName("pes").get == pes)
  }
}
