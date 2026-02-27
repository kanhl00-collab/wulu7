sealed trait ULTerm
case class ULVar(index: Int) extends ULTerm {
  override def toString() = index.toString()
}
case class ULAbs(t: ULTerm) extends ULTerm {
  override def toString() = "lambda . " + t.toString()
}
case class ULApp(t1: ULTerm, t2: ULTerm) extends ULTerm {
  override def toString() = "(" + t1.toString() + ") (" + t2.toString() + ")"
}

// Shift the numbering of unbound variables
def shift(shiftAmount: Int, t: ULTerm): ULTerm = {
  // Walk through the term and shift all variables with index
  // greater than or equal to c, which is maintained to be
  // the number of variable binders (abstractions) outside the current subterm.
  def walk(currentBinders: Int, t: ULTerm): ULTerm = t match {
    // Check if x is a free variable; that is,
    // if the number x is greater than or equal to
    // the number of variable binders encountered outside this subterm.
    case ULVar(x) if (x >= currentBinders) => ULVar(x+shiftAmount)
    case ULVar(x) => ULVar(x)

    case ULAbs(t) =>
      // We now have one more variable binder outside the subterm.
      // Increment currentBinders and walk into the subterm.
      ULAbs(walk(currentBinders+1, t))

    case ULApp(t1,t2) =>
      // No new variable binders. Just walk into the subterms.
      ULApp(walk(currentBinders,t1),walk(currentBinders,t2))
  }

  // Walk the term and perform the shift of free variables.
  // We begin with 0 variable binders outside.
  walk(0, t)
}


// In our usual syntax, we would write substitution as `t[x := r]`.
// Here we write `substitute(t,x,r)`.
def substitute(t: ULTerm, x: Int, r: ULTerm): ULTerm = {
  // We want to substitute for the free variable with number x.
  // Inside a variable binder (abstraction),
  // the index of all free variables is shifted up by 1.
  // So we must keep track of the number of binders outside the current subterm.
  def walk(currentBinders: Int, t: ULTerm): ULTerm = t match {
    case ULVar(y) if y == x + currentBinders =>
      // y is the xth free variable. Substitute for it,
      // making sure to shift the free variables in r
      // to account for the number of variable binders outside this subterm.
      shift(currentBinders,r)

    case ULVar(y) =>
      // Otherwise, y is not the xth free variable;
      // leave it as is.
      ULVar(y)

    case ULAbs(t) =>
      // We now have one more variable binder outside the subterm.
      // Increment currentBinders and walk into the subterm.
      ULAbs(walk(currentBinders+1,t))

    case ULApp(t1,t2) =>
      // No new variable binders. Just walk into the subterms.
      ULApp(walk(currentBinders,t1),walk(currentBinders,t2))
  }

  // Walk the term, performing the substitution.
  // We begin with 0 variable binders outside.
  walk(0,t)
}

// We need to know if a term is a value during reduction
// when using call-by-value semantics.
def isValue(t: ULTerm): Boolean = t match {
  case ULAbs(_) => true
  case _ => false
}

// Call-by-value reduction function.
// Performs one step of evaluation, if possible according to the call-by-value rules.
// If no reduction is possible, returns None.
def reduce(t: ULTerm): Option[ULTerm] = t match {

  // Case: the left term is an abstraction, and the right is a value.
  // Then apply the value to the abstraction.
  case ULApp(ULAbs(t),v) if isValue(v) =>
    // When we apply the value to the abstraction,
    // we must shift the value's free variables up by 1 to account
    // for the abstraction's variable binder.
    val r = substitute(t,0,shift(1,v))
    // Then, we need to shift the result back.
    // Since the abstraction's variable is now "used up".
    Some(shift(-1,r))

  // Case: the left term is a value, then try to reduce the right term.
  case ULApp(v,t) if isValue(v) =>
    reduce(t) match {
      case Some(r) => Some(ULApp(v,r))
      case None => None
    }

  // Case: the left term is not a value (not an abstraction.)
  // Try to reduce it.
  case ULApp(t1,t2) =>
    reduce(t1) match {
      case Some(r1) => Some(ULApp(r1,t2))
      case None => None
    }

  case _ => None
}

