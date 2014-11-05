package org.kleemann.predprey.model

/**
 * This is the interface needed by the UI in order to use a map.
 * Perhaps a better name for this would be simulation?
 */
abstract class Simulation {

  /**
   * A serial number that is incremented each time the simulation is advanced.
   */
  val iteration: Int
  
  /**
   * Returns the next value in the simulation.  TODO: may need a randomization
   * State.
   */
  def next: Simulation

  /**
   * The width in simulation units.  TODO: We should define what this means in real/word terms
   */
  val width: Double
  val height: Double
  
  /**
   * Iterate over everything.  We may want to filter by type.
   */
  def things: Seq[Thing]
  
}