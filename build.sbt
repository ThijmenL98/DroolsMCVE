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
      case xs if xs.endsWith("LICENSE") => MergeStrategy.discard
      case xs if xs.endsWith("LICENSE.txt") => MergeStrategy.discard
      case xs if xs.endsWith("INDEX.LIST") => MergeStrategy.discard
      case xs if xs.endsWith("MANIFEST.MF") => MergeStrategy.discard
      case xs if xs.endsWith("NOTICE") => MergeStrategy.discard
      case xs if xs.endsWith("NOTICE.txt") => MergeStrategy.discard
      case xs if xs.endsWith("module-info.class") => MergeStrategy.discard
      case PathList("META-INF", "services", "org.apache.poi.sl.draw.ImageRenderer") => MergeStrategy.filterDistinctLines
      case PathList("META-INF", "services", "org.apache.poi.ss.usermodel.WorkbookProvider") => MergeStrategy.filterDistinctLines
      case PathList("META-INF", "services", "org.apache.poi.extractor.ExtractorProvider") => MergeStrategy.filterDistinctLines
      case PathList("META-INF", "services", "org.drools.wiring.api.ComponentsSupplier") => MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.singleOrError
    }
  )
