package example

import java.io._
import java.math._
import java.security._
import java.text._
import java.util.concurrent._
import java.util.function._
import java.util.regex._
import java.util.stream._

// Problem (as I remember it, can't seem to link to it:()..
// Given a string, return "YES", if:
// 1. All characters occur with the same frequency or
// 2. If you can subtract only one character (at one index) and 1 holds
// 3. Otherwise, return "NO"
object Solution {

    // Complete the isValid function below.
    def isValid(s: String): String = {
        if (s.trim.length == 0 || s.length == 1) return "YES"

        val grouped = 
          s.toList
            .groupBy   { c => c }
            .mapValues { _.size }
            .groupBy   { _._2 }
            .toList
            .sortWith  { case ((cnt1, _), (cnt2, _)) => cnt1 > cnt2 }

        println(grouped)
        if (grouped.size <= 1) "YES"
        else if (grouped.size == 2 && 
          grouped(0)._2.size == 1 &&
          grouped(0)._1 - grouped(1)._1 == 1) "YES"
        else "NO"
    }

    def main(args: Array[String]) {
      List(
        "a",
        "ab",
        "aab",
        "abb",
        "abbc",
        "aabbccc",
        "aabbbccc",
        "aaaaabbbccc"
        ).foreach { s => println(isValid(s)) }
    }
}