// Evaluation just repeatedly applies reduce,
// until we reach None (signifying reduction failed.)
def evaluate(t: ULTerm): ULTerm = reduce(t) match {
  case None => t
  case Some(r) => evaluate(r)
}








sealed trait STType
case object STNat extends STType {
  override def toString() = "nat"
}
case object STBool extends STType {
  override def toString() = "bool"
}
// Functions have a domain type and a codomain type.
case class STFun(dom: STType, codom: STType) extends STType {
  override def toString() = "(" + dom.toString + ") -> (" + codom.toString + ")"
}


sealed trait STTerm
case class STVar(index: Int) extends STTerm
case class STAbs(t1: STType, t2:STTerm) extends STTerm
case class STApp(t1: STTerm, t2: STTerm) extends STTerm
case object STZero extends STTerm
case class STSuc(s: STTerm) extends STTerm
case class STIsZero(t: STTerm) extends STTerm
case object STTrue extends STTerm
case object STFalse extends STTerm
case class STTest(t1: STTerm, t2: STTerm, t3:STTerm) extends STTerm


// Example use: the type "nat -> bool" is written STFun(STNat,STBool)



def typeOf(env: Map[Int, STType], st: STTerm) : Option[STType] = st match {
  case STTrue => Some(STBool)
  case STFalse => Some(STBool)
  case STZero => Some(STNat)
  case STSuc(t) => typeOf(env, t) match {
    case Some(STNat) => Some(STNat)
    case _ => None
  }
  case STIsZero(t) => typeOf(env, t) match {
    case Some(STNat) => Some(STBool)
    case _ => None
  }
  case STTest(t1,t2,t3) => (typeOf(env, t1), typeOf(env, t2), typeOf(env, t3)) match {
    case (Some(STBool),Some(STBool),Some(STBool)) => Some(STBool)
    case (Some(STBool),Some(STNat),Some(STNat)) => Some(STNat)
    case _ => None
  }
  case STVar(index) =>  env.get(index)
  case STAbs(t1,t2) =>
    val t3 = typeOf(env + (env.size -> t1),t2)
    t3 match {
    case Some(t3) => Some(STFun(t1,t3))
    case _ => None
  }
  case STApp(t1,t2) =>
    val t3 = typeOf(env, t1)
    val t4 = typeOf(env, t2)
    (t3,t4) match {
      case (Some(STFun(STNat,b)),Some(STNat)) => Some(b)
      case (Some(STFun(STBool,b)),Some(STBool)) => Some(b)
      case _ => None
    }

}

def typecheck(st: STTerm) : Boolean = typeOf(Map(),st) match {
  case None => false
  case Some(t) => true

}

def eraseTypes(st: STTerm) : ULTerm = st match {
  case STVar(index) => ULVar(index)
  case STApp(t1,t2) => ULApp(eraseTypes(t1), eraseTypes(t2))
  case STAbs(t1,t2) => ULAbs(eraseTypes(t2))
  case STZero => ULAbs(ULAbs(ULVar(0)))
  case STSuc(s) => ULApp( ULAbs(ULAbs(ULAbs(ULApp(ULVar(1),ULApp(ULApp(ULVar(2),ULVar(1)),ULVar(0)))))),eraseTypes(s))
  case STIsZero(t) => ULApp( ULAbs(ULApp(ULApp(ULVar(0), ULAbs(eraseTypes(STFalse))),eraseTypes(STTrue))),eraseTypes(t))
  case STTrue => ULAbs(ULAbs(ULVar(1)))
  case STFalse  => ULAbs(ULAbs(ULVar(0)))
  case STTest(t1,t2,t3) => ULApp(ULApp(ULApp(ULAbs(ULAbs(ULAbs(ULApp(ULApp(ULVar(2),ULVar(1)),ULVar(0))))),eraseTypes(t1)),eraseTypes(t2)),eraseTypes(t3))
}

def evaluate(st: STTerm) : STTerm = st match {
  case STIsZero(t) => evaluate(t) match {
    case STZero => STTrue
    case _ => STFalse
  }
  case STTest(t1,t2,t3) => evaluate(t1) match {
    case STTrue => t2
    case STFalse => t3
  }
}

























