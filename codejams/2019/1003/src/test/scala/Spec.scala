import scala.util.Random

import org.scalatest._
import org.scalatest.prop.Checkers
import org.scalacheck.Arbitrary._
import org.scalacheck.Test
import org.scalacheck.Prop._
import org.scalacheck.Shrink
import org.scalacheck.Gen
import org.scalatest.prop.Checkers
import org.scalacheck.Arbitrary.arbitrary

class Spec extends FunSpec with Matchers with Checkers {

  import Result.cracker

  implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny

  val iters = 1 * 1000

  private val passwordGen: Gen[List[String]] =
    Gen.containerOf[List, String](
      Gen.listOfN(new Random().nextInt(99) + 1, Gen.alphaChar)
        .map(_.mkString(""))
        .filter(_.trim != "")
        .map(s => s.take(new Random().nextInt(s.size - 1)+ 1))
    ).filter(_.nonEmpty)
      .map(_.toList)
      .map { l =>
        (l ++ l.sliding(2, 4).toList.flatten.filter { _.trim != "" }).distinct
      }.filter(_.nonEmpty) 

  describe("Something") {

    it("should work") {
      check(forAll(passwordGen) {
        case passwords =>
          val loginAttempt = validPassword(passwords)
          val r = cracker(passwords.toArray, loginAttempt)
          r.replaceAll(" ", "") == loginAttempt
      }, Test.Parameters.default.withMinSuccessfulTests(iters))
    }

    it("should not work") {
      // We use a valid passwords for the generate password list; however, we modify that
      // password list such that it becomes invalid. 
      check(forAll(passwordGen) { case passwords =>
        val usedPasswords = passwords.map { pw => (pw(0) + 1).toChar.toString  + pw.drop(1) }
        val r = cracker(usedPasswords.toArray, validPassword(passwords))
        r == "WRONG PASSWORD"
      }, Test.Parameters.default.withMinSuccessfulTests(iters))
    }
  }

  def validPassword(passwords: List[String]): String = {
    Random.shuffle(passwords)
      .take(new Random().nextInt(50)+1)
      .permutations
      .take(new Random().nextInt(50)+1)
      .toList
      .map(l => Random.shuffle(l))
      .flatten
      .mkString("")
  }
}
