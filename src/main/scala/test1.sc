object test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val conf = com.typesafe.config.ConfigFactory.parseString("foo=42")
                                                  //> conf  : com.typesafe.config.Config = Config(SimpleConfigObject({"foo":42}))
  conf.getInt("foo")                              //> res0: Int = 42
    
  
}