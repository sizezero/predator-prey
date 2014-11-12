package org.kleemann.predprey.model

trait Message

/**
 * Generic attack message.  This could be made more specific (e.g. Bite, Stab) if we need it
 */
object Attack extends Message

/**
 * This is a thing that can exist within the simulation
 */
trait Thing {
  
  /**
   * Every thing in a simulation has a unique id
   */
  def id: Int
  
  // TODO: make this polymorphic
  def setId(newId: Int): Thing 
  
  def loc: Location

  def isInSimulation = id != -1
  
  def act(ms: List[Message], s: SimulationBuilder): Thing

  def size: Double
  
  def adjacent(that: Thing): Boolean = {
    val span = (this.size + that.size) / 2
    math.abs(this.loc.x - that.loc.x) < span && math.abs(this.loc.y - that.loc.y) < span 
  }

  // fairly dumb move algorithm: move dist 0.5 in both x and y directions towards target
  // will overshoot.  Size is the total size of both objects.
  def toward(that: Thing, dist: Double): Location = {
    if (adjacent(that)) this.loc
    else Location(
        if (this.loc.x > that.loc.x) this.loc.x - dist else this.loc.x + dist,
        if (this.loc.y > that.loc.y) this.loc.y - dist else this.loc.y + dist)
  }
    
  /**
   * Add some newlines and indentation to make the objects easier to read
   */
  def prettyPrint: String = {
    // it would be interesting to see this functionally instead of imperatively
    // I would imagine lots of objects would be generated in order to handle indents
    var indent = 0
    val sb = new StringBuilder() 
    for (c <- toString) {
      c match {
        case '(' => {
          sb.append("(\n")
          indent += 2
          sb.append(" " * indent)
        }
        case ')' => {
          sb.append(")\n")
          indent -= 2
          sb.append(" " * indent)
        }
        case ',' => {
          sb.append(",\n")
          sb.append(" " * indent)
        }
        case _ => sb.append(c)
      }
    }
    sb.toString
  }
  
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

/**
 * <p>Behavior can modify the passed Simulation in any way. 
 * Takes a particular Thing, SimulationBuilder, and messages passed to this thing
 * returns a new thing and a list of messages to send to other things
 */
trait Behavior[A <: Thing] {
  
  def act(a: A, ms: List[Message], s: SimulationBuilder): A  
}
