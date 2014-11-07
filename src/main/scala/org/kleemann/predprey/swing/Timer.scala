package org.kleemann.predprey.swing

import scala.swing._
import java.awt.{Font,Color}

/**
 * <p>snagged from the interwebs
 * 
 * <p>https://otfried-cheong.appspot.com/scala/timers.html
 */

object Time {
  private val form = new java.text.SimpleDateFormat("HH:mm:ss")
  def current = form.format(java.util.Calendar.getInstance().getTime)
}

object Timer {
  def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) = op
    }
    val t = new javax.swing.Timer(interval, timeOut)
    t.setRepeats(repeats)
    t.start()
  }
}
