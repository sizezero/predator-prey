package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

/**
 * <p>This object allows us to handle general, global behavior
 */
case class World(val id: Int) extends Thing {
    
  val loc = Location(0, 0)
    
  def setId(newId: Int) = new World(newId) 
    
  // The world's behavior never changes
  def act(ms: List[Message], s: SimulationBuilder): World = {
    
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
