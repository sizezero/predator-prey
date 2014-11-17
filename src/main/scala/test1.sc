object test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val conf = com.typesafe.config.ConfigFactory.parseString("foo=42")
                                                  //> conf  : com.typesafe.config.Config = Config(SimpleConfigObject({"foo":42}))
  conf.getInt("foo")                              //> res0: Int = 42
    
  1.0 to 2.0 by 0.5                               //> res1: scala.collection.immutable.NumericRange[Double] = NumericRange(1.0, 1.
                                                  //| 5, 2.0)
  'a' to 'z'                                      //> res2: scala.collection.immutable.NumericRange.Inclusive[Char] = NumericRange
                                                  //| (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, 
                                                  //| z)
  List(1,2,4)                                     //> res3: List[Int] = List(1, 2, 4)
}