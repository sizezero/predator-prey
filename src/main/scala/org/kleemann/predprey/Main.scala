package org.kleemann.predprey

import scala.swing._
import scala.swing.event._
// TODO: calling this package swing makes the other imports a bit wordier
import org.kleemann.predprey.swing._
import model._
import scala.concurrent._

object Main extends SimpleSwingApplication {
  
  var simulation: Simulation = SimulationFactory.random1
  val mapComponent = new swing.MapComponent(simulation)
  val iterationLabel = new Label(simulation.iteration.toString)
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

  var isCalculatingNextIteration = false

  // Try to perform the next iteration in the background.  
  // If a background iteration task is already running then skip it.
  def nextIteration {
    // start in UI thread
    import ExecutionContext.Implicits.global
    // local variable may be necessary so that pooled thread does not see a stale reference
    val simulationFreshReference = simulation
    if (!isCalculatingNextIteration) {
      isCalculatingNextIteration = true
      // could just call "new Thread(new Runnable{ def run() {}})" but future may use thread pooling
      future {
        // switch to background non-UI thread
        val nextSimulation = simulationFreshReference.next
        InvokeLater {
          // back to UI thread
          simulation = nextSimulation
          iterationLabel.text = simulation.iteration.toString
          mapComponent.setSimulation(simulation)
          isCalculatingNextIteration = false
        }
      }
    }
  }
  
  // start a timer task; do we have to wait until the window has started?
  Timer(1000) { nextIteration }
}