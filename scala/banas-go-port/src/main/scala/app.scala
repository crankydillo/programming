object app extends App {

  // variables
  {
    var age = 15
    var favNum = 1.6180
    var randNum = 1

    // Scala compiler won't error due to unused variables.
    // Of course, most people are using all kinds of tools to analyze
    // their code.  The Scala compiler does some of that, and I've
    // enabled it the build tool I'm using for this (SBT 1.X)
    // Of course, a lot just rely on their IDE.
    println(randNum)
  }
  println()

  // constants
  {
    val pi: Float = 3.14f
    println(pi)
    // Need to find out what var ( var a = 5 ) means...
  }
  println()

  // Skipping string/bools..  Like above, it is similar.
  
  // loops
  {
    var i = 1
    while (i <= 10) { println(i); i += 1 }
    println()
    for (j <- 1 to 10) { println(j) }
  }
  println()
 
  {
    var yourAge = 18
    if (yourAge >= 16) println("You can drive")
    else if (yourAge >= 18) println("You can vote")
    else println("Go have fun")
    println()

    yourAge match {
      case 16 => println("You can drive")
      case 18 => println("You can't drive")
      case _  => println("Go have fun")
    }

    // But everything-is-an-expression makes your code so much DRYer..
    // You can probably get this with go, but you'll have to use lambdas
    // surrounding statements?  When I saw his demo, this is what made
    // me want to do a comparison of this simple stuff to an expression-based
    // language.
    println(if (yourAge >= 16) "You can drive" else "Go have fun")
  }
  println()

  // arrays
  {
    val favNums = 
      Array(
        163,
        78557
      )

    println(favNums(1))

    for ( (value, i) <- favNums.zipWithIndex ) { println((i+1) + ". " + value) }
    println()
    for ( (value, _) <- favNums.zipWithIndex ) { println(value) }
    println()
    for ( value <- favNums) { println(value) }
  }
  println()

  // slices  I'm not really sure what a 'slice' is so I'll have to read about that.
  // However, I can reproduce this stuff with arrays.
  // AFAIK, Scala doesn't have this slice syntax.  However, what it does have are
  // methods that are applicable across many data types..  Which do you think is
  // better?
  {
    val slice1 = Array(5, 4, 3, 2, 1)
    println(slice1.slice(3, 5).toList) // toList gets us a nice toString
    println(slice1.take(2).toList)

    // Slight mod to https://stackoverflow.com/a/34863496
    var slice2 = Array.ofDim[Int](10)
    println(slice2.toList)
    Array.copy(slice1, 0, slice2, 0, slice1.size) // is there better?
    println(slice2.toList)

    // Not sure of direct translation here
    // slice2 = slice2 ++ Array(0, -1) This 'works' if initial dim was 5
    //
    // This isn't nice, but I do think it's the same as the go one..
    // Don't know if I'm missing a feature.  I don't do this (i.e. mutate) a lot.
    // actually go's append is returning a new array?
    // TODO revisit this
    Array.copy(Array(0, -1), 0, slice2, 5, 2)
    println(slice2(6))
  }
  println()

  // Mutable Maps
  {
    val presAge = new scala.collection.mutable.HashMap[String, Int]
    presAge("Teddy") = 42
    println(presAge.size)
    presAge("JFK") = 43
    println(presAge.size)
    presAge.remove("JFK")
    println(presAge.size)
  }
  println()

  // Immutable Maps
  {
    val presAge = 
      Map(
        "Teddy" -> 42,
        "JFK"   -> 43
      )
    println(presAge.size)
    println(presAge("JFK"))

    // probably should have a 'remove' method, but I can understand why they
    // don't as it implies mutation.
    println(presAge - "JFK") 
  }
  println()

  // functions
  {
    def addThemUp(numbers: Array[Float]): Float = {
      var sum = 0.0f
      for (value <- numbers) { sum += value }
      return sum // 'return' is optional (and discouraged in expression-based langs)
    }

    val array = Array[Float](1, 2, 3, 4, 5)
    println(addThemUp(array))
    // or
    println(array.sum)
  }

  // functions returning two values (i.e. pairs, or more generally tuples)
  // Contrary to the video claims, a lot of languages DO allow
  // for tuples
  {
    def next2Values(i: Int): (Int, Int) = {
      (i+1, i+2)   // leaving off 'return' from now on
    }

    // I hope there's some reason for the overloaded `fmt.Println`
    val (num1, num2) = next2Values(5)
    println(s"$num1 $num2")
  }

  // Varargs
  {
    println(subtractThem(1, 2, 3, 4, 5))

    def subtractThem(is: Int*): Int = {
      var finalValue = 0
      for (i <- is) { finalValue -= i }
      finalValue
    }
  }

  // closures
  {
    var num3 = 3

    val doubleNum = () => {
      num3 *= 2
      num3
    }

    println(num3)
    println(doubleNum())
    println(doubleNum())
  }

  // recursion
  {
    println(factorial(5))
    //println(factorial(100000)) this *could* blow your stack
    println(tailFac(10000)) // this will handle bigger numbers, but I don't want to wait

    // I'm going to get more terse...
    def factorial(n: BigInt): BigInt = if (n == 0) 1 else n * factorial(n - 1)

    // Should you even show recursion if you can't achieve tail (either
    // explicitly or via compiler magic?)
    def tailFac(num: BigInt): BigInt = {
      @scala.annotation.tailrec
      def helper(n: BigInt, runningTotal: BigInt): BigInt = {
        if (n == 0) runningTotal
        else helper(n - 1, runningTotal * n)
      }
      helper(num, 1)
    }
  }

  // defer/recover
  {
    // AFAIK, Scala has nothing like go's defer, so I implemented something
    // _like_ it in the deferral object below.
    import deferral._

    println(safeDiv(3, 0))
    println(safeDiv(3, 2))

    def safeDiv(i: Int, j: Int): Int = {
      // go seems to chooses a default value?  What happens with struct, nil?
      defer(e => /*println(recover(e));*/ 0) {
        i / j
      }
    }

    defer(e => println(recover(e))) {
      panic("PANIC")
    }
  }

  // pointers... I got nothing.  scala-native?  JNI?
  
  // classes
  {
    case class Rectangle(leftX: Float, topY: Float, height: Float, width: Float)
    val rect1 = Rectangle(leftX = 0, topY = 50, height = 10, width = 10)
    val rect2 = Rectangle(0, 50, 10, 10)
    println(s"Rectangle is ${rect2.width} wide")

    def area(r: Rectangle) = r.width * r.height

    println(area(rect2))
  }

  // 'type' classes
  {
    // No native support, but there are some options..
    case class Rectangle(height: Double, width: Double)
    case class Circle(radius: Double)

    trait `2d-shape` {
      def area(): Double
    }

    implicit def rectToshape(r: Rectangle) = new `2d-shape` {
      override def area(): Double = r.width * r.height
    }
    implicit def circleToshape(c: Circle) = new `2d-shape` {
      override def area(): Double = Math.PI * Math.pow(c.radius, 2)
    }

    def getArea(s: `2d-shape`) = s.area()

    println(getArea(Rectangle(20, 50)))
    println(getArea(Circle(4)))
  }

  // String conveniences..weird to come back to this..
  {
    val s = "Hello Worlld l"
    println(s.contains("lo"))
    println(s.indexOf("lo"))
    println(s.filter(_ == 'l').size)

    // A built-in replace n chars is one of the first 'simple'
    // things that Go has that Scala lacks.  Can solve with functional
    // or imperative styles.  If you want to use implicits, they could
    // be used to add a replace(char, char, int) method to Strings.
    println(
      s.foldLeft (("", 0)) { case ((memo, ctr), char) =>
        if (char == 'l' && ctr < 3) (memo + 'x', ctr + 1) else (memo + char, ctr)
      }._1
    )

    // He mentions error in unused import.  Doesn't happen by default, but see
    // note above about unused vars.
   
    val csv = "1,2,3,4,5"
    println(csv.split(",").toList)

    val listOfLetters = List('c', 'a', 'b')
    println(listOfLetters.sorted)

    val listOfNums = List('3', '2', '1').mkString(", ")
    println(listOfNums)
  }

  // File IO
  
  // Finish (i.e. get to the interesting stuff)
  
  object deferral {
    import scala.util.Try
    // Best I can do at this time..  Look at scalatest impl.
    def defer[T](fn1:  Option[Throwable] => T)(fn2: => T): T = {
      val tryfn2T = Try { fn2 }
      val fn1T = fn1(tryfn2T.toEither.left.toOption)
      tryfn2T.getOrElse(fn1T)
    }
    def recover(e: Option[Throwable]) = e.map(_.getMessage)

    defer(_ => println(2)) { println(1) }  // How to remove `:Try[_]`?

    def panic(msg: String) = throw new Exception(msg)
  }
}

