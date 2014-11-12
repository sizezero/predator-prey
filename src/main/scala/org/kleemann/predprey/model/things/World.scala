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

    // partition into sections of size 20x20
    val sparseGroup: Map[(Int, Int), List[Grass]] = gs.groupBy { g => 
      ((g.loc.x/Grass.GrowthGridSize).toInt*Grass.GrowthGridSize, (g.loc.y/Grass.GrowthGridSize).toInt*Grass.GrowthGridSize)
    }
    
    for {
      j <- 0 until s.height.toInt by Grass.GrowthGridSize
      i <- 0 until s.width.toInt by Grass.GrowthGridSize      
    } {
      val grassCount = sparseGroup.getOrElse( (i,j), Nil).size
      if (s.rnd.nextDouble < Grass.countToPercentage(grassCount))
        s.birth(new Grass(Location(i+s.rnd.nextDouble*Grass.GrowthGridSize, j+s.rnd.nextDouble*Grass.GrowthGridSize)))
    }
    
    new World(id, Grass.GrowthRate)
  }
}
