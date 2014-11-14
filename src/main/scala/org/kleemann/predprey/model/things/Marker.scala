package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

/**
 * A special class that is never visible, and never exists on the map.
 * Currently it is just used to be a target placeholder.
 */
case class Marker(
    val id: Int,
    val loc: Location)
    extends Thing {

  override def setId(newId: Int): Thing = Marker(newId, loc) 

  override val size = 0.1
  
  def act(ms: List[Message], s: SimulationBuilder): Thing = this
}