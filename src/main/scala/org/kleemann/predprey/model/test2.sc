package org.kleemann.predprey.model

object test2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val s1 = SimulationFactory.random1              //> s1  : org.kleemann.predprey.model.SimulationImp = org.kleemann.predprey.mode
                                                  //| l.SimulationImp@3bdd7d1e
  s1.gs                                           //> res0: List[org.kleemann.predprey.model.Grass] = List(Grass(9,227.0,25.0), Gr
                                                  //| ass(8,187.0,97.0), Grass(7,46.0,196.0), Grass(6,163.0,92.0), Grass(5,212.0,1
                                                  //| 19.0), Grass(4,139.0,186.0), Grass(3,218.0,28.0), Grass(2,90.0,198.0), Grass
                                                  //| (1,256.0,104.0), Grass(0,253.0,185.0))
  s1.closestGrass(s1.gs, 0.0, 0.0)                //> res1: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(6,16
                                                  //| 3.0,92.0),187.17104476921637))
  s1.closestGrass(s1.gs, 300.0, 200.0)            //> res2: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(0,25
                                                  //| 3.0,185.0),49.33558553417604))
  s1.distance(0.0, 0.0, 200.0, 108.0)             //> res3: Double = 227.29716232280595
  s1.distance(0.0, 0.0, 200.0, 108.0)             //> res4: Double = 227.29716232280595
  s1.gs.map{g => (g.id, s1.distance(0.0, 0.0, g.x, g.y).toInt) }
                                                  //> res5: List[(Int, Int)] = List((9,228), (8,210), (7,201), (6,187), (5,243), (
                                                  //| 4,232), (3,219), (2,217), (1,276), (0,313))
}