name := """jianghu-server"""

version := "1.0"

scalaVersion := "2.11.7"

//// Change this to another test framework if you prefer
//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor_2.11
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5.3"

// https://mvnrepository.com/artifact/io.netty/netty-all
libraryDependencies += "io.netty" % "netty-all" % "4.1.14.Final"

// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"

//// https://mvnrepository.com/artifact/org.jooq/jooq
//libraryDependencies += "org.jooq" % "jooq" % "3.9.5"

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"

// https://mvnrepository.com/artifact/commons-dbutils/commons-dbutils
libraryDependencies += "commons-dbutils" % "commons-dbutils" % "1.7"

// https://mvnrepository.com/artifact/com.zaxxer/HikariCP
libraryDependencies += "com.zaxxer" % "HikariCP" % "2.7.1"

fork in run := false