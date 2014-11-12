package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

case class Rabbit(
    override val id: Int,
    override val loc: Location,
    val fed: Int)
    extends Thing {
  
  def this (loc: Location) {
    this(-1, loc, Rabbit.Full)
  }
  
  override def setId(newId: Int) = Rabbit(newId, loc, fed)

  override val size = 1.0
  
  def fullyFed: Rabbit = Rabbit(id, loc, Rabbit.Full)
  
  def didntEat: Rabbit = Rabbit(id, loc, fed-1)
  
  def moveToward(target: Thing): Rabbit = Rabbit(id, toward(target, Rabbit.MoveDistance), fed)
  
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
          if (adjacent(g)) {
            if (s.kill(g)) {
              if (s.rnd.nextDouble < Rabbit.ChanceOfBaby) {
                // make baby rabbit
                val babyLoc = Location(loc.x + s.rnd.nextDouble*Rabbit.BirthDistance, loc.y + s.rnd.nextDouble*Rabbit.BirthDistance)
                s.birth(new Rabbit(babyLoc))
              }
              fullyFed
            }
            else didntEat
          }
          else moveToward(g).didntEat
        }
        case None => didntEat
      }
    }
  } 
}

object Rabbit {
  private val conf = com.typesafe.config.ConfigFactory.load()
  
  val Size = conf.getDouble(s"rabbit.size")
  val Full = conf.getInt(s"rabbit.full")
  val MoveDistance = conf.getDouble(s"rabbit.move-distance")
  val ChanceOfBaby = conf.getDouble(s"rabbit.chance-of-baby")
  val BirthDistance = conf.getDouble(s"rabbit.birth-distance")
}

