package org.kleemann.predprey.model

import scala.util.parsing.combinator._

/**
 * A wrapper around the typesafe config class that allows values 
 * to be expressed as either 
 */
class MultiConfiguration {

  class CIterator {
    
    
  }
  
  
}

// probably shouldn't be at this level
class IntParser extends JavaTokenParsers {
  def expr = range
  def range = term~opt(","~term)
  def term = wholeNumber
}

object MultiConfiguration {
  val ints = Set(
    "simulation-random-seed"
  )
  val doubles = Set(
    "simulation-width",
    "simulation-height"
  )
  def parse(resource: String) {
    val is = MultiConfiguration.getClass().getResourceAsStream(resource)
    val sr = scala.util.parsing.input.StreamReader(new java.io.InputStreamReader(is))
    val p = new IntParser()  
    p.parseAll(p.expr, sr)
  }
}