// Copyright (C) 2022-2023 Eaglescience Software B.V.

import domain.Foo
import org.kie.api.KieServices
import org.kie.api.runtime.{KieContainer, KieSession}
import org.slf4j.{Logger, LoggerFactory}

object Main extends App {
  val logger: Logger = LoggerFactory.getLogger(Main.super.getClass)

  val kieServices: KieServices = KieServices.Factory.get
  val kieContainer: KieContainer = kieServices.getKieClasspathContainer
  val kieSession: KieSession = kieContainer.newKieSession("DroolDummyKS")

  kieSession.setGlobal("logger", logger)
  kieSession.insert(new Foo("bar"))
  kieSession.fireAllRules
}
