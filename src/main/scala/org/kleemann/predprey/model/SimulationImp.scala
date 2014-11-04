package org.kleemann.predprey.model

import scala.collection.mutable.MutableList

class SimulationImp extends Simulation {
  
  // for now just populate this with some random data.  Consider moving 
  // this initialization to a mixin trait
  
  override val iteration = 0
  
  override val width = 300.0
  override val height = 200.0

  private var nextThing: Int = 0
  private val ts: MutableList[Thing] = MutableList()
  private val rnd = new scala.util.Random()
  
  for (i <- 1 to 10) {
    ts += Grass(nextThing, rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
    nextThing = nextThing + 1
  }
  
  for (i <- 1 to 10) {
    ts += Rabbit(nextThing, rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
    nextThing = nextThing + 1
  }

  // TODO: probably not safe to send this back with just a cast
  override def things = ts
}