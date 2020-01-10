import scala.collection.immutable._

object Solution {

  def mbs(q: Array[Int]): Unit = {
    val toRight = new scala.collection.mutable.HashSet[Int]
    var highest = 0
    var i = 0
    var moves = 0
    var invalid = false
    var prev = 0
    while (!invalid && i < q.size) {
      val curr = q(i)
        if (toRight.contains(curr)) {
          toRight.remove(curr)
          if (toRight.nonEmpty && curr > toRight.head) moves += 1
        } else {
          (if (prev == 0) (1 until curr) else (highest+1 until curr)).foreach(toRight.add _)
          if (toRight.size > 2) {
            invalid = true
          } else {
            moves += toRight.size
          }
        }
      prev = curr
      if (highest < curr) highest = curr
      i += 1
    }

    if (invalid) {
      println("Too chaotic")
    } else {
      println(moves)
    }

  }

  def main(args: Array[String]): Unit = {
    List(
      Array(2, 1, 5, 3, 4),
      Array(2, 5, 1, 3, 4),
      Array(1, 2, 5, 3, 7, 8, 6, 4),
    ).foreach(mbs)
  }
}
