package org.kleemann.predprey.model.things

import org.kleemann.predprey.model._

case class Wolf(
    override val id: Int,
    override val loc: Location,
    val fed: Int,
    val nextBirth: Int,
    private val behavior: Behavior[Wolf])
    extends Thing {

  def this (loc: Location) {
    this(-1, loc, Wolf.Full, Wolf.BirthDelay, Wolf.Prowling)
  }
  
  override def setId(newId: Int) = Wolf(newId, loc, fed, nextBirth, behavior) 
  
  override val size = Wolf.Size
  
  def isPregnant: Boolean = nextBirth<=0
  
  def fullyFed: Wolf = Wolf(id, loc, Wolf.Full, if (isPregnant) Wolf.BirthDelay else nextBirth-1, Wolf.Prowling)
  
  def didntEat: Wolf = Wolf(id, loc, fed-1, nextBirth, behavior)
  
  def moveToward(target: Thing): Wolf = Wolf(id, toward(target, Wolf.MoveDistance), fed, nextBirth, behavior)

  def chase(target: Thing): Wolf = Wolf(id, loc, fed, nextBirth, new Wolf.Chasing(target))
  
  def prowl: Wolf = Wolf(id, loc, fed, nextBirth, Wolf.Prowling)
  
  def isStarved: Boolean = fed <= 0
  
  // Wolf has dynamic behavior but check for starvation first
  def act(ms: List[Message], s: SimulationBuilder): Wolf = {
    if (isStarved) {
      s.kill(this)
      s.birth(new Meat(loc))
      this
    } else behavior.act(this, ms, s)
  }
}

object Wolf {
  private val conf = com.typesafe.config.ConfigFactory.load()
  
  val Size = conf.getDouble(s"wolf.size")
  val Full = conf.getInt(s"wolf.full")
  val Hungry = conf.getInt(s"wolf.hungry")
  val MoveDistance = conf.getDouble(s"wolf.move-distance")
  val BirthDelay = conf.getInt(s"wolf.birth-delay")
  val BirthDistance = conf.getDouble(s"wolf.birth-distance")
  val SightDistance =  conf.getDouble(s"wolf.sight-distance")
  val WanderRange =  conf.getDouble(s"wolf.wander-range")
  
  object Prowling extends Behavior[Wolf] {

    override def toString = "Prowling"
    
    def act(w: Wolf, ms: List[Message], s: SimulationBuilder): Wolf = {
      // if wolf is not hungry then don't move
      if (w.fed > Wolf.Hungry) w.didntEat
      else {
        // if wolf is hungry then look for nearest meat or rabbit

        val ts = Thing.near(s.ts, w.loc, SightDistance) 
        val ms = ts.filter{ _ match {
          case _: Meat => true
          case _ => false
        }}

        // if meat is adjacent then eat it; possibly give birth
        Thing.closest(ms, w.loc) match {
          case Some((m, d)) =>
            if (w.adjacent(m) && s.kill(m)) {
              if (w.isPregnant) {
                // make baby wolf
                val babyLoc = Location(w.loc.x + s.rnd.nextDouble*BirthDistance, w.loc.y + s.rnd.nextDouble*BirthDistance)
                s.birth(new Wolf(babyLoc))
              }
              return w.fullyFed
            }
          case None =>
        }
        
        // no adjacent meat
        
        val rms = ts.filter{ _ match {
          case _: Rabbit => true
          case _: Meat => true
          case _ => false
        }}
        Thing.closest(rms, w.loc) match {
          case Some((t, d)) => w.didntEat.chase(t)
          case _ => {
            // hungry and don't see anything to eat.
            // move to a nearby location ( one of 9 corners of a bounding box)
            val loc = Location(
              w.loc.x + (s.rnd.nextInt(3)-1) * WanderRange, w.loc.y + (s.rnd.nextInt(3)-1) * WanderRange)
            w.didntEat.chase(new Marker(-1, loc))
          }
        }
      }
    }
  }

  class Chasing(val target: Thing) extends Behavior[Wolf] {

    override def toString = "Chasing: "+target
    
    def act(w: Wolf, ms: List[Message], s: SimulationBuilder): Wolf = {
      if (w.adjacent(target)) {
        // if target is a rabbit then attack it
        target match {
          case r: Rabbit => s.send(r, Attack)
          case _ => 
        }
        // immediately perform the prowl behavior; rabbit won't become meat till next turn
        val w2 = w.prowl
        w2.behavior.act(w2, ms, s)
      }
      // keeps moving toward thing location even if it has been killed
      else w.didntEat.moveToward(target)
    }   
  }
}

