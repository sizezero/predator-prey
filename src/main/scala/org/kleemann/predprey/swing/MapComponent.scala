package org.kleemann.predprey.swing

import swing._
import java.awt.{Color, Graphics2D, Point, geom}
import org.kleemann.predprey.model._

class MapComponent(val simulation: Simulation) extends Component {
  
  border = Swing.LineBorder(Color.BLACK)
  // TODO: example code has this without Dimension object "preferredSize = (300,200)"
  preferredSize = new java.awt.Dimension(300, 200)

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
      case Grass(id, x, y) => {
        g.setColor(Color.YELLOW)
        g.fillRect(c(x.toInt), c(y.toInt), 2, 2)
      }
      case Rabbit(id, x, y) => {
        g.setColor(Color.BLUE)
        g.fillRect(c(x.toInt), c(y.toInt), 2, 2)
      }
    }
  }
  
}
