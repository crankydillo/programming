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

  def notifs(exps: List[Int], d: Int): Int = {
    exps.sliding(d+1)
      .count { window => window.last >= 2 * median(window.take(window.size - 1)) }
  }

  def notifs(exps: Array[Int], d: Int): Int = {
    notifs(exps.toList, d)
  }

  def main(args: Array[String]) {

    val rand = new java.util.Random()

    println(notifs(
      (0 to 20000).toList.map { _ => Math.abs(rand.nextInt) },
      2
    ))

    println(notifs(Array(1, 2, 3, 4, 4), 4))
  }
}
