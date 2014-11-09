package org.kleemann.predprey.model

/**
 * This is a thing that can exist within the simulation
 */
sealed trait Thing {
  
  /**
   * Every thing in a simulation has a unique id
   */
  def id: Int
  
  // TODO: make this polymorphic
  def setId(newId: Int): Thing 
  
  def loc: Location

  def isInSimulation = id != -1
  
  type Behavior[A <: Thing] = SimulationBuilder => A
  
  /**
   * Behavior can modify the passed Simulation in any way.  Returns itself 
   * which may be modified
   */
  val behavior: Behavior[Thing]
  
  /**
   * Add some newlines and indentation to make the objects easier to read
   */
  def prettyPrint: String = {
    // it would be interesting to see this functionally instead of imperatively
    // I would imagine lots of objects would be generated in order to handle indents
    var indent = 0
    val sb = new StringBuilder() 
    for (c <- toString) {
      c match {
        case '(' => {
          sb.append("(\n")
          indent += 2
          sb.append(" " * indent)
        }
        case ')' => {
          sb.append(")\n")
          indent -= 2
          sb.append(" " * indent)
        }
        case ',' => {
          sb.append(",\n")
          sb.append(" " * indent)
        }
        case _ => sb.append(c)
      }
    }
    sb.toString
  }
  
}

/**
 * Various functions--not sure where the best place is for these  
 */
object Thing {

  /**
   * Find the closest thing (linear search)
   */
  def closest[T <: Thing](ts: Seq[T], loc: Location): Option[Tuple2[T, Double]] =
    ts.foldLeft[Option[Tuple2[T, Double]]](None) { (o, t) => {
      val dNew = loc distance t.loc
      o match {
        case Some((_, dOld)) =>
          if (dNew < dOld) Some(t, dNew)
          else o
        case None => Some((t, dNew))
      }
    }}

}

// lets try these with case classes; this will put most of the logic 
// in simulation

case class Grass(
    override val id: Int,
    override val loc: Location)
    extends Thing {
  
  def this (loc: Location) {
    this(-1, loc)
  }
  
  def setId(newId: Int) = Grass(newId, loc)
  
  // A grass's behavior never changes
  val behavior: Behavior[Grass] = (s: SimulationBuilder) => {
    // a single grass does nothing
    this
  } 
}

case class Rabbit(
    override val id: Int,
    override val loc: Location,
    val fed: Int)
    extends Thing {
  
  def this (loc: Location) {
    this(-1, loc, 10)
  }
  
  def setId(newId: Int) = Rabbit(newId, loc, fed)
  
  def fullyFed: Rabbit = Rabbit(id, loc, 10)
  
  def didntEat: Rabbit = Rabbit(id, loc, fed-1)
  
  def moveToward(target: Location): Rabbit = Rabbit(id, loc.moveTowards(target, 1.0), fed)
  
  def isStarved: Boolean = fed <= 0
  
  // A rabbit's behavior never changes
  val behavior: Behavior[Rabbit] = (s: SimulationBuilder) => {
    // if we have multiple behaviors, we may want to plug them into each other. 
    //e.g. check for starvation before or after other behaviors
    if (isStarved) {
      s.kill(this)
      this
    } else { 
      // TODO: very inefficient to filter this for every Rabbit
      val gs = s.ts.filter{ _ match {
        case _: Grass => true
        case _ => false
      }}
    
      // rabbits eat close grass or move towards the nearest grass
      Thing.closest(gs, loc) match {
        case Some((g, d)) => {
          if (Location.adjacent(d)) {
            // TODO if rabbit has already been eaten then wolf doesn't eat
            if (s.kill(g)) {
              // make baby rabbit
              val birthDistance = 5.0
              val babyLoc = Location(loc.x + s.rnd.nextDouble*birthDistance, loc.y + s.rnd.nextDouble*birthDistance)
              s.birth(new Rabbit(babyLoc))
              fullyFed
            }
            else didntEat
          }
          else moveToward(g.loc).didntEat
        }
        case None => didntEat
      }
    }
  } 
}

case class Wolf(
    override val id: Int,
    override val loc: Location,
    val fed: Int,
    val nextBirth: Int)
    extends Thing {
  
  def this (loc: Location) {
    this(-1, loc, 30, 5)
  }
  
  def setId(newId: Int) = Wolf(newId, loc, fed, nextBirth) 
  
  def isPregnant: Boolean = nextBirth<=0
  
  def fullyFed: Wolf = Wolf(id, loc, 30, if (isPregnant) 5 else nextBirth-1)
  
  def didntEat: Wolf = Wolf(id, loc, fed-1, nextBirth)
  
  def moveToward(target: Location): Wolf = Wolf(id, loc.moveTowards(target, 1.5), fed, nextBirth)
  
  def isStarved: Boolean = fed <= 0
  
  // A wolfe's behavior never changes
  val behavior: Behavior[Wolf] = (s: SimulationBuilder) => {
    
    if (isStarved) {
      s.kill(this)
      this
    } else { 
      // TODO: very inefficient to filter this for every Wolf
      val rs = s.ts.filter{ _ match {
        case _: Rabbit => true
        case _ => false
      }}
    
      // wolves eat close rabbits or move towards the nearest rabbit
      Thing.closest(rs, loc) match {
        case Some((r, d)) => {
          if (Location.adjacent(d)) {
            if (s.kill(r)) {
              if (isPregnant) {
                // make baby wolf
                val birthDistance = 5.0
                val babyLoc = Location(loc.x + s.rnd.nextDouble*birthDistance, loc.y + s.rnd.nextDouble*birthDistance)
                s.birth(new Wolf(babyLoc))
              }
              fullyFed
            }
            else didntEat
          }
          else moveToward(r.loc).didntEat
        }
        case None => didntEat
      }
    }
  }
}
  
  /**
   * <p>This object allows us to handle general, global behavior
   */
  case class World(val id: Int) extends Thing {
    
  val loc = Location(0, 0)
    
  def setId(newId: Int) = new World(newId) 
    
  // The world's behavior never changes
  val behavior: Behavior[World] = (s: SimulationBuilder) => {
    
    // grow some grass

    val gs = s.ts.flatMap{ _ match {
      case g: Grass => List(g)
      case _ => List()
    }}

    // TODO: think this is wrong; not 10x10 but checkerboard of square size 10x10
    
    // partition into 10x10 sections
    val sparseGroup: Map[(Int, Int), List[Grass]] = gs.groupBy { g => ((g.loc.x/10).toInt*10, (g.loc.y/10).toInt*10) }
    
    for {
      j <- 0 until s.height.toInt by 10
      i <- 0 until s.width.toInt by 10      
    } {
      val grassCount = sparseGroup.getOrElse( (i,j), List()).size
      val pct =  grassCount match {
        case 0 => 5
        case 1 => 20
        case 2 => 50
        case n: Int if n>10 => 0 // maximum grass growth
        case _ => 70
      }
      if (s.rnd.nextInt(100) < pct) s.birth(new Grass(Location(i+s.rnd.nextDouble*10, j+s.rnd.nextDouble*10)))
    }
    
    this
  }
}

