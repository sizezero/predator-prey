package org.kleemann.predprey.swing

import swing._
import java.awt.{Color, Graphics2D, Point, geom}


class MapComponent extends Component {
  
  val startingWidth = 300
  val startingHeight = 150
  
  border = Swing.LineBorder(Color.BLACK)
  // TODO: example code has this without Dimension object "preferredSize = (300,200)"
  preferredSize = new java.awt.Dimension(300, 200)

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)

    // Provide function c() to convert coordinates from (startingWidth, startingHeight)
    // to (size.width, size.height)
    // The coordinates are scaled to maximum size within the component while preserving 
    // the aspect ratio
    val mapRatio = startingWidth.toDouble / startingHeight
    val componentRatio = size.width.toDouble / size.height
    val widthRatio = startingWidth.toDouble / size.width
    val heightRatio = startingHeight.toDouble / size.height
    val c: Int => Int = 
      if (mapRatio > componentRatio) coord => (coord / widthRatio).toInt
      else coord => (coord / heightRatio).toInt
    
    g.setColor(Color.RED)
    g.fillRect(0, 0, c(startingWidth), c(startingHeight))
    g.setColor(Color.BLACK)
    val mid = c(startingWidth/2)
    g.drawLine(mid, c(0), mid, c(startingHeight))
  }
  
}
