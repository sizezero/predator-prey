package org.kleemann.predprey.model

import scala.collection.mutable.MutableList

class SimulationImp(
    override val iteration: Int,
    var nextThing: Int,
    val rnd: scala.util.Random,
    val ts: MutableList[Thing]
    ) extends Simulation {
  
  def this() {
    this(0, 0, new scala.util.Random(), MutableList())
  }
  
  // for now just populate this with some random data.  Consider moving 
  // this initialization to a mixin trait
  
  override val width = 300.0
  override val height = 200.0

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
  
  def next: Simulation = {
    // TODO: move all things one to the right
    this
  }
}