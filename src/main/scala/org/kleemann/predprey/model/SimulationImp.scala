package org.kleemann.predprey.model

import scala.collection.mutable.MutableList
import Thing._

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
  override val things: List[Thing]) extends Simulation {

  def next: Simulation = {
    val sb = new SimulationBuilder(
        iteration,
        width,
        height,
        nextThing,
        rnd,
        things)

    sb.mkSimulation
  }
  
  /*
  def next2: Simulation = {

    val rndNew = new scala.util.Random(rnd.self) 
    var newNextThing = nextThing

    def makeWolf(parent: Wolf): Wolf = {
      val birthDistance = 5.0
      val loc = Location(parent.loc.x + rnd.nextDouble*birthDistance, parent.loc.y + rnd.nextDouble*birthDistance)
      val baby = new Wolf(newNextThing, loc)
      newNextThing += 1
      baby
    }
    
    var eatenRabbits: Set[Rabbit] = Set()
    var babyWolves: List[Wolf] = List()
    
    // Wolves move towards rabbits and eats them
    val nextWs: List[Wolf] = ws map { w =>
      // if the wolf is close then eat it; otherwise move towards it
      closest(rs, w.loc) match {
        case Some((r, d)) => {
          if (Location.adjacent(d)) {
            // TODO if rabbit has already been eaten then wolf doesn't eat
            if (eatenRabbits.contains(r)) w.didntEat
            else {
              // lots of side effects for code trying to be functional
              // maybe functional code is bad for simulations?
              eatenRabbits += r
              if (w.isPregnant) babyWolves = makeWolf(w) :: babyWolves
              w.fullyFed
            }
          }
          else w.moveToward(r.loc).didntEat
        }
        // no rabbit then the wolf doesn't move
        case None => w.didntEat
      }
    } filter { ! _.isStarved }

    // remove all eaten rabbits
    val nextRs = rs.filter{ !eatenRabbits.contains(_) }
    
    def makeRabbit(parent: Rabbit): Rabbit = {
      val birthDistance = 5.0
      val loc = Location(parent.loc.x + rnd.nextDouble*birthDistance, parent.loc.y + rnd.nextDouble*birthDistance)
      val baby = new Rabbit(newNextThing, loc)
      newNextThing += 1
      baby
    }
    
    var eatenGrass: Set[Grass] = Set()
    var babyRabbits: List[Rabbit] = List()
    
    // rabbits move towards grass and eats it
    val nextRs2: List[Rabbit] = nextRs map { r =>
      // if the rabbit is close then eat it; otherwise move towards it
      closest(gs, r.loc) match {
        case Some((g, d)) => {
          if (Location.adjacent(d)) {
            // TODO if grass has already been eaten then rabbit doesn't eat
            if (eatenGrass.contains(g)) r.didntEat
            else {
              // lots of side effects for code trying to be functional
              // maybe functional code is bad for simulations?
              eatenGrass += g
              babyRabbits = makeRabbit(r) :: babyRabbits
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

    // this is ugly; what's the good way to have auto incrementing serial ids in a functional environment
    def createGrass(loc: Location): Grass = {
      val g = Grass(newNextThing, loc)
      newNextThing += 1
      g
    }
    
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
      newNextThing,
      rndNew,
      nextGs ++ newGrass,
      nextRs2 ++ babyRabbits,
      nextWs ++ babyWolves)
  }
* 
*/
}

object SimulationFactory {

  // TODO: returning SimulationImp just for testing
  def random1: SimulationImp = {
    val width = 150.0
    val height = 100.0
    var id: Int = 0
    val rnd = new scala.util.Random()

    var ts: List[Thing] = List()
    
    for (i <- 1 to 50) {
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

    for (i <- 1 to 20) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      ts = new Rabbit(loc).setId(id) :: ts
      id += 1
    }

    for (i <- 1 to 20) {
      val loc = Location(rnd.nextInt(width.toInt), rnd.nextInt(height.toInt))
      ts = new Wolf(loc).setId(id) :: ts
      id += 1
    }

    ts = new World(id) :: ts
    id += 1
    
    new SimulationImp(0, width, height, id, rnd, ts)
  }

}
