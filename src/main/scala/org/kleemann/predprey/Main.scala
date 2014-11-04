package org.kleemann.predprey

import scala.swing._
// TODO: calling this package swing makes the other imports a bit wordier
import org.kleemann.predprey.swing._

object Main extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Predator / Prey"
    val l1 = new Label("TODO: CONTROL")
    val l2 = new swing.MapComponent
    contents = new BoxPanel(Orientation.Horizontal ) {
      contents.append(l1, l2)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
  }
  
}