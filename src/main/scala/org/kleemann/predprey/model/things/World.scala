package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

/**
 * <p>This is not a visible object that exists in the world but the behavior of this singleton allows us to handle general, global behavior
 */
class World(val id: Int, val nextGrassGrowth: Int) extends Thing {

  def this() {
    this(-1, Grass.GrowthRate)
  }
  
  val loc = Location(0, 0)
    
  override def setId(newId: Int) = new World(newId, nextGrassGrowth)
  
  override val size = 1.0 // not used
    
  // The world's behavior never changes
  def act(ms: List[Message], s: SimulationBuilder): World = {
    
    if (nextGrassGrowth>0) 
      return new World(id, nextGrassGrowth-1) 
      
    // grow some grass

    val gs: List[Grass] = s.ts.flatMap{ _ match {
      case g: Grass => List(g)
      case _ => Nil
    }}

    // TODO: think this is wrong; not 10x10 but checkerboard of square size 10x10
    
    // partition into 10x10 sections
    val sparseGroup: Map[(Int, Int), List[Grass]] = gs.groupBy { g => ((g.loc.x/10).toInt*10, (g.loc.y/10).toInt*10) }
    
    for {
      j <- 0 until s.height.toInt by 10
      i <- 0 until s.width.toInt by 10      
    } {
      val grassCount = sparseGroup.getOrElse( (i,j), List()).size
      if (s.rnd.nextDouble < World.grassCountToPercentage(grassCount)) s.birth(new Grass(Location(i+s.rnd.nextDouble*10, j+s.rnd.nextDouble*10)))
    }
    
    new World(id, Grass.GrowthRate)
  }
}

object World {
  
  /**
   * Given the number of grass in a region of the screen, what is the percentage that a new grass will grow?
   */
  private def grassCountToPercentage(count: Int): Double = count match {
    case 0 => 5
    case 1 => 20
    case 2 => 50
    case n: Int if n>10 => 0 // maximum grass growth
    case _ => 70
  }
}
