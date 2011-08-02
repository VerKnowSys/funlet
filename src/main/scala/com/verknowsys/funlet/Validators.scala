package com.verknowsys.funlet

object EmptyValidator extends Validator[Any]{
    def apply(value: Any) = Nil
}

trait Validators {
    val NotEmpty: Validator[String] = s => if(s.isEmpty) List("Must not be empty") else Nil

    def LessThan[T](v: T)(implicit ord: Ordering[T]): Validator[T] = i => if(ord.lt(i, v)) Nil else List("Must be less than " + v)
    def GreaterThan[T](v: T)(implicit ord: Ordering[T]): Validator[T] = i => if(ord.gt(i, v)) Nil else List("Must be greater than " + v)

    def LessThanEqual[T](v: T)(implicit ord: Ordering[T]): Validator[T] = i => if(ord.lteq(i, v)) Nil else List("Must be less than or equal " + v)
    def GreaterThanEqual[T](v: T)(implicit ord: Ordering[T]): Validator[T] = i => if(ord.gteq(i, v)) Nil else List("Must be greater than or equal " + v)

    case class AndValidator[T](a: Validator[T], b: Validator[T]) extends Validator[T] {
        def apply(value: T) = a(value) match {
            case Nil => b(value)
            case list => list
        }
    }

    case class OrValidator[T](a: Validator[T], b: Validator[T]) extends Validator[T] {
        def apply(value: T) = a(value) match {
            case Nil => Nil
            case list => b(value)
        }
    }

    case class XorValidator[T](a: Validator[T], b: Validator[T]) extends Validator[T] {
        def apply(value: T) = (a(value), b(value)) match {
            case (Nil, Nil) => List("w00t")
            case (Nil, ls1) => Nil
            case (ls1, Nil) => Nil
            case (ls1, ls2) => ls1 ++ ls2
        }
    }
    class JoinValidator[T](a: Validator[T]){
        def &(b: Validator[T]): Validator[T] = Validators.AndValidator(a,b)
        def |(b: Validator[T]): Validator[T] = Validators.OrValidator(a,b)
        def ^(b: Validator[T]): Validator[T] = Validators.XorValidator(a,b)
    }

}

object Validators extends Validators

