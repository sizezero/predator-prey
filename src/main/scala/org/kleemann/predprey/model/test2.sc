package org.kleemann.predprey.model

object test2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val s1 = SimulationFactory.random1              //> s1  : org.kleemann.predprey.model.SimulationImp = org.kleemann.predprey.mode
                                                  //| l.SimulationImp@499aed36
  s1.things.size                                  //> res0: Int = 91
  s1.things.filter{ t => t match { case _ : Wolf => true; case _ => false}}.size
                                                  //> res1: Int = 20
  val s2 = s1.next                                //> s2  : org.kleemann.predprey.model.Simulation = org.kleemann.predprey.model.S
                                                  //| imulationImp@40b73194
  s2.things.size                                  //> res2: Int = 100
  s2.things.filter{ t => t match { case _ : Wolf => true; case _ => false}}.size
                                                  //> res3: Int = 20
}