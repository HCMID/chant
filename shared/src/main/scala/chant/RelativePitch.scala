package io.github.hcmid.chant

trait RelativePitch {
  def label: String
}

object Neutral extends RelativePitch {
  def label = "Neutral"
}
object High extends RelativePitch {
  def label = "High"
}
object Low extends RelativePitch {
  def label = "Low"
}
