package org.kleemann.predprey

import swing._

object Main extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "ScalaSheet"
    val l1 = new Label("TODO: CONTROL")
    val l2 = new Component {
      border = Swing.LineBorder(java.awt.Color.BLACK)
      // TODO: example code has this without Dimension object "preferredSize = (300,200)"
      preferredSize = new java.awt.Dimension(300, 200)
    }
    contents = new BoxPanel(Orientation.Horizontal ) {
      contents.append(l1, l2)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
  }
  
}