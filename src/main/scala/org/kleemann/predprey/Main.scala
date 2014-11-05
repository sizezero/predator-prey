package org.kleemann.predprey

import scala.swing._
import scala.swing.event._
// TODO: calling this package swing makes the other imports a bit wordier
import org.kleemann.predprey.swing._
import model._


object Main extends SimpleSwingApplication {
  
  var iteration = 0
  val simulation: Simulation = new SimulationImp()

  def top = new MainFrame {
    title = "Predator / Prey"
    
    val iterationLabel = new Label(iteration.toString)
    val nextButton = new Button("Next") {
      reactions += {
        case ButtonClicked(_) => {
          iteration += 1
          iterationLabel.text = iteration.toString
          simulation.next
        }
      }
    }
    val b1 = new BoxPanel(Orientation.Vertical) {
      contents.append(iterationLabel, nextButton)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
    val b2 = new swing.MapComponent(simulation)
    contents = new BoxPanel(Orientation.Horizontal ) {
      contents.append(b1, b2)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
  }
}