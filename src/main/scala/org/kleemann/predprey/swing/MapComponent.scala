package org.kleemann.predprey.swing

import swing._
import scala.swing.event._
import java.awt.{Color, Graphics2D, Point, geom, BasicStroke}
import org.kleemann.predprey.model._
import org.kleemann.predprey.model.things._

class MapComponent(var simulation: Simulation, val statusComponent: TextArea) extends Component {
  
  border = Swing.LineBorder(Color.BLACK)
  // TODO: example code has this without Dimension object "preferredSize = (300,200)"
  preferredSize = new java.awt.Dimension(500, 300)

  // TODO: seems kind of dumb to set this both here and in the constructor
  def setSimulation(s: Simulation) {
    this.simulation = s
    // find the new object in the simulation that corresponds to the old one
    selectedThing = selectedThing.flatMap{ st => simulation.things.find{ t => t.id == st.id } }
    // I believe that since we haven't resized, we should just
    // repaint() instead of revalidate() or invalidate()
    repaint
  }

  // screen coords = model coords * scale
  val scale = 30.0
  
  // the center of the map in model coordinates
  private var _center = Location(simulation.width / 2, simulation.height / 2)
  def center = _center
  def center_= (loc: Location) {
    _center = loc
    repaint
  }

  // users can select a thing
  private var _selectedThing: Option[Thing] = None
  def selectedThing = _selectedThing
  def selectedThing_= (st: Option[Thing]) {
    _selectedThing = st
    statusComponent.text = selectedThing.fold(""){ _.prettyPrint }
  }
  
  listenTo(mouse.clicks)
  reactions += {
    case e: MousePressed  =>
      // convert to model coords
      val x = (e.point.x - size.width / 2.0) / scale + center.x
      val y = (e.point.y - size.height / 2.0) / scale + center.y
      // simple clicking; all objects are 1x1 in model size
      // linear search
      selectedThing = simulation.things.find{ t => x>t.loc.x-t.size/2 && x<t.loc.x+t.size/2 && y>t.loc.y-t.size/2 && y<t.loc.y+t.size/2 }
      repaint
  }
  
  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)

    // model bounds for viewport
    val left = center.x - size.width / 2.0 / scale 
    val right = center.x + size.width  / 2.0 / scale
    val top = center.y - size.height / 2.0 / scale
    val bottom = center.y + size.height / 2.0 / scale

    // convert model coord to screen/viewport coord
    def cx(x: Double): Int = ((x-left) * scale).toInt
    def cy(y: Double): Int = ((y-top) * scale).toInt
    
    g.setColor(new Color(0xa56648)) // brown background
    g.fillRect(cx(0.0), cy(0.0), (simulation.width * scale).toInt, (simulation.height * scale).toInt)
    
    // current things are boxes the size of a model dimension
    for(t <- simulation.things)
      // don't display thing if it is outside the viewport
      if (t.loc.x > left-scale && t.loc.x < right+scale && t.loc.y > top-scale && t.loc.y < bottom+scale) {
        val thingBounds = (t.size * scale).toInt
        t match {
          case gr: Grass => {
            g.setColor(Color.YELLOW)
            // grass is twice as high as it's size
            g.fillRect(cx(gr.loc.x - t.size/2), cy(gr.loc.y - t.size), thingBounds, thingBounds*2)
          }
          case r: Rabbit => {
            g.setColor(Color.BLUE)
            g.fillRect(cx(t.loc.x - t.size/2), cy(t.loc.y - t.size/2), thingBounds, thingBounds)
          }
          case w: Wolf => {
            g.setColor(Color.RED)
            g.fillRect(cx(w.loc.x - t.size/2), cy(w.loc.y - t.size/2), thingBounds, thingBounds)
          }
          case m: Meat => {
            g.setColor(Color.MAGENTA)
            g.fillRect(cx(m.loc.x - t.size/2), cy(m.loc.y - t.size/2), thingBounds, thingBounds)
          }
          case _ =>
        }
      }

    // display a green circle around the selected item
    selectedThing match {
      case Some(thing) => 
        g.setColor(Color.GREEN)
        g.setStroke(new BasicStroke(3))
        val d = 3.0
        g.drawOval(cx(thing.loc.x - d/2), cy(thing.loc.y - d/2), (d * scale).toInt, (d * scale).toInt)
      case None =>
    }
  }
}
