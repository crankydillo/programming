import org.scalatest._

class Spec extends FunSpec with Matchers {

  import Solution._

  describe("games") {

    it("intro.1") {
      val portals = Map(
        (32, 62),
        (42, 68),
        (12, 98),

        (95, 13),
        (97, 25),
        (93, 37),
        (79, 27),
        (75, 19),
        (49, 47),
        (67, 17)
      )

      play(portals) should be(3)
    }

    it("intro.2") {
      val portals = Map(
        (8, 52),
        (6, 80),
        (26,42),
        (2, 72),

        (51, 19),
        (39, 11),
        (37, 29),
        (81, 3),
        (59, 5),
        (79, 23),
        (53, 7),
        (43, 33),
        (77, 21)
      )

      play(portals) should be(5)
    }

    it("1.1") {
      play(Map(
        (3, 54),
        (37, 100),
        (56, 33)
      )) should be(3)
    }

    it("1.2") {
      play(Map(
        (3, 57),
        (8, 100),
        (88, 44)
      )) should be(2)
    }

    it("1.3") {
      val portals = Map(
        (7,98),
        (99,1)
      )

      play(portals) should be(2)
    }

    it("3.1") {
      play(Map(
        (3, 5),
        (7, 8),
        (44, 56),
        (36, 54),
        (88, 91),
        (77, 83),
        (2, 4),
        (9, 99),
        (45, 78),
        (31, 75),
        (10, 6),
        (95, 90),
        (96, 30),
        (97, 52),
        (98, 86),
      )) should be(3)
    }

    it("4.1") {
      play(Map(
        (3, 90),
        (99, 10),
        (97, 20),
        (98, 30),
        (96, 40),
        (95, 50),
        (94, 60),
        (93, 70),
      )) should be(-1)
    }
  }
}
