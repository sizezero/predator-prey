package org.kleemann.predprey.model

import scala.collection.mutable.MutableList
import Thing._

private[model] class SimulationImp(
  override val iteration: Int,
  override val width: Double,
  override val height: Double,
  var nextThing: Int,
  val rnd: scala.util.Random,
  val gs: List[Grass],
  val rs: List[Rabbit]) extends Simulation {

  override val things = gs ++ rs

  def next: Simulation = {

    var eatenGrass: Set[Grass] = Set()
    
    // rabbits move towards grass and eats it
    val nextRs: List[Rabbit] = rs map { r =>
      // if the rabbit is close then eat it; otherwise move towards it
      closestGrass(gs, r.loc) match {
        case Some((g, d)) => {
          if (adjacent(d)) {
            // TODO if grass has already been eaten then rabbit doesn't eat
            if (eatenGrass.contains(g)) r.didntEat
            else {
              eatenGrass += g
              r.fullyFed
            }
          }
          else r.moveToward(g.loc).didntEat
        }
        // no grass then the rabbit doesn't move
        case None => r.didntEat
      }
    } filter { ! _.isStarved }

    // remove all eaten grass
    val nextGs = gs.filter{ !eatenGrass.contains(_) }

    val rndNew = new scala.util.Random(rnd.self) 
    var newNextThing = nextThing
    
    // grow new grass
    // partition into 10x10 sections
    val p: Map[(Int, Int), List[Grass]] = nextGs.groupBy { g => ((g.loc.x/10).toInt*10, (g.loc.y/10).toInt*10) }
    // each partition may generate a new Grass depending on how many grasses are in the partition
    // TODO: we're not trying the partitions that are zero
    val newGrass = p.flatMap{ case (loc: (Int,Int), gs: List[Grass]) => {
      val pct =  gs.size match {
        case 0 => 5
        case 1 => 20
        case 2 => 50
        case _ => 70
      }
      if (rnd.nextInt(100) < pct) {
         val l = List(Grass(newNextThing, Location((loc._1+rndNew.nextInt(10)).toDouble, (loc._2+rndNew.nextInt(10)).toDouble)))
         newNextThing += 1
         l
      } else List()
    }}
    
    new SimulationImp(
      iteration + 1,
      width,
      height,
      nextThing,
      rndNew,
      nextGs ++ newGrass,
      nextRs)
  }
}

object SimulationFactory {

  // TODO: returning SimulationImp just for testing
  def random1: SimulationImp = {
    val width = 150.0
    val height = 100.0
    var id: Int = 0
    val rnd = new scala.util.Random()

    var gs: List[Grass] = List()
    for (i <- 1 to 50) {
      // need to play around with the list append syntax
      // doesn't work
      // l += i
      // does work but ugly
      // l = l :+ i
      // does work but ugly
      // l = i :: l
      gs = Grass(id, Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))) :: gs
      id += 1
    }

    var rs: List[Rabbit] = List()
    for (i <- 1 to 20) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      rs = new Rabbit(id, loc) :: rs
      id += 1
    }

    new SimulationImp(0, width, height, id, rnd, gs, rs)
  }

}