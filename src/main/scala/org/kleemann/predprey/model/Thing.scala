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

/**
 * This is a thing that can exist within the simulation
 */
sealed trait Thing {
  
  /**
   * Every thing in a simulation has a unique id
   */
  def id: Int
  
  def loc: Location
}

/**
 * Various functions--not sure where the best place is for these  
 */
object Thing {

  /**
   * Find the closest thing (linear search)
   */
  def closest[T <: Thing](ts: Seq[T], loc: Location): Option[Tuple2[T, Double]] =
    ts.foldLeft[Option[Tuple2[T, Double]]](None) { (o, t) => {
      val dNew = loc distance t.loc
      o match {
        case Some((_, dOld)) =>
          if (dNew < dOld) Some(t, dNew)
          else o
        case None => Some((t, dNew))
      }
    }}

}

// lets try these with case classes; this will put most of the logic 
// in simulation

case class Grass(
    override val id: Int,
    override val loc: Location)
    extends Thing

case class Rabbit(
    override val id: Int,
    override val loc: Location,
    val fed: Int)
    extends Thing {
  
  def this (id: Int, loc: Location) {
    this(id, loc, 10)
  }
  
  def fullyFed: Rabbit = Rabbit(id, loc, 10)
  
  def didntEat: Rabbit = Rabbit(id, loc, fed-1)
  
  def moveToward(target: Location): Rabbit = Rabbit(id, loc.moveTowards(target, 1.0), fed)
  
  def isStarved: Boolean = fed <= 0
}

case class Wolf(
    override val id: Int,
    override val loc: Location,
    val fed: Int,
    val nextBirth: Int)
    extends Thing {
  
  def this (id: Int, loc: Location) {
    this(id, loc, 30, 5)
  }
  
  def isPregnant: Boolean = nextBirth<=0
  
  def fullyFed: Wolf = Wolf(id, loc, 30, if (isPregnant) 5 else nextBirth-1)
  
  def didntEat: Wolf = Wolf(id, loc, fed-1, nextBirth)
  
  def moveToward(target: Location): Wolf = Wolf(id, loc.moveTowards(target, 1.5), fed, nextBirth)
  
  def isStarved: Boolean = fed <= 0
}

