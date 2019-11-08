import scala.collection.immutable._

object Solution {

  def median(l: List[Int]): Double = {
    val sorted = l.sorted
    val size = sorted.size
    if (size % 2 == 1) {
      sorted(size / 2)
    } else {
      val (first, second) = sorted.splitAt(size / 2)
      (first.last + second.head) / 2.0
    }
  }

  def notifs(exps: Array[Int], d: Int): Int = {
    exps.toList
      .sliding(d+1)
      .count { window => window.last >= 2 * median(window.take(window.size - 1)) }
  }

  def main(args: Array[String]) {

    val rand = new java.util.Random()

    List(
      (2, (0 to 20000).toArray.map { _ => Math.abs(rand.nextInt) }),
      (4, Array(1, 2, 3, 4, 4)),
      (5, Array(2, 3, 4, 2, 3, 6, 8, 4, 5))
    ).foreach { case (n, exps) =>
      println(notifs(exps, n))
    }
  }
}
