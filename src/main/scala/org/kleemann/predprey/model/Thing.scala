package org.kleemann.predprey.model

/**
 * A location, always in model coordinates; not device coordinates
 */
case class Location(val x: Double, y: Double)

/**
 * This is a thing that can exist within the simultion
 */
sealed trait Thing {
  def id: Int
  def loc: Location
}

object Thing {

  // not sure where the best place is for these
  
  def distance(loc1: Location, loc2: Location): Double = {
    val a = loc1.x - loc2.x
    val b = loc1.y - loc2.y
    math.sqrt(a * a + b * b)
  }

  def adjacent(dist: Double): Boolean = math.abs(dist) < 2.0

  // fairly dumb move algorithm: move dist 0.5 in both x and y directions towards target
  // will overshoot
  def moveTowards(loc1: Location, loc2: Location): Location = {
    val d = 1.0
    if (adjacent(distance(loc1, loc2))) loc1
    else Location(if (loc1.x > loc2.x) loc1.x - d else loc1.x + d, if (loc1.y > loc2.y) loc1.y - d else loc1.y + d)
  }
  
  // find closest grass (linear search)
  def closestGrass(gs: Seq[Grass], loc: Location): Option[Tuple2[Grass, Double]] =
    gs.foldLeft[Option[Tuple2[Grass, Double]]](None) { (o, g) => {
      val dNew = distance(loc, g.loc)
      o match {
        case Some((_, dOld)) =>
          if (dNew < dOld) Some(g, dNew)
          else o
        case None => Some((g, dNew))
      }
    }}
  
  // find closest rabbit (linear search)
  def closestRabbit(rs: Seq[Rabbit], loc: Location): Option[Tuple2[Rabbit, Double]] =
    rs.foldLeft[Option[Tuple2[Rabbit, Double]]](None) { (o, g) => {
      val dNew = distance(loc, g.loc)
      o match {
        case Some((_, dOld)) =>
          if (dNew < dOld) Some(g, dNew)
          else o
        case None => Some((g, dNew))
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
  
  def moveToward(target: Location): Rabbit = Rabbit(id, Thing.moveTowards(loc, target), fed)
  
  def isStarved: Boolean = fed <= 0
}

case class Wolf(
    override val id: Int,
    override val loc: Location,
    val fed: Int)
    extends Thing {
  
  def this (id: Int, loc: Location) {
    this(id, loc, 30)
  }
  
  def fullyFed: Wolf = Wolf(id, loc, 30)
  
  def didntEat: Wolf = Wolf(id, loc, fed-1)
  
  def moveToward(target: Location): Wolf = Wolf(id, Thing.moveTowards(loc, target), fed)
  
  def isStarved: Boolean = fed <= 0
}

