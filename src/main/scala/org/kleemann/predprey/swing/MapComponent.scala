package org.kleemann.predprey.swing

import swing._
import java.awt.{Color, Graphics2D, Point, geom}
import org.kleemann.predprey.model._

class MapComponent(var simulation: Simulation) extends Component {
  
  border = Swing.LineBorder(Color.BLACK)
  // TODO: example code has this without Dimension object "preferredSize = (300,200)"
  preferredSize = new java.awt.Dimension(300, 200)

  // TODO: seems kind of dumb to set this both here and in the constructor
  def setSimulation(s: Simulation) {
    this.simulation = s
    // I believe that since we haven't resized, we should just
    // repaint() instead of revalidate() or invalidate()
    repaint
  }
  
  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)

    // Provide function c() to convert coordinates from (startingWidth, startingHeight)
    // to (size.width, size.height)
    // The coordinates are scaled to maximum size within the component while preserving 
    // the aspect ratio
    val mapRatio = simulation.width.toDouble / simulation.height
    val componentRatio = size.width.toDouble / size.height
    val widthRatio = simulation.width.toDouble / size.width
    val heightRatio = simulation.height.toDouble / size.height
    val c: Int => Int = 
      if (mapRatio > componentRatio) coord => (coord / widthRatio).toInt
      else coord => (coord / heightRatio).toInt
    
    // brown background
    g.setColor(new Color(0xa56648))
    g.fillRect(0, 0, c(simulation.width.toInt), c(simulation.height.toInt))
    
    // current things are 2x2 boxes
    for(t <- simulation.things) t match {
      case gr: Grass => {
        g.setColor(Color.YELLOW)
        g.fillRect(c(gr.loc.x.toInt), c(gr.loc.y.toInt), 2, 2)
      }
      case r: Rabbit => {
        g.setColor(Color.BLUE)
        g.fillRect(c(r.loc.x.toInt), c(r.loc.y.toInt), 2, 2)
      }
      case w: Wolf => {
        g.setColor(Color.WHITE)
        g.fillRect(c(w.loc.x.toInt), c(w.loc.y.toInt), 2, 2)
      }
    }
  }
  
}
