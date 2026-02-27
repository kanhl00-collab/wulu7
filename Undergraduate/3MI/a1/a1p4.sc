import scala.math.pow

sealed trait MixedExpr
case object TT extends MixedExpr
case object FF extends MixedExpr
case class Band(e1: MixedExpr, e2: MixedExpr) extends MixedExpr
case class Bor(e1: MixedExpr, e2: MixedExpr) extends MixedExpr
case class Bnot(e: MixedExpr) extends MixedExpr
case class Const(value: Int) extends MixedExpr
case class Neg(expr1 : MixedExpr) extends MixedExpr
case class Abs(expr2: MixedExpr) extends MixedExpr
case class Plus(e1: MixedExpr, e2: MixedExpr) extends MixedExpr
case class Minus(e3: MixedExpr, e4:MixedExpr) extends MixedExpr
case class Times(e5: MixedExpr, e6:MixedExpr) extends MixedExpr
case class Exp(e7: MixedExpr, e8:MixedExpr) extends MixedExpr

def interpretExpr(expr: MixedExpr): Int = expr match {
  case Const(a) => a
  case Neg(a) => -1 * interpretExpr(a)
  case Abs(a) => if (interpretExpr(a) > 0) interpretExpr(a) else -1 * interpretExpr(a)
  case Plus(a,b) => interpretExpr(a) + interpretExpr(b)
  case Minus(a,b) => interpretExpr(a) - interpretExpr(b)
  case Times(a,b) => interpretExpr(a) * interpretExpr(b)
  case Exp(a,b) => pow(interpretExpr(a), interpretExpr(b)).intValue()
}

def interpretBoolean(expr: MixedExpr): Boolean = expr match {
  case TT => true
  case FF => false
  case Bnot(e) => ! interpretBoolean(e)
  case Band(e1,e2) => interpretBoolean(e1) && interpretBoolean(e2)
  case Bor(e1, e2) => interpretBoolean(e1) || interpretBoolean(e2)
}
def interpretMixedExpr(e: MixedExpr): Option[Either[Int,Boolean]] = e match {
  case TT => Some(Right(true))
  case FF => Some(Right(false))
  case Bnot(e0) => Some(Right(interpretBoolean(e0)))
  case Band(e1, e2) => Some(Right(interpretBoolean(e)))
  case Bor(e1, e2) => Some(Right(interpretBoolean(e)))
  case _ => Some(Left(interpretExpr(e)))
}