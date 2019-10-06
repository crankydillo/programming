import scala.collection.immutable._
import scala.collection._
import scala.collection.concurrent._
import scala.collection.parallel.immutable._
import scala.collection.parallel.mutable._
import scala.concurrent._
import scala.io._
import scala.math._
import scala.sys._
import scala.util.matching._
import scala.reflect._

object Result {

   import scala.collection.JavaConverters._

  // ScalaCheck Generators saved the day for this solution.
  // It found my last bug after running 250K huge lists through
  // this function.
  def cracker(passwords: Array[String], loginAttempt: String): String = {

    val stack = new java.util.Stack[String]()
    val memo = new mutable.HashSet[(String, String)]

    var buffer = passwords.sortBy { _.size }.reverse.toBuffer
    var ctr = buffer.size
    var str = loginAttempt

    while (ctr > 0 && str != "") {
      var cont = true
      var j = 0
      while (j < buffer.size) {
        val pw = buffer(j)
        while (str.startsWith(pw) && !memo.contains((pw, str))) {
          stack.push(pw)
          str = str.substring(pw.size)
          cont = false
        }
        if (cont && j == buffer.size - 1) {
          if (!stack.isEmpty) {
            val popped = stack.pop()
            memo += ((popped, popped + str))
            str = popped + str
          } else {
            str = loginAttempt
          }
        }
        j += 1
      }
      if (str == loginAttempt) {
        ctr -= 1
      }
    }

    stack.asScala match {
      case s if s.mkString("") == loginAttempt => s.mkString(" ")
      case _ => "WRONG PASSWORD"
    }
  }
}

object Solution {
  import Result._

  def main(args: Array[String]): Unit = {
    //println((0 to 0).map(i => "becausebecaused").mkString(""))
    //
    //println(splitf("becausebecaused", "be"))

    List(
      (
        "otgpcbxsqY x xo",
        //"xotgpcbxsqYxoxoxotgpcbxsqYxootgpcbxsqYxxotgpcbxsqYxoxootgpcbxsqYxotgpcbxsqYxox"
        "xotgpcbxsqYxoxox"
      )
    /*
        (
        "ytcsl",
        "ytcsl"
        ),
      (
        "ab ba b baba aaaaaaaaaaaaaa a",
        "aaaaaaaaaaaaabab"
        ),
      (
        "b",
        "b"
        ),
      (
        "b",
        "a"
        ),
      (
        "bee bb be cause en because word ends d e",
        "bbenendsebbbeecausecauseendsbewordbecausecausedendsbebbendsends"
        ),
      (
        "aa c aba a aaa da ab abb",
        "cabaabaaaabbabb"
        ),
      (
        "sam xcox x",
        "samxcoxsam"
        ),
      (
        "a aa aaa aaaa aaaaa aaaaaa aaaaaaa aaaaaaaa aaaaaaaaa aaaaaaaaaa",
        "babaa"
        ),
      (
        "gurwgrb maqz holpkhqx aowypvopu",
        "gurwgrb"
        ),
      (
        "ozkxyhkcst xvglh hpdnb zfzahm",
        "zfzahm"
        ),
      (
        "because can do must we what",
        "wedowhatwemustbecausewecan"
        ),
      (
        "hello planet",
        "helloworld"
        ),
      (
        "ab abcd cd",
        "abcd"
      ),
      (
        "ause abeca eca b abe c because bec causes abec",
        "abecause"
      ),
    (
      "e use",
      "usee"
    ),
  (
  "abra ka dabra",
  "kaabra"
),
(
  "ab abcd bcd",
  "abcd"
)
*/
      ).foreach { case (passwordsStr, attempt) =>
        println(cracker(passwordsStr.split("""\s+"""), attempt))
      }
  }

  // I was using this in the recursive solutions.  I still have some hope
  // for those.  One definitely would work, it was just too slow.
  def splitf(w: String, sub: String): List[String] = {
    val l = new scala.collection.mutable.ArrayBuffer[String]
    var cur = 0
    var subIdx = w.indexOf(sub)
    val subSize = sub.size
    while(subIdx != -1) {
      if (!(cur == 0 && subIdx == 0)) {
        l += sub
        l += w.substring(cur, subIdx)
      }
      cur = subIdx + subSize
      subIdx = w.indexOf(sub, cur)
    }
    if (cur != w.size && cur != -1) {
      l += sub
      l += w.substring(cur)
    }
    l.toList.filter { _ != "" }
  }



