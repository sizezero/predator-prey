package org.kleemann.predprey.model

object test2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val s1 = SimulationFactory.random1              //> s1  : org.kleemann.predprey.model.SimulationImp = org.kleemann.predprey.mode
                                                  //| l.SimulationImp@4fee5bed
  s1.gs                                           //> res0: List[org.kleemann.predprey.model.Grass] = List(Grass(9,Location(106.0,
                                                  //| 29.0)), Grass(8,Location(168.0,198.0)), Grass(7,Location(61.0,24.0)), Grass(
                                                  //| 6,Location(198.0,104.0)), Grass(5,Location(82.0,82.0)), Grass(4,Location(144
                                                  //| .0,174.0)), Grass(3,Location(255.0,172.0)), Grass(2,Location(205.0,66.0)), G
                                                  //| rass(1,Location(186.0,152.0)), Grass(0,Location(84.0,22.0)))
  val ul = Location(0.0, 0.0)                     //> ul  : org.kleemann.predprey.model.Location = Location(0.0,0.0)
  val br = Location(300.0, 200.0)                 //> br  : org.kleemann.predprey.model.Location = Location(300.0,200.0)
  Thing.closestGrass(s1.gs, ul)                   //> 259.6690201005888 109.89540481748998
                                                  //| 65.55150646628954 109.89540481748998
                                                  //| 223.6515146382872 65.55150646628954
                                                  //| 115.96551211459379 65.55150646628954
                                                  //| 225.85836269662454 65.55150646628954
                                                  //| 307.5857603986244 65.55150646628954
                                                  //| 215.36248512682053 65.55150646628954
                                                  //| 240.20824298928628 65.55150646628954
                                                  //| 86.83317338436964 65.55150646628954
                                                  //| res1: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(7,Lo
                                                  //| cation(61.0,24.0)),65.55150646628954))
  Thing.closestGrass(s1.gs, br)                   //> 132.015150645674 258.6058777367599
                                                  //| 296.8113879216901 132.015150645674
                                                  //| 140.07141035914503 132.015150645674
                                                  //| 247.88707106261108 132.015150645674
                                                  //| 158.15182578775372 132.015150645674
                                                  //| 53.0 132.015150645674
                                                  //| 164.2589419179364 53.0
                                                  //| 123.69316876852982 53.0
                                                  //| 279.89283663573815 53.0
                                                  //| res2: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(3,Lo
                                                  //| cation(255.0,172.0)),53.0))
  Thing.distance(ul, br)                          //> res3: Double = 360.5551275463989
  Thing.distance(ul, br)                          //> res4: Double = 360.5551275463989
  s1.gs.map{g => (g.id, Thing.distance(ul, g.loc).toInt) }
                                                  //> res5: List[(Int, Int)] = List((9,109), (8,259), (7,65), (6,223), (5,115), (4
                                                  //| ,225), (3,307), (2,215), (1,240), (0,86))
}