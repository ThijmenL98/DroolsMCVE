package domain

import java.util.Date

class CurrentState(var throttleState: Boolean) {

  var lastThrottled: Date = new Date
  def getThrottleState: Boolean = {
    throttleState
  }

  def setLastThrottled(t: Date): Unit = {
    lastThrottled = t
  }

  def setThrottleState(toggled: Boolean): Unit = {
    throttleState = toggled
  }

}
