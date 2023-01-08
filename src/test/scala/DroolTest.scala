import java.util.concurrent.TimeUnit
import java.util.Date

import domain._
import org.kie.api.KieServices
import org.kie.api.event.rule.{AfterMatchFiredEvent, DefaultAgendaEventListener}
import org.kie.api.runtime.{KieContainer, KieSession, KieSessionConfiguration}
import org.kie.api.runtime.conf.ClockTypeOption
import org.kie.api.time.SessionPseudoClock
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class SessionListener(kieSession: KieSession, var triggerMap: Map[String, Int] = Map.empty) {

  kieSession.addEventListener(new DefaultAgendaEventListener() {
    override def afterMatchFired(event: AfterMatchFiredEvent): Unit = {
      super.afterMatchFired(event)
      val ruleName = event.getMatch.getRule.getName
      triggerMap = triggerMap ++ List(ruleName -> (getCount(ruleName) + 1))
    }
  })

  def getCount(key: String): Int = triggerMap.getOrElse(key, 0)
}

class DroolTest extends AsyncWordSpec with Matchers {
  // Testing KIE Setup
  val kFactory: KieServices = KieServices.Factory.get
  val kieConfig: KieSessionConfiguration = kFactory.newKieSessionConfiguration
  val kieContainer: KieContainer = kFactory.getKieClasspathContainer
  kieConfig.setOption(ClockTypeOption.get("pseudo"))

  // Instantiate resettable objects. These need to be reset for every test as drools keeps track of everything using
  // an aggregate, not resetting these values might cause tests to influence each other.
  var kieSession: KieSession = _
  var clock: SessionPseudoClock = _
  var sListener: SessionListener = _

  // Reset the drools state for a new test
  def reset(): Unit = {
    kieSession = kieContainer.newKieSession("DroolDummyKS", kieConfig)
    clock = kieSession.getSessionClock
    kieSession.insert(new CurrentState(false))
    sListener = new SessionListener(kieSession)
  }

  // Helper functions
  def mockMessage(offsetSec: Int = 0, offsetMSec: Int = 0): Message =
    new Message(new Date(System.currentTimeMillis() + (offsetSec * 1000 + offsetMSec)))

  def insertAndFire(message: Message, timeAdvanceSec: Int = 0, timeAdvanceMSec: Int = 0): Unit = {
    clock.advanceTime(timeAdvanceSec, TimeUnit.SECONDS)
    clock.advanceTime(timeAdvanceMSec, TimeUnit.MILLISECONDS)
    kieSession.insert(message)
    kieSession.fireAllRules
  }

  "rules" must {
    "successfully trigger (activate)" in {
      reset() // Resets the kie session and event listener
      // The insertAndFire function first advances the pseudo clock, then inserts the message, then triggers all
      // drools rules
      (1 to 50).foreach(i => insertAndFire(mockMessage(offsetMSec = 200 * i), timeAdvanceMSec = 200))
      // The sListener simply counts every drools rule fire amount
      sListener.getCount("throttle state activated") mustBe 1 // THIS TEST SUCCEEDS
    }

    "successfully trigger (deactivate)" in {
      reset()
      (1 to 50).foreach(i => insertAndFire(mockMessage(offsetMSec = 200 * i), timeAdvanceMSec = 200))
      (1 to 60).foreach(_ => {
        // Now for the next 60 seconds, we advance the clock and fire all rules to force the deactivation to not have 20 messages in the last 10 seconds.
        // This should theoretically already trigger the rule after 10 for loop iterations.
        clock.advanceTime(1, TimeUnit.SECONDS)
        kieSession.fireAllRules
      })
      sListener.getCount("throttle state activated") mustBe 1 // THIS TEST SUCCEEDS
      sListener.getCount("throttle state deactivated") mustBe 1 // !!! THIS TEST FAILS !!!
    }
  }
}
