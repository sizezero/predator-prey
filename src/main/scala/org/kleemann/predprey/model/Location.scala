package org.kleemann.predprey.model

/**
 * A location, always in model coordinates; not device coordinates
 */
case class Location(val x: Double, y: Double) {
  /**
   * Returns the distance squared between two locations.
   * Can be more efficient when doing comparisons.
   */
  def distance2(that: Location): Double = {
    val a = this.x - that.x
    val b = this.y - that.y
    a * a + b * b
  }
  
  def distance(that: Location): Double = math.sqrt(distance2(that))
}

object Location {
  /**
   * Checks whether two things are adjacent
   */
  def adjacent(d: Double, size1: Double, size2: Double) = d < (size1+size2)/2
}