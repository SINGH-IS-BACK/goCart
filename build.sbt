name := "goCart"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.10.1"

libraryDependencies += "org.mongodb.morphia" % "morphia" % "1.0.0-rc0"

libraryDependencies += "com.simplify" % "payments-sdk-java" % "1.3.0"

play.Project.playJavaSettings

