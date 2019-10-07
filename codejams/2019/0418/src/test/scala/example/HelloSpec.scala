package example

import org.scalatest._
import org.scalacheck._
import org.scalacheck.Prop.forAll
import org.scalatest.prop.Checkers

class HelloSpec extends FunSpec with Checkers {

  describe("My solution") {
    it("should work") {

      // Didn't finish this:(
      val charList = for {
        char     <- Gen.alphaChar
        numChars <- Gen.choose(1, 5)
        list     <- (0 to numChars).map(_ => char)
      } yield list

      val charLists =
        Gen.listOf(charList)

      check(
        forAll(charLists) { charList =>
            println(charList)
            true
        }
      )
    }
  }
}
