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
  
  def setId(newId: Int) = Wolf(newId, loc, fed, nextBirth, behavior) 
  
  def isPregnant: Boolean = nextBirth<=0
  
  def fullyFed: Wolf = Wolf(id, loc, Wolf.Full, if (isPregnant) 5 else nextBirth-1, Wolf.Prowling)
  
  def didntEat: Wolf = Wolf(id, loc, fed-1, nextBirth, behavior)
  
  def moveToward(target: Location): Wolf = Wolf(id, loc.moveTowards(target, Wolf.MoveDistance), fed, nextBirth, behavior)

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
  val Full = 30
  val Hungry = 20
  val BirthDelay = 10
  val MoveDistance = 1.5
  val BirthDistance = 5.0
  
  object Prowling extends Behavior[Wolf] {

    override def toString = "Prowling"
    
    def act(w: Wolf, ms: List[Message], s: SimulationBuilder): Wolf = {
      // if wolf is hungry then look for closest meat or rabbit
      if (w.fed > Wolf.Hungry) w.didntEat
      else {
        // TODO: very inefficient to filter this for every Wolf
        val ms = s.ts.filter{ _ match {
          case _: Meat => true
          case _ => false
        }}

        // if meat is adjacent then eat it; possibly give birth
        Thing.closest(ms, w.loc) match {
          case Some((m, d)) =>
            if (Location.adjacent(d) && s.kill(m)) {
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
        
        // TODO: very inefficient to filter this for every Wolf
        val rms = s.ts.filter{ _ match {
          case _: Rabbit => true
          case _: Meat => true
          case _ => false
        }}
        Thing.closest(rms, w.loc) match {
          case Some((t, d)) => w.didntEat.chase(t)
          case _ => w.didntEat
        }
      }
    }
  }

  class Chasing(val target: Thing) extends Behavior[Wolf] {

    override def toString = "Chasing: "+target
    
    def act(w: Wolf, ms: List[Message], s: SimulationBuilder): Wolf = {
      if (Location.adjacent(w.loc.distance(target.loc))) {
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
      else w.didntEat.moveToward(target.loc)
    }   
  }
}

