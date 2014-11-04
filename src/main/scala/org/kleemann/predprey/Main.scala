package org.kleemann.predprey

import swing._

object Main extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "ScalaSheet"
    contents = new Label("TODO")
  }
}