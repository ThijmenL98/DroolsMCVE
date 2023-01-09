import domain.CurrentState
import org.kie.api.KieServices
import org.kie.api.runtime.{KieContainer, KieSession}
import org.slf4j.{Logger, LoggerFactory}

object Main extends App {
  val logger: Logger = LoggerFactory.getLogger(Main.super.getClass)

  val kieServices: KieServices = KieServices.Factory.get
  val kieContainer: KieContainer = kieServices.getKieClasspathContainer
  val kieSession: KieSession = kieContainer.newKieSession("DroolDummyKS")

  kieSession.insert(new CurrentState(false))
  Thread.sleep(100)
  kieSession.fireAllRules
}
