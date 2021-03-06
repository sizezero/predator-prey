package org.kleemann.predprey.swing

import swing._
import scala.swing.event._
import java.awt.{Color, Graphics2D, Point, geom}
import org.kleemann.predprey.model._
import org.kleemann.predprey.model.things._

class MiniMap(var simulation: Simulation, val mapComponent: MapComponent) extends Component {
  
  border = Swing.LineBorder(Color.BLACK)
  // TODO: example code has this without Dimension object "preferredSize = (300,200)"
  preferredSize = new java.awt.Dimension(simulation.width.toInt, simulation.height.toInt)

  listenTo(mouse.clicks)
  reactions += {
    case e: MousePressed  =>
      // the mini-map is 1:1 model to screen coords so no conversion is necessary
      mapComponent.center = Location(e.point.x, e.point.y)
      repaint
    }
  
  // TODO: seems kind of dumb to set this both here and in the constructor
  def setSimulation(s: Simulation) {
    this.simulation = s
    // I believe that since we haven't resized, we should just
    // repaint() instead of revalidate() or invalidate()
    repaint
  }
  
  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)

    // draw simulation coords to component coords 1-1
    
    g.setColor(new Color(0xa56648)) // brown background
    g.fillRect(0, 0, simulation.width.toInt, simulation.height.toInt)
    
    // current things are 1x1 boxes
    for(t <- simulation.things) {
      t match {
        case gr: Grass => g.setColor(Color.YELLOW)
        case r: Rabbit => g.setColor(Color.BLUE)
        case w: Wolf => g.setColor(Color.RED)
        case m: Meat => g.setColor(Color.MAGENTA)
        case _ => g.setColor(Color.WHITE)
      }
      g.fillRect(t.loc.x.toInt, t.loc.y.toInt, 1, 1)
    }

    mapComponent.selectedThing match {
      case Some(thing) => 
        g.setColor(Color.GREEN)
        g.fillRect(thing.loc.x.toInt, thing.loc.y.toInt, 2, 2)
      case None =>
    }
    
    g.setColor(Color.WHITE)
    g.drawRect(
      (mapComponent.center.x - mapComponent.size.width / mapComponent.scale / 2.0).toInt,
      (mapComponent.center.y - mapComponent.size.height / mapComponent.scale / 2.0).toInt,
      (mapComponent.size.width / mapComponent.scale).toInt,
      (mapComponent.size.height / mapComponent.scale).toInt)
    
  }
  
}
