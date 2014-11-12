package org.kleemann.predprey.model

import scala.collection.mutable.MutableList
import things._

/**
 * <p>An immutable implementation of Simulation
 * 
 * <p>I can't find a way to deep copy a random number so this class is not completely encapulated
 */
private[model] class SimulationImp(
  override val iteration: Int,
  override val width: Double,
  override val height: Double,
  val nextThing: Int,
  val rnd: scala.util.Random,
  override val things: List[Thing],
  val ms: Map[Int, List[Message]]) extends Simulation {

  def next: Simulation = {
    val sb = new SimulationBuilder(
        iteration,
        width,
        height,
        nextThing,
        rnd,
        things,
        ms)

    sb.mkSimulation
  }
  
}

object SimulationFactory {

  private val conf = com.typesafe.config.ConfigFactory.load()
  
  // TODO: returning SimulationImp just for testing
  def random1: SimulationImp = {
    
    val pre = "init.random1"
    val init = "random1"
    val width = conf.getDouble(s"$pre.width")
    val height = conf.getDouble(s"$pre.height")
    var id: Int = 0
    val rnd = new scala.util.Random()

    var ts: List[Thing] = List()
    
    for (i <- 1 to conf.getInt(s"$pre.grass")) {
      // need to play around with the list append syntax
      // doesn't work
      // l += i
      // does work but ugly
      // l = l :+ i
      // does work but ugly
      // l = i :: l
      ts = new Grass(Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))).setId(id) :: ts
      id += 1
    }

    for (i <- 1 to conf.getInt(s"$pre.rabbit")) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      ts = new Rabbit(loc).setId(id) :: ts
      id += 1
    }

    for (i <- 1 to conf.getInt(s"$pre.wolf")) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      ts = new Wolf(loc).setId(id) :: ts
      id += 1
    }

    ts = new World(id) :: ts
    id += 1
    
    new SimulationImp(0, width, height, id, rnd, ts, Map())
  }

  def small1: SimulationImp = {
    val width = 10.0
    val height = 10.0
    var id: Int = 0
    val rnd = new scala.util.Random()

    var ts: List[Thing] = List()
    
    for (i <- 1 to 10) {
      // need to play around with the list append syntax
      // doesn't work
      // l += i
      // does work but ugly
      // l = l :+ i
      // does work but ugly
      // l = i :: l
      ts = new Grass(Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))).setId(id) :: ts
      id += 1
    }

    for (i <- 1 to 4) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      ts = new Rabbit(loc).setId(id) :: ts
      id += 1
    }

    for (i <- 1 to 1) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      ts = new Wolf(loc).setId(id) :: ts
      id += 1
    }

    ts = new World(id) :: ts
    id += 1
    
    new SimulationImp(0, width, height, id, rnd, ts, Map())
  }

}
