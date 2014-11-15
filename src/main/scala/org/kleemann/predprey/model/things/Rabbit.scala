package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

case class Rabbit(
    override val id: Int,
    override val loc: Location,
    val fed: Int,
    val nextBirth: Int,
    private val behavior: Behavior[Rabbit])
    extends Thing {
  
  def this (loc: Location) {
    this(-1, loc, Rabbit.Full, Rabbit.BirthDelay, Rabbit.Wandering)
  }
  
  override def setId(newId: Int) = Rabbit(newId, loc, fed, nextBirth, behavior)

  override val size = Rabbit.Size
  
  def isPregnant: Boolean = nextBirth<=0

  def fullyFed: Rabbit = Rabbit(id, loc, Rabbit.Full, if (isPregnant) Rabbit.BirthDelay else nextBirth-1, Rabbit.Wandering)
  
  def didntEat: Rabbit = Rabbit(id, loc, fed-1, nextBirth, behavior)
  
  def moveToward(target: Thing): Rabbit = Rabbit(id, toward(target, Rabbit.MoveDistance), fed, nextBirth, behavior)
  
  def wander: Rabbit = Rabbit(id, loc, fed, nextBirth, Rabbit.Wandering)
  
  def chase(target: Thing): Rabbit = Rabbit(id, loc, fed, nextBirth, new Rabbit.Chasing(target))
  
  def isStarved: Boolean = fed <= 0

  // Rabbit has dynamic behavior but check for starvation first
  def act(ms: List[Message], s: SimulationBuilder): Rabbit = {
    val isAttacked = ms.exists{ _ == Attack }
    if (isStarved || isAttacked) {
      s.kill(this)
      s.birth(new Meat(loc))
      this
    } else behavior.act(this, ms, s)
  }
}

object Rabbit {
  private val conf = com.typesafe.config.ConfigFactory.load()
  
  val Size = conf.getDouble(s"rabbit.size")
  val Full = conf.getInt(s"rabbit.full")
  val Hungry = conf.getInt(s"rabbit.hungry")
  val MoveDistance = conf.getDouble(s"rabbit.move-distance")
  val BirthDelay = conf.getInt(s"rabbit.birth-delay")
  val BirthDistance = conf.getDouble(s"rabbit.birth-distance")
  val WolfFearDistance = conf.getDouble(s"rabbit.wolf-fear-distance")
  val SightDistance =  conf.getDouble(s"rabbit.sight-distance")
  val WanderRange =  conf.getDouble(s"rabbit.wander-range")
  
  object Wandering extends Behavior[Rabbit] {
    
    override def toString = "Wandering"
    
    def act(r: Rabbit, ms: List[Message], s: SimulationBuilder): Rabbit = {
      val ts = Thing.near(s.ts, r.loc, SightDistance) 
      if (r.fed > Rabbit.Hungry) {
        // if a wolf is near, run away for a bit
        
        val ws = ts.filter{ _ match {
          case _: Wolf => true
          case _ => false
        }}
        
        Thing.closest(ws, r.loc) match {
          case Some((w, d)) =>
            if (d < WolfFearDistance) {
              // for now just run to a spot directly opposite from the wolf twice as far
              // eventually, we may use vectors for smarter behavior
              val oppositeLocation = Location(
                  r.loc.x - 2*(w.loc.x - r.loc.x), r.loc.y - 2*(w.loc.y - r.loc.y))
              return r.didntEat.chase(new Marker(-1, oppositeLocation))
            }
          case _ =>
        }
        // no woves; not hungry: just sit around
        r.didntEat
      }
      else {
        // if rabbit is hungry then look for nearest grass

        val gs = ts.filter{ _ match {
          case _: Grass => true
          case _ => false
        }}

        // if grass is adjacent then eat it; possibly give birth
        Thing.closest(gs, r.loc) match {
          case Some((g, d)) =>
            if (r.adjacent(g) && s.kill(g)) {
              if (r.isPregnant) {
                // make baby rabbit
                val babyLoc = Location(r.loc.x + s.rnd.nextDouble*BirthDistance, r.loc.y + s.rnd.nextDouble*BirthDistance)
                s.birth(new Rabbit(babyLoc))
              }
              return r.fullyFed
            } else r.didntEat.chase(g)
          case None => {
            // hungry and don't see anything to eat.
            // move to a nearby location ( one of 9 corners of a bounding box)
            val loc = Location(
              r.loc.x + (s.rnd.nextInt(3)-1) * WanderRange, r.loc.y + (s.rnd.nextInt(3)-1) * WanderRange)
            r.didntEat.chase(new Marker(-1, loc))
          }
        }
      }
    }
  }

  class Chasing(val target: Thing) extends Behavior[Rabbit] {

    override def toString = "Chasing: "+target
    
    def act(r: Rabbit, ms: List[Message], s: SimulationBuilder): Rabbit = {
      if (r.adjacent(target)) {
        // immediately perform the gather behavior; rabbit won't become meat till next turn
        val r2 = r.wander
        r2.behavior.act(r2, ms, s)
      }
      // keeps moving toward thing location even if it has been killed
      else r.didntEat.moveToward(target)
    }   
  }
}

