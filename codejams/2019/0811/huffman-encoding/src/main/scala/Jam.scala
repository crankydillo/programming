import scala.annotation.tailrec

object Decoder {


  def decode(s: String, root: Node): Unit = {

    val allChars = s.toCharArray

    @tailrec
    def decodeH(chars: Array[Char], node: Node, result: String): String = {
      if (chars.isEmpty && !node.isInstanceOf[HuffmanLeaf]) return result
      node match {
        case leaf: HuffmanLeaf => decodeH(chars, root, result + leaf.data)
        case tree: HuffmanNode =>
          val child = if (chars.head == '0') tree.left else tree.right
          decodeH(chars.tail, child, result)
      }
    }

    println(decodeH(allChars, root, ""))
  }
}
