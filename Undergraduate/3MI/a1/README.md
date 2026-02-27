# COMPSCI 3MI3 Assignment1

## Interpreter written in Scala

We define the type `Expr` as follows

```sh
sealed trait Expr  
case class Const(value: Int) extends Expr  
case class Neg(expr1 : Expr) extends Expr  
case class Abs(expr2: Expr) extends Expr  
case class Plus(e1: Expr, e2: Expr) extends Expr  
case class Minus(e3: Expr, e4:Expr) extends Expr  
case class Times(e5: Expr, e6:Expr) extends Expr  
case class Exp(e7: Expr, e8:Expr) extends Expr
```
Then define `interpretExpr` which is a method that calculates the value of Expr. Import `pow` from Scala math library to do exponential.
```sh
def interpretExpr(expr: Expr): Int = expr match {  
  case Const(a) => a  
  case Neg(a) => -1 * interpretExpr(a)  
  case Abs(a) => if (interpretExpr(a) > 0) interpretExpr(a) else -1 * interpretExpr(a)  
  case Plus(a,b) => interpretExpr(a) + interpretExpr(b)  
  case Minus(a,b) => interpretExpr(a) - interpretExpr(b)  
  case Times(a,b) => interpretExpr(a) * interpretExpr(b)  
  case Exp(a,b) => pow(interpretExpr(a), interpretExpr(b)).intValue()    
}
```

## Interpreter written in Prolog

Define predicate `isExpr` which represents expressions in Prolog.
```sh
isExpr(constE(_)) :- !.
isExpr(absE(A)) :- isExpr(A), !.
isExpr(negE(A)) :- isExpr(A), !.
isExpr(plusE(A,B)) :- isExpr(A), isExpr(B).
isExpr(minusE(A,B)) :- isExpr(A), isExpr(B).
isExpr(timesE(A,B)) :- isExpr(A), isExpr(B).
isExpr(expE(A,B)) :- isExpr(A), isExpr(B).
```
Define the predicate `interpretExpr`. In `interpretExpr(e,X)`, e is the expression and X is the integer value.
```sh
interpretExpr(constE(A),X) :- X is A.
interpretExpr(absE(A),X) :- interpretExpr(A,Y), (Y > 0 -> X is Y; X is -1 * Y).
interpretExpr(negE(A),X) :- interpretExpr(A,Y), X is -1 * Y.
interpretExpr(plusE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y + Z.
interpretExpr(minusE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y - Z.
interpretExpr(timesE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y * Z.
interpretExpr(expE(A,B),X) :- interpretExpr(A,Y), interpretExpr(B,Z), X is Y ** Z.
```


## Variable and Substitution
In scala, define the type `VarExpr` by adding 2 new constructors `Var` and `Subst` to `Expr`.
```sh
case class Var(variable: Symbol) extends VarExpr  
case class Subst(expr1: VarExpr, sym: Symbol, expr2: VarExpr) extends VarExpr
```
To define a interpreter, we first define a method to check if there is the variable to be substitute in the expression.
```sh
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
```
Then define `subst` which carries out the substitution.
```sh
def subst(varExpr: VarExpr, sym: Symbol, expr2: VarExpr) : VarExpr = varExpr match {  
  case Const(a) => Const(a)  
  case Var(a) => expr2def subst(varExpr: VarExpr, sym: Symbol, expr2: VarExpr) : VarExpr = varExpr match {  
  case Const(a) => Const(a)  
  case Var(a) => expr2  
  case Neg(a) => Neg(subst(a, sym, expr2))  
  case Abs(a) => Abs(subst(a,sym,expr2))  
  case Plus(a,b) => if (has(a, sym)) Plus(subst(a,sym,expr2),b) else Plus(a,subst(b,sym,expr2))  
  case Minus(a,b) => if (has(a, sym)) Minus(subst(a,sym,expr2),b) else Minus(a,subst(b,sym,expr2))  
  case Times(a,b) => if (has(a, sym)) Times(subst(a,sym,expr2),b) else Times(a,subst(b,sym,expr2))  
  case Exp(a,b) => if (has(a, sym)) Exp(subst(a,sym,expr2),b) else Exp(a,subst(b,sym,expr2))  
  case Subst(e1, s1, e3) => subst(subst(e1,s1,e3),sym, expr2)  
} 
```
Use the above two methods to build the interpreter `interpretVarExpr` which is built on `interpretExpr`.
```sh
def interpretVarExpr(varExpr: VarExpr) : Int = varExpr match {  
  case Subst(expr1, sym, expr2) => if (has(expr1,sym)) interpretVarExpr(subst(expr1,sym,expr2)) else interpretVarExpr(expr1)  
  case Const(a) => a  
  case Neg(a) => -1 * interpretVarExpr(a)  
  case Abs(a) => if (interpretVarExpr(a) > 0) interpretVarExpr(a) else -1 * interpretVarExpr(a)  
  case Plus(a,b) => interpretVarExpr(a) + interpretVarExpr(b)  
  case Minus(a,b) => interpretVarExpr(a) - interpretVarExpr(b)  
  case Times(a,b) => interpretVarExpr(a) * interpretVarExpr(b)  
  case Exp(a,b) => pow(interpretVarExpr(a),interpretVarExpr(b)).intValue()  
}
```

