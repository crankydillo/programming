import org.scalatest._
import org.scalacheck.Test
import org.scalacheck.Prop._
import org.scalacheck.Shrink
import org.scalacheck.Gen
import org.scalatest.prop.Checkers

class Spec extends FunSpec with Matchers with Checkers {

  //implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny

  val iters = 1 * 1000

  private val scenarios: Gen[(Int, List[Int])] =
    Gen.containerOf[Array, Int](
      Gen.chooseNum(1, 200000)
    ).flatMap(arr => {
      Gen.chooseNum(1, 1000)
        .map(d => (d, arr.toList))
        .filter { case (d, l) => d > 0 && d < l.size }
    })

  describe("Something") {

    import Solution.notifs

    it("print") {
      println(worksButSlow(Array(1, 0, 0, 0, 1, 0), 4))
      println(worksButSlow(Array(1, 3), 1))
    }

    it("should work") {
      check(forAll(scenarios) {
        case (window, days) =>
          val dayArr = days.toArray
          notifs(dayArr, window) ==
            worksButSlow(dayArr, window)
      }, Test.Parameters.default.withMinSuccessfulTests(iters))

    }
  }


  def median(l: List[Int]): Double = {
    if (l.size == 1) {
      return l(0)
    }
    val sorted = l.sorted
    val size = sorted.size
    if (size % 2 == 1) {
      sorted(size / 2)
    } else {
      val (first, second) = sorted.splitAt(size / 2)
      (first.last + second.head) / 2.0
    }
  }

  def worksButSlow(exps: Array[Int], d: Int): Int = {
    exps.toList
      .sliding(d+1)
      .count { window => window.last >= 2 * median(window.take(window.size - 1)) }
  }

}
