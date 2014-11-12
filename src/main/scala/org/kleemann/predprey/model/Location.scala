package org.kleemann.predprey.model

/**
 * A location, always in model coordinates; not device coordinates
 */
case class Location(val x: Double, y: Double) {
  def distance(that: Location): Double = {
    val a = this.x - that.x
    val b = this.y - that.y
    math.sqrt(a * a + b * b)
  }
  
}

object Location {
  /**
   * Checks whether two things are adjacent
   */
  def adjacent(d: Double, size1: Double, size2: Double) = d < (size1+size2)/2
}