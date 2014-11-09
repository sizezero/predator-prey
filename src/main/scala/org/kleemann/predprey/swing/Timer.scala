package org.kleemann.predprey.swing

import scala.swing._
import java.awt.{Font,Color}

/**
 * <p>Run some code one time from the swing event thread
 */
object InvokeLater {
  def apply(op: => Unit) {
    javax.swing.SwingUtilities.invokeLater(new Runnable {
      def run() = op
    })
  }
}

/**
 * <p>This allows you to construct javax.swing.Timer objects in 
 * Scala friendly way
 */
object Timer {

  /**
   * Return a normal swing Timer; already running
   */
  def apply(intervalMillis: Int)(op: => Unit): javax.swing.Timer = {
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) = op
    }
    new javax.swing.Timer(intervalMillis, timeOut)
  }
}
