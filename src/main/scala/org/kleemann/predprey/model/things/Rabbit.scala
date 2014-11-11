package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

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
  def act(ms: List[Message], s: SimulationBuilder): Rabbit = {
     // don't differentiate between one or more attacks
     val isAttacked = ms.exists{ _ == Attack }
    
    if (isStarved || isAttacked) {
      s.kill(this)
      s.birth(new Meat(loc))
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
            if (s.kill(g)) {
              if (s.rnd.nextInt(100) < 25) { // 25% chance of a baby
                // make baby rabbit
                val birthDistance = 5.0
                val babyLoc = Location(loc.x + s.rnd.nextDouble*birthDistance, loc.y + s.rnd.nextDouble*birthDistance)
                s.birth(new Rabbit(babyLoc))
              }
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

