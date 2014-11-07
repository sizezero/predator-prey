package org.kleemann.predprey

import scala.swing._
import scala.swing.event._
// TODO: calling this package swing makes the other imports a bit wordier
import org.kleemann.predprey.swing._
import model._


object Main extends SimpleSwingApplication {
  
  var simulation: Simulation = SimulationFactory.random1
  val mapComponent = new swing.MapComponent(simulation)
  val iterationLabel = new Label(simulation.iteration.toString)

  // TODO: currently this runs in the UI thread which is a bad idea
  def nextIteration {
    // TODO: since this takes quite a while we probably don't want it in the ui thread
    simulation = simulation.next
    iterationLabel.text = simulation.iteration.toString
    mapComponent.setSimulation(simulation)
  }
  
  def top = new MainFrame {
    title = "Predator / Prey"
    
    val nextButton = new Button("Next") {
      reactions += {
        case ButtonClicked(_) => nextIteration
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
  
  // start a timer task; do we have to wait until the window has started?
  Timer(2000) { nextIteration }
}