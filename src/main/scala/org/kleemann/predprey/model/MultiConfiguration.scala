package org.kleemann.predprey.model

/**
 * A wrapper around the typesafe config class that allows values 
 * to be expressed as either 
 */
class MultiConfiguration {

  case class CIterator {
    val SimulationRandomSeed: Range[Int]
    
  }
  
  
}