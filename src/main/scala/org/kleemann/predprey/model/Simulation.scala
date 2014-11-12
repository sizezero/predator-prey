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
   * Returns the next value in the simulation.
   */
  def next: Simulation

  /**
   * The width/height in simulation units.  
   * 1 unit == 10 feet at a close up view.
   * At a mini-map view this is distorted as towns will end up being much closer than normal.
   */
  val width: Double
  val height: Double
  
  /**
   * Iterate over every object in the simulation.
   */
  def things: Seq[Thing]
  
}