  /*
  def cracker(_passwords: Array[String], _loginAttempt: String): String = {

    val pwSet = _passwords.toSet
    val psSorted = _passwords.sortBy(_.size).reverse
    val memo = new java.util.HashMap[String, List[String]]

    def h(loginAttempt: String): List[String] = {
      if (pwSet.contains(loginAttempt)) return List(loginAttempt)

      if (!pwSet.exists { loginAttempt.startsWith _ }) return Nil

      if (loginAttempt.isEmpty) return Nil

      if (memo.containsKey(loginAttempt)) {
        return memo.get(loginAttempt)
      }

      psSorted
        .toStream
        .filter { loginAttempt.startsWith }
        .map { pw =>
          h(loginAttempt.substring(pw.size)) match {
            case Nil => Nil
            case l   =>
              val ans = pw +: l
              memo.put(loginAttempt, ans)
              ans
          }
        }.filter { _.nonEmpty }
        .headOption
        .getOrElse(Nil)
    }

    h(_loginAttempt) match {
      case Nil => "WRONG PASSWORD"
      case l => l.mkString(" ")
    }
  }

  def cracker2(_passwords: Array[String], _loginAttempt: String): String = {
    val memo = new java.util.HashMap[(Array[String], String), List[String]]()

    def helper(
      passwords: Array[String],
      currString: String,
      answer: List[String]
    ): (String, List[String]) = {
      if (passwords.length == 0) {
        return (currString, answer)
      }
      val firstPassword = passwords.head
      if (firstPassword == currString) {
        return (currString, List(firstPassword))
      }
      val index = currString.indexOf(firstPassword)
      if (index > -1) {
        val (headStr, headAnswers) = helper(passwords.tail, currString.substring(0, index), answer)

        val (midStr, midAnswers) = helper(passwords, firstPassword, answer)

        helper(passwords, currString.substring(index + firstPassword.size), answer) match {
            case ("", words) => (headStr + midStr, headAnswers ++ midAnswers ++ words)
            case stuff => 
              println(stuff)
              val (tailStr, tailAnswers) = helper(passwords.tail, currString.substring(index), answer)
              (headStr + tailStr, headAnswers ++ tailAnswers)
          }
      } else {
        helper(passwords.tail, currString, answer)
      }
    }

    def h(words: Array[String]): String = {
      if (words.isEmpty) return "WRONG PASSWORD"
      helper(words, _loginAttempt, Nil) match {
        //case (s, words) if s.trim.isEmpty && words.mkString("") == _loginAttempt => words.mkString(" ")
        case (s, words) if s.trim.isEmpty => words.mkString(" ")
        case _ => h(words.tail)
      }

    }

    if (_passwords.contains(_loginAttempt)) _loginAttempt
    else helper(_passwords.sortBy(_.size).reverse, _loginAttempt, Nil)
    match {
        //case (s, words) if s.trim.isEmpty && words.mkString("") == _loginAttempt => words.mkString(" ")
        case (s, words) if s.trim.isEmpty => words.mkString(" ")
        case _ => "WRONG PASSWORD"
      }

  }

  def cracker3(_passwords: Array[String], _loginAttempt: String): String = {
    val passwords = _passwords.toList
    val memo = 
      new java.util.HashMap[String, List[String]]()
      //new java.util.concurrent.ConcurrentHashMap[String, List[String]]()
      if (memo.containsKey(_loginAttempt)) {
        return memo.get(_loginAttempt)
      }

    def h(loginAttempt: String, firstPart: String, ans: List[String]): (String, List[String]) = {
      if (loginAttempt == "" || passwords.isEmpty) {
        return (loginAttempt, ans)
      }


      passwords
        .toStream
        .filter  { pw => loginAttempt.startsWith(pw) }
        .map { pw =>
          val newAns = ans :+ pw
          val sub = loginAttempt.substring(0, pw.size)
          val newFirstPart = firstPart + sub
          //memo.put(loginAttempt, newAns)
          h(loginAttempt.substring(pw.size), newFirstPart, newAns)
        }.find { case (p, l) => p == "" }
          .getOrElse(("yup", Nil))
    }

    if (_passwords.contains(_loginAttempt)) {
      _loginAttempt
    } else {
      h(_loginAttempt, "", Nil) match {
        case ("", l) if l.nonEmpty => l.mkString(" ")
        case _   => "WRONG PASSWORD"
      }
    }
  }

  def cracker(_passwords: Array[String], _loginAttempt: String): String = {

    def h(passwords: Array[String], loginAttempt: String): Option[String] = {
      if (passwords.isEmpty) {
        return None
      }
      if (passwords.exists(pw => pw == loginAttempt)) {
        return Some(loginAttempt)
      }

      passwords
        .toStream
        .flatMap { pw => 
          val split = splitf(loginAttempt, pw)
          split match {
            case List(s) if s == pw => Some(s)
            case _ => 
              val l = split
                .map { sub => h(passwords.tail, sub) }
                println(s"pw: $pw, split: $split, l: $l")
              if (l.exists(_ == None)) None
              else Some(l.flatten.mkString(s" $pw "))
          }
        }.headOption
    }

    //println(h(_passwords, _loginAttempt))

    h(_passwords, _loginAttempt).getOrElse("WRONG PASSWORD")
  }
  */
}