In prolog, the coding logic is similar.

`isVarExpr` represents the variable expression.
```sh
isVarExpr(constE(_)) :- !.
isVarExpr(absE(A)) :- isVarExpr(A), !.
isVarExpr(negE(A)) :- isVarExpr(A), !.
isVarExpr(plusE(A,B)) :- isVarExpr(A), isVarExpr(B), !.
isVarExpr(minusE(A,B)) :- isVarExpr(A), isVarExpr(B), !.
isVarExpr(timesE(A,B)) :- isVarExpr(A), isVarExpr(B), !.
isVarExpr(expE(A,B)) :- isVarExpr(A), isVarExpr(B).
isVarExpr(subst(A, B, C)) :- isVarExpr(A), atom(B), isVarExpr(C).
isVarExpr(var(A)) :- atom(A).
```
The checker:
```sh
has(constE(_),_) :- false.
has(var(A),A).
has(absE(A),B) :- has(A,B).
has(negE(A),B) :- has(A,B).
has(plusE(A,D),B) :- has(A,B); has(D,B).
has(minusE(A,D),B) :- has(A,B); has(D,B).
has(timesE(A,D),B) :- has(A,B); has(D,B).
has(expE(A,D),B) :- has(A,B); has(D,B).
has(subst(A,D,E),B) :- has(A,D), substitute(A,D,E,Y), has(Y,B).
```
The substitution method:
```sh
substitute(constE(A),_,_,constE(A)).
substitute(var(B),B, C, C) :- !.
substitute(var(A),_, _, var(A)) :- !.
substitute(absE(A),B, C, X) :- substitute(A,B,C,Y), interpretExpr(absE(Y),X).
substitute(negE(A),B, C, X) :- substitute(A,B,C,Y), interpretExpr(negE(Y),X).
substitute(plusE(A,D),B, C, plusE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(plusE(A,D),B, C, plusE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(minusE(A,D),B, C, minusE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(minusE(A,D),B, C, minusE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(timesE(A,D),B, C, timesE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(timesE(A,D),B, C, timesE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(expE(A,D),B, C, expE(Y,D)) :- has(A,B), !, substitute(A,B,C,Y).
substitute(expE(A,D),B, C, expE(A,Z)) :- has(D,B), substitute(D,B,C,Z).
substitute(subst(A,D,E),B,C,X) :- substitute(A,B,C,Y), substitute(Y,D,E,X).
```
Finally the interpreter `interpretVarExpr` which calls `interpretExpr`.
```sh
interpretVarExpr(subst(A,B,C),X) :- has(A,B), substitute(A,B, C,Y), interpretExpr(Y,X).
interpretVarExpr(A,X) :- interpretExpr(A,X).
```

## Boolean expressions
As usual, first define the type `MixedExpr` in Scala.
```sh
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
```
Then a method to interpret boolean expressions only.
```sh
def interpretBoolean(expr: MixedExpr): Boolean = expr match {  
  case TT => true  
 case FF => false  
 case Bnot(e) => ! interpretBoolean(e)  
  case Band(e1,e2) => interpretBoolean(e1) && interpretBoolean(e2)  
  case Bor(e1, e2) => interpretBoolean(e1) || interpretBoolean(e2)  
}
```
Group `interpretBoolean` and `interpretExpr` to make `interpretMixedExpr`.
```sh
def interpretMixedExpr(e: MixedExpr): Option[Either[Int,Boolean]] = e match {  
  case TT => Some(Right(true))  
  case FF => Some(Right(false))  
  case Bnot(e0) => Some(Right(interpretBoolean(e0)))  
  case Band(e1, e2) => Some(Right(interpretBoolean(e)))  
  case Bor(e1, e2) => Some(Right(interpretBoolean(e)))  
  case _ => Some(Left(interpretExpr(e)))  
}
```

The coding logic is similar for prolog.

Add the predicate `isBoolExpr`.
```sh
isBoolExpr(tt).
isBoolExpr(ff).
isBoolExpr(band(A,B)) :- isBoolExpr(A), isBoolExpr(B).
isBoolExpr(bnot(A)) :- isBoolExpr(A).
isBoolExpr(bor(A,B)) :- isBoolExpr(A), isBoolExpr(B).
```

`isMixedExpr` is using both `isExpr` and `isBoolExpr`:
```sh
isMixedExpr(A) :- isExpr(A); isBoolExpr(A).
```

Define `interpretBool` to do boolean operations:
```sh
interpretBool(tt).
interpretBool(ff) :- false.
interpretBool(band(A,B)) :- interpretBool(A), interpretBool(B).
interpretBool(bor(A,B)) :- interpretBool(A); interpretBool(B).
interpretBool(bnot(A)) :- \+ interpretBool(A).
```

`interpretBoolExpr` is to decide if an interpretation of boolean expression is true.
```sh
interpretBoolExpr(A,true) :- (interpretBool(A) -> true; false).
interpretBoolExpr(A,false) :- (interpretBool(A) -> false; true).
```

`interpretMixedExpr` is either `interpretExpr` or `interpretBoolExpr`.
```sh
interpretMixedExpr(A, X) :- interpret(A,X); interpretBoolExpr(A,X).
```

