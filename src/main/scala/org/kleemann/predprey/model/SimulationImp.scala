package org.kleemann.predprey.model

import scala.collection.mutable.MutableList

private[model] class SimulationImp(
  override val iteration: Int,
  override val width: Double,
  override val height: Double,
  var nextThing: Int,
  val rnd: scala.util.Random,
  val gs: List[Grass],
  val rs: List[Rabbit]) extends Simulation {

  override val things = gs ++ rs

  def distance(x1: Double, y1: Double, x2: Double, y2: Double): Double = {
    val a = x1 - x2
    val b = y1 - y2
    math.sqrt(a * a + b * b)
  }

  def adjacent(dist: Double): Boolean = math.abs(dist) < 2.0

  // fairly dumb move algorithm: move dist 0.5 in both x and y directions towards target
  // will overshoot
  def moveTowards(x1: Double, y1: Double, x2: Double, y2: Double): (Double, Double) = {
    val d = 0.5
    if (adjacent(distance(x1, y1, x2, y2))) (x1, y1)
    else (if (x1 > x2) x1 - d else x1 + d, if (y1 > y2) y1 - d else y1 + d)
  }

  // find closest grass (linear search)
  def closestGrass(gs: Seq[Grass], x: Double, y: Double): Option[Tuple2[Grass, Double]] =
    gs.foldLeft[Option[Tuple2[Grass, Double]]](None) { (o, g) =>
      o match {
        case Some((Grass(gid, gx, gy), dOld)) => {
          val dNew = distance(x, y, g.x, g.y)
          if (dNew < dOld) Some(g, dNew)
          else o
        }
        case None => g match {
          case Grass(gid, gx, gy) => Some((g, distance(x, y, gx, gy)))
        }
      }
    }

  def next: Simulation = {

    // rabbits move towards grass and eats it
    val nextRs: List[Rabbit] = rs map { r =>
      r match {
        case Rabbit(rid, rx, ry) => {

          // if the rabbit is close then eat it; otherwise move towards it
          closestGrass(gs, rx, ry) match {
            case Some((Grass(gid, gx, gy), d)) => {
              if (adjacent(d)) r // TODO: this is an efficient way to handle the side effect of removing the grass
              else {
                val (nx, ny) = moveTowards(rx, ry, gx, gy)
                Rabbit(rid, nx, ny)
              }
            }
            // no grass then the rabbit doesn't move
            case None => r
          }
        }
      }
    }

    // TODO: currently grass is static
    val nextGs = gs

    new SimulationImp(
      iteration + 1,
      width,
      height,
      nextThing,
      new scala.util.Random(rnd.self),
      nextGs,
      nextRs)
  }
}

object SimulationFactory {

  // TODO: returing SimulationImp just for testing
  def random1: SimulationImp = {
    val width = 300.0
    val height = 200.0
    var id: Int = 0
    val rnd = new scala.util.Random()

    var gs: List[Grass] = List()
    for (i <- 1 to 10) {
      // need to play around with the list append syntax
      // doesn't work
      // l += i
      // does work but ugly
      // l = l :+ i
      // does work but ugly
      // l = i :: l
      gs = Grass(id, rnd.nextInt(width.toInt), rnd.nextInt(height.toInt)) :: gs
      id += 1
    }

    var rs: List[Rabbit] = List()
    for (i <- 1 to 10) {
      rs = Rabbit(id, rnd.nextInt(width.toInt), rnd.nextInt(height.toInt)) :: rs
      id += 1
    }

    new SimulationImp(0, width, height, id, rnd, gs, rs)
  }

}