import scala.collection.immutable._

object Solution {

  import scala.collection.mutable.TreeMap

  def size1Window(exps: Array[Int]): Int = {
    exps.sliding(2)
      .count(a => a(1) >= 2 * a(0))
  }

  def size2Window(exps: Array[Int]): Int = {
    exps.sliding(3)
      .count(a => a(2) >= 2 * ((a(0) + a(1)) / 2.0))
  }

  def notifs(exps: Array[Int], d: Int): Int = {

    if (d == 1) {
      return size1Window(exps)
    }

    if (d == 2) {
      return size2Window(exps)
    }

    val size = exps.size
    var res = 0

    val medianPointers: (TreeMap[Int, Int], TreeMap[Int, Int]) = {
      val sort = exps.take(d).sorted
      val size = sort.size
      val midPoint = if (size % 2 == 1) (size / 2) + 1 else (size / 2)
      val (left, right) = sort.splitAt(midPoint)
      def mkTreeMap(arr: Array[Int]) = {
        val m = new TreeMap[Int, Int]
        arr.foreach { i =>
          m.get(i) match {
            case Some(cnt) => m.put(i, cnt + 1)
            case _         => m.put(i, 1)
          }
        }
        m
      }
      (mkTreeMap(left), mkTreeMap(right))
    }

    val (left, right) = medianPointers

    var idx = d

    def sz(m: TreeMap[Int, Int]) = m.values.sum

    while (idx < size) {
//      println(left)
//      println(right)
      val leftV = left.last._1
      val rightV = right.head._1
      val curr = exps(idx)

      val median = if (d % 2 == 1) {
        if (sz(left) > sz(right)) leftV else rightV
      } else {
        (leftV + rightV) / 2.0
      }

//      println(s"$leftV - $rightV - $median")

      if (curr >= 2 * median) {
        res += 1
      }

      val dropped = exps(idx - d)

      val removeFrom = if (dropped <= leftV) left else right
      removeFrom.get(dropped) match {
        case Some(1) => removeFrom.remove(dropped)
        case Some(n) => removeFrom.put(dropped, n - 1)
      }

      val addTo = if (curr <= leftV) left else right
      addTo.get(curr) match {
        case Some(n) => addTo.put(curr, n + 1)
        case _ => addTo.put(curr, 1)
      }

      val szLeft = sz(left)
      val szRight = sz(right)
      if (szRight - szLeft > 1) {
          left.get(rightV) match {
            case Some(n) => left.put(rightV, n + 1)
            case _ => left.put(rightV, 1)
          }
          right.get(rightV) match {
            case Some(1) => right.remove(rightV)
            case Some(n) => right.put(rightV, n - 1)
            case _ =>
          }
        } else if (szLeft - szRight > 1) {
          right.get(leftV) match {
            case Some(n) => right.put(leftV, n + 1)
            case _ => right.put(leftV, 1)
          }
          left.get(leftV) match {
            case Some(1) => left.remove(leftV)
            case Some(n) => left.put(leftV, n - 1)
            case _ =>
          }
        }

      idx += 1
    }

    res
  }

  def main(args: Array[String]) {

    val rand = new java.util.Random()

    List(
      //(1000, (0 to 200000).toArray.map { _ => Math.abs(rand.nextInt(2000)) }),
//      (6, (0 to 12).toArray.map { _ => Math.abs(new java.util.Random().nextInt(20000)) }),
//      (3,Array(0, 0, 3, 1, 3)),
//      (3, Array(0, 1, 1, 0, 0)),
//      (3,Array(0, 0, 0, 1, 1, 0)),
      (5, Array(0, 0, 0, 3, 3, 3, 1, 3))
      //(3, Array(0, 0, 1, 1, 0))
      //(3, Array(30, 20, 10, 40, 50)),
//      (3, Array(10, 20, 30, 40, 50)),
//      (2, Array(10, 20, 30, 40, 150)),
//      (4, Array(1, 3, 3, 3, 4, 7, 1, 1, 1)),
//      (6, Array(1, 3, 2, 4, 50, 3, 404)),
//      (3, Array(1, 2, 3, 4, 50, 3, 404)),
//      (2, Array(1, 3, 2, 4, 50, 3, 404)),
//      (5, Array(2, 3, 4, 2, 3, 6, 8, 4, 5))
//      (4, Array(1, 2, 3, 4, 4))
    ).foreach { case (n, exps) =>
      println(notifs(exps, n))
    }
  }
}
