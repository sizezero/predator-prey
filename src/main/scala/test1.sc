object test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  var l = List(1,2)                               //> l  : List[Int] = List(1, 2)
  //l += 3
  l = l :+ 4
  l                                               //> res0: List[Int] = List(1, 2, 4)
}