package org.kleemann.predprey.model

object test2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val s1 = SimulationFactory.random1              //> s1  : org.kleemann.predprey.model.SimulationImp = org.kleemann.predprey.mode
                                                  //| l.SimulationImp@2b4b2d2d
  s1.gs                                           //> res0: List[org.kleemann.predprey.model.Grass] = List(Grass(49,Location(117.0
                                                  //| ,15.0)), Grass(48,Location(60.0,76.0)), Grass(47,Location(4.0,64.0)), Grass(
                                                  //| 46,Location(38.0,51.0)), Grass(45,Location(7.0,12.0)), Grass(44,Location(98.
                                                  //| 0,64.0)), Grass(43,Location(149.0,47.0)), Grass(42,Location(86.0,35.0)), Gra
                                                  //| ss(41,Location(20.0,39.0)), Grass(40,Location(5.0,2.0)), Grass(39,Location(1
                                                  //| 32.0,60.0)), Grass(38,Location(78.0,13.0)), Grass(37,Location(19.0,5.0)), Gr
                                                  //| ass(36,Location(106.0,7.0)), Grass(35,Location(17.0,34.0)), Grass(34,Locatio
                                                  //| n(70.0,38.0)), Grass(33,Location(38.0,28.0)), Grass(32,Location(22.0,43.0)),
                                                  //|  Grass(31,Location(142.0,10.0)), Grass(30,Location(47.0,28.0)), Grass(29,Loc
                                                  //| ation(134.0,10.0)), Grass(28,Location(115.0,79.0)), Grass(27,Location(44.0,9
                                                  //| .0)), Grass(26,Location(18.0,73.0)), Grass(25,Location(26.0,47.0)), Grass(24
                                                  //| ,Location(62.0,96.0)), Grass(23,Location(80.0,3.0)), Grass(22,Location(45.0,
                                                  //| 49.0)), Grass(21,Locatio
                                                  //| Output exceeds cutoff limit.
  val ul = Location(0.0, 0.0)                     //> ul  : org.kleemann.predprey.model.Location = Location(0.0,0.0)
  val br = Location(300.0, 200.0)                 //> br  : org.kleemann.predprey.model.Location = Location(300.0,200.0)
  Thing.closestGrass(s1.gs, ul)                   //> res1: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(40,L
                                                  //| ocation(5.0,2.0)),5.385164807134504))
  Thing.closestGrass(s1.gs, br)                   //> res2: Option[(org.kleemann.predprey.model.Grass, Double)] = Some((Grass(43,L
                                                  //| ocation(149.0,47.0)),214.96511344867102))
  Thing.distance(ul, br)                          //> res3: Double = 360.5551275463989
  Thing.distance(ul, br)                          //> res4: Double = 360.5551275463989
  s1.gs.map{g => (g.id, Thing.distance(ul, g.loc).toInt) }
                                                  //> res5: List[(Int, Int)] = List((49,117), (48,96), (47,64), (46,63), (45,13), 
                                                  //| (44,117), (43,156), (42,92), (41,43), (40,5), (39,144), (38,79), (37,19), (3
                                                  //| 6,106), (35,38), (34,79), (33,47), (32,48), (31,142), (30,54), (29,134), (28
                                                  //| ,139), (27,44), (26,75), (25,53), (24,114), (23,80), (22,66), (21,95), (20,1
                                                  //| 03), (19,99), (18,52), (17,57), (16,75), (15,122), (14,118), (13,51), (12,10
                                                  //| 2), (11,124), (10,65), (9,89), (8,32), (7,89), (6,45), (5,61), (4,110), (3,8
                                                  //| 3), (2,73), (1,56), (0,86))
                                                  
  var s: Set[Int] = Set()                         //> s  : Set[Int] = Set()
  s += 1
  s = s + 2
  s                                               //> res6: Set[Int] = Set(1, 2)
  
  val locs = s1.gs.map{ g => g.loc }              //> locs  : List[org.kleemann.predprey.model.Location] = List(Location(117.0,15.
                                                  //| 0), Location(60.0,76.0), Location(4.0,64.0), Location(38.0,51.0), Location(7
                                                  //| .0,12.0), Location(98.0,64.0), Location(149.0,47.0), Location(86.0,35.0), Lo
                                                  //| cation(20.0,39.0), Location(5.0,2.0), Location(132.0,60.0), Location(78.0,13
                                                  //| .0), Location(19.0,5.0), Location(106.0,7.0), Location(17.0,34.0), Location(
                                                  //| 70.0,38.0), Location(38.0,28.0), Location(22.0,43.0), Location(142.0,10.0), 
                                                  //| Location(47.0,28.0), Location(134.0,10.0), Location(115.0,79.0), Location(44
                                                  //| .0,9.0), Location(18.0,73.0), Location(26.0,47.0), Location(62.0,96.0), Loca
                                                  //| tion(80.0,3.0), Location(45.0,49.0), Location(30.0,91.0), Location(96.0,38.0
                                                  //| ), Location(88.0,47.0), Location(44.0,28.0), Location(30.0,49.0), Location(3
                                                  //| 8.0,65.0), Location(110.0,54.0), Location(117.0,18.0), Location(21.0,47.0), 
                                                  //| Location(97.0,32.0), Location(123.0,16.0), Location(25.0,61.0), Location(72.
                                                  //| 0,53.0), Location(28.0,1
                                                  //| Output exceeds cutoff limit.
  val p: Map[(Int,Int),List[Location]] = locs.groupBy[(Int,Int)]{ loc => ((loc.x/10).toInt * 10, (loc.y/10).toInt * 10) }
                                                  //> p  : Map[(Int, Int),List[org.kleemann.predprey.model.Location]] = Map((0,60)
                                                  //|  -> List(Location(4.0,64.0)), (60,90) -> List(Location(62.0,96.0)), (100,0) 
                                                  //| -> List(Location(106.0,7.0)), (20,60) -> List(Location(25.0,61.0)), (0,0) ->
                                                  //|  List(Location(5.0,2.0)), (30,50) -> List(Location(38.0,51.0)), (80,20) -> L
                                                  //| ist(Location(81.0,20.0)), (30,60) -> List(Location(38.0,65.0)), (0,10) -> Li
                                                  //| st(Location(7.0,12.0)), (80,40) -> List(Location(88.0,47.0)), (140,10) -> Li
                                                  //| st(Location(142.0,10.0)), (40,0) -> List(Location(44.0,9.0), Location(45.0,3
                                                  //| .0)), (40,50) -> List(Location(45.0,58.0)), (20,30) -> List(Location(20.0,39
                                                  //| .0)), (70,10) -> List(Location(78.0,13.0)), (100,40) -> List(Location(100.0,
                                                  //| 47.0)), (90,60) -> List(Location(98.0,64.0)), (110,10) -> List(Location(117.
                                                  //| 0,15.0), Location(117.0,18.0)), (80,30) -> List(Location(86.0,35.0), Locatio
                                                  //| n(81.0,39.0)), (50,30) -> List(Location(54.0,30.0)), (20,40) -> List(Locatio
                                                  //| n(22.0,43.0), Location(2
                                                  //| Output exceeds cutoff limit.
  for (
    j <- 0 until 150 by 10;
    i <- 0 until 100 by 10
  ) yield p.getOrElse( (i,j), List()).size        //> res7: scala.collection.immutable.IndexedSeq[Int] = Vector(1, 1, 0, 0, 2, 0, 
                                                  //| 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 1, 0, 0, 1
                                                  //| , 1, 0, 1, 1, 0, 1, 2, 2, 0, 0, 3, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0,
                                                  //|  1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 
                                                  //| 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                                                  //| , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                                  //|  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
  for ( j <- 0 until 100 by 10) {
    val s = (for(i <- 0 until 150 by 10) yield p.getOrElse( (i,j), List()).size.toString).mkString
    System.out.println(s)
  }                                               //> 110020001010000
                                                  //| 101000010002111
                                                  //| 000120001000000
                                                  //| 011011012200000
                                                  //| 003110001010001
                                                  //| 000110010001000
                                                  //| 101100000100010
                                                  //| 010000100001000
                                                  //| 001000000000000
                                                  //| 000100100000000
                                                  
  (for ( q <- 1 to 10) yield q.toString).mkString //> res8: String = 12345678910
}