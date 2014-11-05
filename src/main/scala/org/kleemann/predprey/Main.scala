package org.kleemann.predprey

import scala.swing._
import scala.swing.event._
// TODO: calling this package swing makes the other imports a bit wordier
import org.kleemann.predprey.swing._
import model._


object Main extends SimpleSwingApplication {
  
  var simulation: Simulation = SimulationFactory.random1
  val mapComponent = new swing.MapComponent(simulation)

  def top = new MainFrame {
    title = "Predator / Prey"
    
    val iterationLabel = new Label(simulation.iteration.toString)
    val nextButton = new Button("Next") {
      reactions += {
        case ButtonClicked(_) => {
          // TODO: since this takes quite a while we probably don't want it in the ui thread
          simulation = simulation.next
          iterationLabel.text = simulation.iteration.toString
          mapComponent.setSimulation(simulation)
        }
      }
    }
    val b1 = new BoxPanel(Orientation.Vertical) {
      contents.append(iterationLabel, nextButton)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
    contents = new BoxPanel(Orientation.Horizontal ) {
      contents.append(b1, mapComponent)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
  }
}