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


  it should "create a new NeumeRelation by adding a second NeumeRelation" in {
    val nr1 = NeumeRelation(Vector(Neutral))
    val nr2 = NeumeRelation(Vector(Neutral, High, Low))
    val expected = NeumeRelation(Vector(Neutral,Neutral,High,Low))
    val newNR = nr1 ++ nr2
    assert(newNR == expected)
  }

  it should "create a new NeumeRelation by adding a Vector of NeumeRelations" in {
    val nr1 = NeumeRelation(Vector(Neutral))
    val nrV = Vector(
      NeumeRelation(Vector(Neutral)),
      NeumeRelation(Vector(High)),
      NeumeRelation(Vector(Low))
    )
    val expected = NeumeRelation(Vector(Neutral,Neutral,High,Low))
    val newNR = nr1 ++ nrV
    assert(newNR == expected)
  }

}
