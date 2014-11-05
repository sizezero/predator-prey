package org.kleemann.predprey.model

object test2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val s1 = SimulationFactory.random1              //> s1  : org.kleemann.predprey.model.SimulationImp = org.kleemann.predprey.mode
                                                  //| l.SimulationImp@31b3c607
  s1.gs                                           //> res0: List[org.kleemann.predprey.model.Grass] = List(Grass(49,Location(94.0,
                                                  //| 68.0)), Grass(48,Location(65.0,97.0)), Grass(47,Location(58.0,39.0)), Grass(
                                                  //| 46,Location(87.0,89.0)), Grass(45,Location(13.0,97.0)), Grass(44,Location(92
                                                  //| .0,50.0)), Grass(43,Location(48.0,32.0)), Grass(42,Location(59.0,7.0)), Gras
                                                  //| s(41,Location(66.0,80.0)), Grass(40,Location(12.0,54.0)), Grass(39,Location(
                                                  //| 149.0,70.0)), Grass(38,Location(57.0,84.0)), Grass(37,Location(113.0,69.0)),
                                                  //|  Grass(36,Location(76.0,33.0)), Grass(35,Location(101.0,57.0)), Grass(34,Loc
                                                  //| ation(123.0,62.0)), Grass(33,Location(124.0,13.0)), Grass(32,Location(134.0,
                                                  //| 47.0)), Grass(31,Location(40.0,22.0)), Grass(30,Location(67.0,60.0)), Grass(
                                                  //| 29,Location(8.0,81.0)), Grass(28,Location(15.0,68.0)), Grass(27,Location(144
                                                  //| .0,67.0)), Grass(26,Location(123.0,40.0)), Grass(25,Location(125.0,47.0)), G
                                                  //| rass(24,Location(112.0,78.0)), Grass(23,Location(29.0,33.0)), Grass(22,Locat
                                                  //| ion(92.0,76.0)), Grass(2
                                                  //| Output exceeds cutoff limit.
  val ul = Location(0.0, 0.0)                     //> ul  : org.kleemann.predprey.model.Location = Location(0.0,0.0)
  val br = Location(300.0, 200.0)                 //> br  : org.kleemann.predprey.model.Location = Location(300.0,200.0)
  Thing.closestGrass(s1.gs, ul)                   //> res1: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(16,L
                                                  //| ocation(32.0,24.0)),40.0))
  Thing.closestGrass(s1.gs, br)                   //> res2: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(11,L
                                                  //| ocation(146.0,93.0)),187.52333188166213))
  Thing.distance(ul, br)                          //> res3: Double = 360.5551275463989
  Thing.distance(ul, br)                          //> res4: Double = 360.5551275463989
  s1.gs.map{g => (g.id, Thing.distance(ul, g.loc).toInt) }
                                                  //> res5: List[(Int, Int)] = List((49,116), (48,116), (47,69), (46,124), (45,97)
                                                  //| , (44,104), (43,57), (42,59), (41,103), (40,55), (39,164), (38,101), (37,132
                                                  //| ), (36,82), (35,115), (34,137), (33,124), (32,142), (31,45), (30,89), (29,81
                                                  //| ), (28,69), (27,158), (26,129), (25,133), (24,136), (23,43), (22,119), (21,1
                                                  //| 26), (20,97), (19,106), (18,49), (17,107), (16,40), (15,124), (14,80), (13,1
                                                  //| 53), (12,124), (11,173), (10,129), (9,154), (8,42), (7,91), (6,97), (5,52), 
                                                  //| (4,117), (3,84), (2,114), (1,106), (0,66))
                                                  
  var s: Set[Int] = Set()                         //> s  : Set[Int] = Set()
  s += 1
  s = s + 2
  s                                               //> res6: Set[Int] = Set(1, 2)
}