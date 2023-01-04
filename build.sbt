ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "MCVE",
    libraryDependencies ++= Seq(
      "org.drools" % "drools-core" % "8.31.1.Final",
      "org.drools" % "drools-compiler" % "8.31.1.Final",
      "org.drools" % "drools-decisiontables" % "8.31.1.Final",
      "org.drools" % "drools-mvel" % "8.31.1.Final",
      "org.drools" % "drools-model-compiler" % "8.31.1.Final",
      "org.drools" % "drools-xml-support" % "8.31.1.Final",
      "org.kie" % "kie-api" % "8.31.1.Final",
      "org.slf4j" % "slf4j-api" % "2.0.5",
      "org.slf4j" % "slf4j-log4j12" % "2.0.5"
    ),
    resolvers in Global ++= Seq(
      "Sbt plugins" at "https://dl.bintray.com/sbt/sbt-plugin-releases",
    ),
    Compile / packageBin / mainClass := Some("Main"),
    Compile / run / mainClass := Some("Main")
  )
  .settings(
    Compile / resourceDirectory := file(".") / "./src/main/resources",
    Runtime / resourceDirectory := file(".") / "./src/main/resources",
  )
  .settings(
    assembly / assemblyJarName := "myJar.jar",
    assembly / assemblyMergeStrategy := {
      case x if x.endsWith("module-info.class") => MergeStrategy.discard
      case x =>
        val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
        oldStrategy(x)
    },
  )
