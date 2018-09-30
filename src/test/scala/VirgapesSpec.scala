package edu.holycross.shot.mid.chant
import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

class VirgapesSpec extends FlatSpec {


  val neumesDoc = CtsUrn("urn:cts:chant:massordinary.sg359.neumes:")
  val ortho = Virgapes(neumesDoc)

  "A Virgapes orthography" should "have a label" in {
    assert(ortho.orthography == "Virgapes")
  }

  it should "recognize its domain" in {
    assert(ortho.inDomain(CtsUrn("urn:cts:chant:massordinary.sg359.neumes:")))

    assert(ortho.inDomain(CtsUrn("urn:cts:chant:massordinary.sg359.text:")) == false)

  }


  it should "accept digits, white space, periods and hyphens" in {
    assert (ortho.validString("1"))
    assert(ortho.validString("1.0  - 1.1"))
  }


  it should "identify classes of tokens recognizable from this orthography" in {
    val tokenTypes = ortho.tokenCategories
    assert(tokenTypes.toSet == Set(NeumeToken, PunctuationToken))
  }

  it should "tokenize plain-text string for a single neume" in {
    val oneNeume = "1.0.1.0"
    val expectedOne = Vector( MidToken(oneNeume, Some(NeumeToken)))
    assert (ortho.tokenizeString(oneNeume) == expectedOne)
  }

  it should "tokenize plain-text string for a hyphen" in {
    val hyphen = "-"
    val expected = Vector( (MidToken(hyphen, Some(PunctuationToken))))
    assert (ortho.tokenizeString(hyphen) == expected)
  }

  it should "tokenize multiple neumes" in {
    val twoNeumes = "1.0.1.0 1.0.0.0"
    val expected = Vector(
      MidToken("1.0.1.0", Some(NeumeToken)),
      MidToken("1.0.0.0", Some(NeumeToken))
    )
    assert (ortho.tokenizeString(twoNeumes) == expected)
  }

  it should "tokenize multiple neumes including multiple neumes on one syllable" in {
    val twoNeumes = "1.0.1.0-2.0.0.0 1.0.0.0"
    val expected = Vector(
      MidToken("1.0.1.0", Some(NeumeToken)),
      MidToken("-", Some(PunctuationToken)),
      MidToken("2.0.0.0", Some(NeumeToken)),
      MidToken("1.0.0.0", Some(NeumeToken))
    )
    assert (ortho.tokenizeString(twoNeumes) == expected)
  }
}
