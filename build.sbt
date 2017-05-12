
name := "scala3dtiles"

version := (version in ThisBuild).value

organization := "com.github.workingDog"

scalaVersion := "2.11.11"

crossScalaVersions := Seq("2.11.11, 2.12.2")

libraryDependencies ++= Seq("com.typesafe.play" %% "play-json" % "2.5.14")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions ++= Seq( "-unchecked", "-deprecation",  "-feature"  )

homepage := Some(url("https://github.com/workingDog/Scala3dTiles"))

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

