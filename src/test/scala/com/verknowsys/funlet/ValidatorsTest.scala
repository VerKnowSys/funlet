package com.verknowsys.funlet

import org.scalatest._
import org.scalatest.matchers._

trait TestValidators {
    val Pass: Validator[Int] = e => Nil
    val FailA: Validator[Int] = e => List("Fail B")
    val FailB: Validator[Int] = e => List("Fail A")
    val v = 1
}

class AndValidatorTest extends FlatSpec with ShouldMatchers with TestValidators {
    it should "work" in {
        (Pass | Pass)(v)
        // a(true) should be (Nil)
    }
}

