package jam

import java.io._
import java.math._
import java.security._
import java.text._
import java.util._
import java.util.concurrent._
import java.util.function._
import java.util.regex._
import java.util.stream._

/**
 * For a given set of integers find the largest subset such that for any two
 * numbers in the subset, there sum is not evenly divisible by a given integer,
 * k.
 */
object Solution {

  // I'm pretty sure this works, because it is basically a codification of the
  // formula, but of course it does not work efficiently.  I think I'm
  // underestimating how quickly the hour goes by.  Probably won't be able to
  // do this type of thing and then optimize:(
  // 
  // Anyhow, for all test cases, this either got the right answer or timed out.

 def subsets(l: scala.List[Int]): scala.Stream[scala.List[Int]] = {
    (0 to l.length)
        .toStream
        .flatMap(i =>
          l.combinations(l.length - i).toStream
        )
  }

  // Complete the nonDivisibleSubset function below.
  def nonDivisibleSubset(k: Int, S: Array[Int]): Int = {
    subsets(S.toList)
        .find(l => {
          !l.combinations(2).find {
            case scala.List(a, b) => (a + b) % k == 0
          }.isDefined
        })
        .head
        .size
  }

  def main(args: Array[String]) {
    val stdin = scala.io.StdIn

    val printWriter = new PrintWriter(sys.env("OUTPUT_PATH"))

    val nk = stdin.readLine.split(" ")
    val n = nk(0).trim.toInt
    val k = nk(1).trim.toInt

    val S = stdin.readLine.split(" ").map(_.trim.toInt)
    val result = nonDivisibleSubset(k, S)

    printWriter.println(result)

    printWriter.close()
  }
}

import org.scalatest._

class Spec extends FunSpec with Matchers {

  describe("Something") {
    it("should work") {
      val l = scala.List(19, 10, 12, 10, 24, 25, 22)
      val k = 4
      Solution.nonDivisibleSubset(k, l.toArray) should equal(3)

      val l2 = scala.List(278, 576, 496, 727, 410, 124, 338, 149, 209, 702, 282, 718, 771, 575, 436)
      Solution.nonDivisibleSubset(7, l2.toArray) should equal(11)
    }
  }
}
