package org.kleemann.predprey.swing

import swing._
import java.awt.{Color, Graphics2D, Point, geom}
import org.kleemann.predprey.model._

class MapComponent(var simulation: Simulation) extends Component {
  
  border = Swing.LineBorder(Color.BLACK)
  // TODO: example code has this without Dimension object "preferredSize = (300,200)"
  preferredSize = new java.awt.Dimension(500, 300)

  // TODO: seems kind of dumb to set this both here and in the constructor
  def setSimulation(s: Simulation) {
    this.simulation = s
    // I believe that since we haven't resized, we should just
    // repaint() instead of revalidate() or invalidate()
    repaint
  }

  // screen coords = model coords * scale
  val scale = 30.0
  
  // the center of the map in model coordinates
  private var _center = Location(0.0, 0.0)
  def center = _center
  def center_= (loc: Location) {
    _center = loc
    repaint
  }
  
  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)

    // model bounds for viewport
    val left = center.x - size.width / scale / 2.0
    val right = center.x + size.width / scale / 2.0
    val top = center.y - size.height / scale / 2.0
    val bottom = center.y + size.height / scale / 2.0

    // convert model coord to screen/viewport coord
    def cx(x: Double): Int = ((x-left) * scale).toInt
    def cy(y: Double): Int = ((y-top) * scale).toInt
    
    g.setColor(new Color(0xa56648)) // brown background
    g.fillRect(cx(0.0), cy(0.0), (simulation.width * scale).toInt, (simulation.height * scale).toInt)
    
    val grassSize = (scale/2).toInt
    val rabbitSize = scale.toInt
    val wolfeSize = rabbitSize
    
    // current things are boxes the size of a model dimension
    for(t <- simulation.things)
      if (t.loc.x > left-scale && t.loc.x < right+scale && t.loc.y > top-scale && t.loc.y < top+scale)
        t match {
          case gr: Grass => {
            g.setColor(Color.YELLOW)
            g.fillRect(cx(gr.loc.x), cy(gr.loc.y), grassSize, grassSize)
          }
          case r: Rabbit => {
            g.setColor(Color.BLUE)
            g.fillRect(cx(r.loc.x), cy(r.loc.y.toInt), rabbitSize, rabbitSize)
          }
          case w: Wolf => {
            g.setColor(Color.RED)
            g.fillRect(cx(w.loc.x), cy(w.loc.y), wolfeSize, wolfeSize)
          }
        }  
  }
}
