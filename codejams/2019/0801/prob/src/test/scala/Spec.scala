import org.scalatest._

class Spec extends FunSpec with Matchers {

  describe("Something") {
    it("should work") {
      true should equal(false)
    }
  }
}
