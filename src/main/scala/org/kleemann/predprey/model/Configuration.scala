package org.kleemann.predprey.model

/**
 * The configuration for a single run of the simulation
 */
trait Configuration {
  def SimulationRandomSeed: Int
  def SimulationWidth: Double
  def SimulationHeight: Double
}