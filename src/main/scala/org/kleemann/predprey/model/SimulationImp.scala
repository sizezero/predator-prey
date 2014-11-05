package org.kleemann.predprey.model

import scala.collection.mutable.MutableList

private[model] class SimulationImp(
  override val iteration: Int,
  override val width: Double,
  override val height: Double,
  var nextThing: Int,
  val rnd: scala.util.Random,
  val gs: List[Grass],
  val rs: List[Rabbit]) extends Simulation {

  // TODO: probably not safe to send this back with just a cast
  override val things = gs ++ rs

  def next: Simulation = {
    // grass to the right; rabbits to the left
    val nextGs: List[Grass] = gs map { _ match { case Grass(id, x, y) => Grass(id, x + 1, y) } }
    val nextRs: List[Rabbit] = rs map { _ match { case Rabbit(id, x, y) => Rabbit(id, x - 1, y) } }
    
    new SimulationImp(
      iteration + 1,
      width,
      height,
      nextThing,
      new scala.util.Random(rnd.self),
      nextGs,
      nextRs)
  }
}

object SimulationFactory {
  
  def random1: Simulation = {
    val width = 300.0
    val height = 200.0
    var id: Int = 0
    val rnd = new scala.util.Random()
    
    var gs: List[Grass] = List()
    for (i <- 1 to 10) {
      // need to play around with the list append syntax
      // doesn't work
      // l += i
      // does work but ugly
      // l = l :+ i
      // does work but ugly
      // l = i :: l
      gs = Grass(id, rnd.nextInt(width.toInt), rnd.nextInt(height.toInt)) :: gs
      id += 1
    }

    var rs: List[Rabbit] = List()
    for (i <- 1 to 10) {
      rs = Rabbit(id, rnd.nextInt(width.toInt), rnd.nextInt(height.toInt)) :: rs
      id += 1
    }

    new SimulationImp(0, width, height, id, rnd, gs, rs)
  }
    
}