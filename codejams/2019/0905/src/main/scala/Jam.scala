import java.io._
import java.math._
import java.security._
import java.util.concurrent._
import java.util.function._
import java.util.regex._
import java.util.stream._

object Solution {

  def climb(ladders: Array[Array[Int]], snakes: Array[Array[Int]]): Int = {
    val portals =
      (ladders ++ snakes)
        .map {
          case Array(entry, exit) => (entry, exit)
        }.toMap

      play(portals)
  }

  import scala.collection.mutable

  val dieSize = 6

  // This is pretty brute force.  I want to try breaking the board into mini-boards
  // between the portals.  Solve for those and then combine in some way.
  def play(_portals: Map[Int, Int]): Int = {
    val locRolls = new Array[Int](100).map { _ => Int.MaxValue }
    locRolls(0) = 0
    val portals = _portals.map { case (k,v) => (k-1, v-1) }.toMap
    var answer = -1

    def setLeastRolls(loc: Int, rollsToGetThere: Int): Unit = {
      if (loc < 100 && rollsToGetThere < locRolls(loc)) {
        locRolls(loc) = rollsToGetThere
        if (loc == 99 && (answer == -1 || rollsToGetThere < answer)) {
          answer = rollsToGetThere
        } else if (100 - loc <= dieSize && rollsToGetThere+1 < answer) {
          answer = rollsToGetThere+1
        }
      }
    }

    val seen = new mutable.HashSet[Int]()
    val reversePortalStarts =
      portals.filter { case (k, v) => v < k }.keys.toSet

    def go(startLoc: Int, _incr: Int, _portaled: Boolean = false): Unit = {
      var incr = _incr
      var portaled = _portaled
      if (incr > 17) {
        return
      }
      var i = startLoc
      var dieCtr = dieSize
      var deadEnd = false

      while (!deadEnd && i < 100) {
        
        if ((1 to dieSize).forall { r => reversePortalStarts.contains(i+r) }) {
          deadEnd = true
        } else {
          setLeastRolls(i, incr)
          portals.get(i).foreach { dest =>
            if (!seen.contains(i) || incr < locRolls(dest)) {
              setLeastRolls(dest, incr)
              setLeastRolls(startLoc, incr)
              seen += i
              go(dest+1, incr+1, true)
            }
          }

          i += 1
          dieCtr -= 1
          if (dieCtr == 0) {
            dieCtr = dieSize
            incr += 1
          }
        }
        portaled = false
      }
    }

    go(1, 1)

    answer
  }

  def main(args: Array[String]) {
  }
}
