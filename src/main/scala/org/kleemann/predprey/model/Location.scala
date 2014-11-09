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
  
  // fairly dumb move algorithm: move dist 0.5 in both x and y directions towards target
  // will overshoot
  def moveTowards(that: Location, dist: Double): Location = {
    if (Location.adjacent(this distance that)) this
    else Location(if (this.x > that.x) this.x - dist else this.x + dist, if (this.y > that.y) this.y - dist else this.y + dist)
  }
}

object Location {
  def adjacent(distance: Double): Boolean = math.abs(distance) < 2.0
}
