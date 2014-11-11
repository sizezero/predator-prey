package org.kleemann.predprey.model

/**
 * <p>A mutable version of a Simulation.  Used by behaviors to iterate between simulations.
 * 
 * <p>Not thread safe.
 */
private[model] class SimulationBuilder(
    val iteration: Int,
    val width: Double,
    val height: Double,
    private var nextThingId: Int,
    val rnd: scala.util.Random,
    val ts: List[Thing]) {

    private val deadThings: scala.collection.mutable.Set[Int] = new scala.collection.mutable.HashSet()
    private var newThings: List[Thing] = List()
  
  /**
   * Returns true if the kill was successful; false if the Thing was already dead
   */
  def kill(t: Thing): Boolean = deadThings add t.id

  def birth(t: Thing) { newThings = t.setId(newId) :: newThings }

  private def newId: Int = {
    val id = nextThingId
    nextThingId += 1
    id
  }
    
  /**
   * Since this modifies this class, it only makes sense to run this once
   */
  def mkSimulation: SimulationImp = {

    // iterate through every thing in an arbitrary order; returned value replaces old
    // TODO: collect and resend messages
    val nextThings = ts.map{ _.act(Nil, this)._1 }.filter{ t => !deadThings.contains(t.id) } ++ newThings
    
    new SimulationImp(
      iteration + 1,
      width,
      height,
      nextThingId,
      rnd, // TODO: not safe; need to figure out how to deep copy this
      nextThings)
  }
}