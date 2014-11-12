package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

case class Grass(
    override val id: Int,
    override val loc: Location)
    extends Thing {
  
  def this (loc: Location) {
    this(-1, loc)
  }
  
  def setId(newId: Int) = Grass(newId, loc)
  
  override val size = Grass.Size
  
  // A grass's behavior never changes
  def act(ms: List[Message], s: SimulationBuilder): Grass = {
    // a single grass does nothing
    this
  } 
}

object Grass {
  private val conf = com.typesafe.config.ConfigFactory.load()

  val Size = conf.getDouble(s"grass.size")
  
  // check for growth after this many iterations
  val GrowthRate = conf.getInt(s"grass.growth-rate")
  
  val GrowthGridSize = conf.getInt(s"grass.growth-grid-size")
  
  def countToPercentage(count: Int): Double = count match {
    case 0 => 5
    case 1 => 20
    case 2 => 50
    case n: Int if n>10 => 0 // maximum grass growth
    case _ => 70
  }

}