organization := "learning.spark.streaming"

name := "hllaccumulator"

version := "0.1.1-SNAPSHOT"

scalaVersion := "2.11.11"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0" % "provided"

libraryDependencies += "com.clearspring.analytics" % "stream" % "2.7.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"