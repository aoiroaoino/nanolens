package dev.aoiroaoino.nanolens

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalacheck.{Arbitrary, Gen}

class LensSpec extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {

  case class Foo(value: String)
  def lens = Lens[Foo, String](_.value)(f => v => f.copy(value = v))

  given Arbitrary[Foo] = Arbitrary(Gen.alphaStr.map(Foo(_)))

  test("get/set") {
    forAll { (s: Foo) =>
      assertResult(expected = s)(actual = lens.set(lens.get(s))(s))
    }
  }
  test("set/get") {
    forAll { (s: Foo, a: String) =>
      assertResult(expected = a)(actual = lens.get(lens.set(a)(s)))
    }
  }
  test("set/set") {
    forAll { (s: Foo, a1: String, a2: String) =>
      assertResult(expected = lens.set(a2)(s))(actual = lens.set(a2)(lens.set(a1)(s)))
    }
  }
}
