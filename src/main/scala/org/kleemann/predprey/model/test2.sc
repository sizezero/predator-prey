package org.kleemann.predprey.model

object test2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  //val p = MultiConfiguration.parse("org/kleemann/predprey/model/multi-configuration.conf")
  
  //val resource = "/org/kleemann/predprey/model/multi-configuration.conf"
  val resource = "/multi-configuration.conf"      //> resource  : String = /multi-configuration.conf
  //val resource = "/application.conf"

  val is = test2.getClass().getResourceAsStream(resource)
                                                  //> is  : java.io.InputStream = null
  val sr = scala.util.parsing.input.StreamReader(new java.io.InputStreamReader(is))
                                                  //> java.lang.NullPointerException
                                                  //| 	at java.io.Reader.<init>(Reader.java:78)
                                                  //| 	at java.io.InputStreamReader.<init>(InputStreamReader.java:72)
                                                  //| 	at org.kleemann.predprey.model.test2$$anonfun$main$1.apply$mcV$sp(org.kl
                                                  //| eemann.predprey.model.test2.scala:13)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at org.kleemann.predprey.model.test2$.main(org.kleemann.predprey.model.t
                                                  //| est2.scala:3)
                                                  //| 	at org.kleemann.predprey.model.test2.main(org.kleemann.predprey.model.te
                                                  //| st2.scala)
  
  
}