object test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  var indent = 0                                  //> indent  : Int = 0
  val sb = new StringBuilder()                    //> sb  : StringBuilder = 
  for (c <- "foo(bar(12,13),baz('hello'))") {
    c match {
      case '(' => {
        sb.append("(\n")
        indent += 2
        sb.append(" " * indent)
      }
      case ')' => {
        sb.append(")\n")
        indent -= 2
        sb.append(" " * indent)
      }
      case ',' => {
        sb.append(",\n")
        sb.append(" " * indent)
      }
      case _ => sb.append(c)
    }
  }
  sb.toString                                     //> res0: String = "foo(
                                                  //|   bar(
                                                  //|     12,
                                                  //|     13)
                                                  //|   ,
                                                  //|   baz(
                                                  //|     'hello')
                                                  //|   )
                                                  //| "
  
  "foo" * 3                                       //> res1: String = foofoofoo
}