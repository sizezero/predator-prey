package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

/**
 * A piece of meat laying on the ground and rotting
 */
case class Meat(
    override val id: Int,
    override val loc: Location,
    val durability: Int)
    extends Thing {

  def this(loc: Location) {
    this(-1, loc, Meat.Durability)
  }
  
  def setId(newId: Int) = Meat(newId, loc, durability) 
  
  def age: Meat = if (durability==0) this else Meat(id, loc, durability-1)
  
  def act(ms: List[Message], s: SimulationBuilder): Meat = {
    val m = age
    if (durability==0) s.kill(this)
    m
  }
}

object Meat {
    val Durability = 10
}