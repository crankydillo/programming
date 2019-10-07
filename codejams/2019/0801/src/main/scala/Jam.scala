object Solution {

  // Complete the encryption function below.
  def encryption(s: String): String = {
    val len = s.replaceAll("""\s+""", "").length;
    val sqrt = Math.sqrt(len)
    val max = Math.ceil(sqrt)

    val lists = s.toList.grouped(max.asInstanceOf[Int]).toList

    lists.tail.foldLeft(lists.head.map { _.toString }) { (memo, l) =>
        l.zipWithIndex.map { case (c, idx) => memo(idx) + c } ++ memo.drop(l.size)
    }.mkString(" ")
  }

  def main(args: Array[String]) {
    println(encryption("feed the dog"))
  }
}
