package org.kleemann.predprey

import scala.swing._
import scala.swing.event._
// TODO: calling this package swing makes the other imports a bit wordier
import org.kleemann.predprey.swing._
import model._
import scala.concurrent._

object Main extends SimpleSwingApplication {
  
  var simulation: Simulation = SimulationFactory.random1
  val statusComponent = new Label
  val mapComponent = new swing.MapComponent(simulation, statusComponent)
  val miniMapComponent = new swing.MiniMap(simulation, mapComponent)
  val iterationLabel = new Label(simulation.iteration.toString)

  def top = new MainFrame {
    title = "Predator / Prey"
    
    val playPauseButton = new Button("Pause") {
      reactions += {
        case ButtonClicked(_) => {
          if (timer.isRunning) {
            timer.stop
            text = "Play"
          } else {
            timer.start
            text = "Pause"
          }
        }
      }
    }
    val left= new BoxPanel(Orientation.Vertical) {
      contents.append(iterationLabel, playPauseButton)
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
    val right = new BorderPanel {
      layout(statusComponent) = BorderPanel.Position.Center
      layout(miniMapComponent) = BorderPanel.Position.South
      border = Swing.EmptyBorder(5, 5, 5, 5)
    }
    contents = new BorderPanel {
      layout(left) = BorderPanel.Position.West
      layout(mapComponent) = BorderPanel.Position.Center
      layout(right) = BorderPanel.Position.East
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
        // switch to background non-UI thread for long running calculation
        val nextSimulation = simulationFreshReference.next
        InvokeLater {
          // back to UI thread
          simulation = nextSimulation
          iterationLabel.text = simulation.iteration.toString
          mapComponent.setSimulation(simulation)
          miniMapComponent.setSimulation(simulation)
          isCalculatingNextIteration = false
        }
      }
    }
  }
  
  // start a timer task; do we have to wait until the window has started?
  val timer = Timer(1000) { nextIteration }
  timer.start
}