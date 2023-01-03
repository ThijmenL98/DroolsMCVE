// Copyright (C) 2022-2023 Eaglescience Software B.V.

import org.kie.api.KieServices
import org.kie.api.runtime.{KieContainer, KieSession}
import org.slf4j.{Logger, LoggerFactory}

object Main extends App {
  val kieServices: KieServices = KieServices.Factory.get

  val logger: Logger = LoggerFactory.getLogger(Main.super.getClass)

  val kieContainer = kieServices.getKieClasspathContainer()
  val kieSession: KieSession = kieContainer.newKieSession("DroolDummyKS")

  kieSession.setGlobal("logger", logger)
  kieSession.fireAllRules()
}
