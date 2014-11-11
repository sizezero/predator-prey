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
  
  // A grass's behavior never changes
  def act(ms: List[Message], s: SimulationBuilder): Grass = {
    // a single grass does nothing
    this
  } 
}
