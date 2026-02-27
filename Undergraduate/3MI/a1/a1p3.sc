import scala.math.pow

sealed trait VarExpr
case class Var(variable: Symbol) extends VarExpr
case class Subst(expr1: VarExpr, sym: Symbol, expr2: VarExpr) extends VarExpr
case class Const(value: Int) extends VarExpr
case class Neg(expr1 : VarExpr) extends VarExpr
case class Abs(expr2: VarExpr) extends VarExpr
case class Plus(e1: VarExpr, e2: VarExpr) extends VarExpr
case class Minus(e3: VarExpr, e4: VarExpr) extends VarExpr
case class Times(e5: VarExpr, e6: VarExpr) extends VarExpr
case class Exp(e7: VarExpr, e8: VarExpr) extends VarExpr

def subst(varExpr: VarExpr, sym: Symbol, expr2: VarExpr) : VarExpr = varExpr match {
  case Const(a) => Const(a)
  case Var(a) => expr2//Var(a)
  case Neg(a) => Neg(subst(a, sym, expr2))
  case Abs(a) => Abs(subst(a,sym,expr2))
  case Plus(a,b) => if (has(a, sym)) Plus(subst(a,sym,expr2),b) else Plus(a,subst(b,sym,expr2))
  case Minus(a,b) => if (has(a, sym)) Minus(subst(a,sym,expr2),b) else Minus(a,subst(b,sym,expr2))
  case Times(a,b) => if (has(a, sym)) Times(subst(a,sym,expr2),b) else Times(a,subst(b,sym,expr2))
  case Exp(a,b) => if (has(a, sym)) Exp(subst(a,sym,expr2),b) else Exp(a,subst(b,sym,expr2))
  case Subst(e1, s1, e3) => subst(subst(e1,s1,e3),sym, expr2)
}

def has(varExpr: VarExpr, sym: Symbol) : Boolean = varExpr match {
  case Const(a) => false
  case Var(a) => if (a==sym) true else false
  case Neg(a) => has(a, sym)
  case Abs(a) => has(a, sym)
  case Plus(a,b) => has(a, sym) || has(b, sym)
  case Minus(a,b) => has(a, sym) || has(b, sym)
  case Times(a,b) => has(a, sym) || has(b, sym)
  case Exp(a,b) => has(a, sym) || has(b, sym)
  case Subst(e1,s1,e3) => has(e1, s1) && has(subst(e1,s1,e3), sym)
}

def interpretVarExpr(varExpr: VarExpr) : Int = varExpr match {
  case Subst(expr1, sym, expr2) => if (has(expr1,sym)) interpretVarExpr(subst(expr1,sym,expr2)) else interpretVarExpr(expr1)
  //case Var(variable) => 0
  case Const(a) => a
  case Neg(a) => -1 * interpretVarExpr(a)
  case Abs(a) => if (interpretVarExpr(a) > 0) interpretVarExpr(a) else -1 * interpretVarExpr(a)
  case Plus(a,b) => interpretVarExpr(a) + interpretVarExpr(b)
  case Minus(a,b) => interpretVarExpr(a) - interpretVarExpr(b)
  case Times(a,b) => interpretVarExpr(a) * interpretVarExpr(b)
  case Exp(a,b) => pow(interpretVarExpr(a),interpretVarExpr(b)).intValue()
}
