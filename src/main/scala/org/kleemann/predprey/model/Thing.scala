package org.kleemann.predprey.model

/**
 * This is a thing that can exist within the simultion
 */
sealed trait Thing {

  def id: Int
  def x: Double
  def y: Double
}

// lets try these with case classes; this will put most of the logic 
// in simulation

case class Grass(
    override val id: Int,
    override val x: Double,
    override val y: Double)
    extends Thing

case class Rabbit(
    override val id: Int,
    override val x: Double,
    override val y: Double)
    extends Thing

