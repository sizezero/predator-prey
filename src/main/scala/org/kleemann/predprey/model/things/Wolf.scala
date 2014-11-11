package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

case class Wolf(
    override val id: Int,
    override val loc: Location,
    val fed: Int,
    val nextBirth: Int)
    extends Thing {

  import Wolf._
  
  def this (loc: Location) {
    // not sure why the above import does not allow the use of plain "Full" here
    this(-1, loc, Wolf.Full, Wolf.BirthDelay)
  }
  
  def setId(newId: Int) = Wolf(newId, loc, fed, nextBirth) 
  
  def isPregnant: Boolean = nextBirth<=0
  
  def fullyFed: Wolf = Wolf(id, loc, Wolf.Full, if (isPregnant) 5 else nextBirth-1)
  
  def didntEat: Wolf = Wolf(id, loc, fed-1, nextBirth)
  
  def moveToward(target: Location): Wolf = Wolf(id, loc.moveTowards(target, Wolf.MoveDistance), fed, nextBirth)
  
  def isStarved: Boolean = fed <= 0
  
  // A wolfe's behavior never changes
  def act(ms: List[Message], s: SimulationBuilder): Wolf = {
    
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
                val babyLoc = Location(loc.x + s.rnd.nextDouble*BirthDistance, loc.y + s.rnd.nextDouble*BirthDistance)
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
  
object Wolf {
  val Full = 30
  val BirthDelay = 10
  val MoveDistance = 1.5
  val BirthDistance = 5.0
}

