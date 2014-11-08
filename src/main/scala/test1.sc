object test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  object Foo {
    def apply(x: Int) = x+1
  }
  Foo(1)                                          //> res0: Int = 2
}