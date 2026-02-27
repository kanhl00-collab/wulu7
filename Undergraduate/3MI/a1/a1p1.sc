import scala.math.pow

sealed trait Expr
case class Const(value: Int) extends Expr
case class Neg(expr1 : Expr) extends Expr
case class Abs(expr2: Expr) extends Expr
case class Plus(e1: Expr, e2: Expr) extends Expr
case class Minus(e3: Expr, e4:Expr) extends Expr
case class Times(e5: Expr, e6:Expr) extends Expr
case class Exp(e7: Expr, e8:Expr) extends Expr

def interpretExpr(expr: Expr): Int = expr match {
  case Const(a) => a
  case Neg(a) => -1 * interpretExpr(a)
  case Abs(a) => if (interpretExpr(a) > 0) interpretExpr(a) else -1 * interpretExpr(a)
  case Plus(a,b) => interpretExpr(a) + interpretExpr(b)
  case Minus(a,b) => interpretExpr(a) - interpretExpr(b)
  case Times(a,b) => interpretExpr(a) * interpretExpr(b)
  case Exp(a,b) => pow(interpretExpr(a), interpretExpr(b)).intValue()

}


















