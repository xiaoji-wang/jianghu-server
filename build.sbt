name := """jianghu-server"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor_2.11
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5.3"

// https://mvnrepository.com/artifact/io.netty/netty-all
libraryDependencies += "io.netty" % "netty-all" % "4.1.14.Final"

// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"

//// https://mvnrepository.com/artifact/org.json4s/json4s-native_2.11
//libraryDependencies += "org.json4s" % "json4s-native_2.11" % "3.5.3"
//
//// https://mvnrepository.com/artifact/org.json4s/json4s-jackson_2.11
//libraryDependencies += "org.json4s" % "json4s-jackson_2.11" % "3.5.3"

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"


fork in run := false