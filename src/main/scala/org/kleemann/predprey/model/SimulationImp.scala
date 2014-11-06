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

    val rndNew = new scala.util.Random(rnd.self) 
    var newNextThing = nextThing

    // this is ugly; what's the good way to have auto incrementing serial ids in a functional environment
    def createGrass(loc: Location): Grass = {
      val g = Grass(newNextThing, loc)
      newNextThing += 1
      g
    }
    def makeBaby(parent: Rabbit): Rabbit = {
      val birthDistance = 5.0
      val loc = Location(parent.loc.x + rnd.nextDouble*birthDistance, parent.loc.y + rnd.nextDouble*birthDistance)
      val baby = new Rabbit(newNextThing, loc)
      newNextThing += 1
      baby
    }
    
    var eatenGrass: Set[Grass] = Set()
    var babyRabbits: List[Rabbit] = List()
    
    // rabbits move towards grass and eats it
    val nextRs: List[Rabbit] = rs map { r =>
      // if the rabbit is close then eat it; otherwise move towards it
      closestGrass(gs, r.loc) match {
        case Some((g, d)) => {
          if (adjacent(d)) {
            // TODO if grass has already been eaten then rabbit doesn't eat
            if (eatenGrass.contains(g)) r.didntEat
            else {
              // lots of side effects for code trying to be functional
              // maybe functional code is bad for simulations?
              eatenGrass += g
              babyRabbits = makeBaby(r) :: babyRabbits
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

    // grow new grass
    // partition into 10x10 sections
    val sparseGroup: Map[(Int, Int), List[Grass]] = nextGs.groupBy { g => ((g.loc.x/10).toInt*10, (g.loc.y/10).toInt*10) }
    
    // tried implement with flatMap and it got ugly; still pretty ugly
    val newGrass: Seq[Grass] = (for {
      j <- 0 until height.toInt by 10
      i <- 0 until width.toInt by 10
    } yield {
      val grassCount = sparseGroup.getOrElse( (i,j), List()).size
      val pct =  grassCount match {
        case 0 => 5
        case 1 => 20
        case 2 => 50
        case _ => 70
      }
      if (rnd.nextInt(100) < pct) List(createGrass(Location(i+rndNew.nextDouble*10, j+rndNew.nextDouble*10)))
      else List()
    }).flatten
          
    new SimulationImp(
      iteration + 1,
      width,
      height,
      nextThing,
      rndNew,
      nextGs ++ newGrass,
      nextRs ++ babyRabbits)
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