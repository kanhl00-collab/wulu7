# COMPSCI 3MI3 Assignment2

## representation of typed lambda calculus 

The type STTerm should be defined as follows:
The `STZero`, `STTrue` and `STFalse` types are declared as singleton classes in Scala.
The `STAbs` constructor takes a `STType` , which is the type of the variable being abstracted, and a `STTerm`.
```
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
```

In Ruby, use classes and subclasses.
```
class STVar < STTerm
    attr_reader :index
    def initialize(i)
        unless i.is_a?(Integer)
            throw "Constructing a lambda term out of non-lambda terms"
        end
    @index = i
    end
    def ==(term); term.is_a?(STVar) && term.index == @index end
end

class STAbs < STTerm
    attr_reader :t1
    attr_reader :t2
    def initialize(t1,t2)
        unless t1.is_a?(STType) && t2.is_a?(STTerm)
            throw "Constructing a lambda term out of a non-lambda term"
        end
        @t1 = t1
        @t2 = t2
    end
    def ==(term); term.is_a?(STAbs) && term.t1 == @t1 && term.t2 == @t2 end
end
```

Similar for the rest that have parameters.
For those without a parameter, this is an example:
```
class STZero < STTerm end
```

## type checking
Before type checking, we first define a method `typeOf` that takes the environment and the STTerm.  `typeOf` method will return a `Option[STType]` in Scala.
I use map to represent the environment in Scala and list to represent it in Ruby.
For STVar(index), if the environment has this index, return the variable type otherwise return none.
For `STAbs`, store the type into the environment.
For `STApp`, variable type and function type should match.
```
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
```
In Ruby, split the function into classes and subclasses.
```
class STVar < STTerm
    def typeOf(env)
        env[@index]
    end
end

class STAbs < STTerm
  def typeOf(env)
    env.push(@t1)
    t3 = @t2.typeOf(env)
    if t3
        STFun.new(@t1,t3)
    else
        nil
    end
  end
 end

class STApp < STTerm
    def typeOf(env)
        t3 = @t1.typeOf(env)
        t4 = @t2.typeOf(env)
        if t3.is_a?(STFun) && t3.dom.is_a?(t4)
            t3.codom
        else
            nil
        end
    end
end

class STZero < STTerm
    def typeOf(env)
        STNat
    end
end

class STSuc < STTerm
    def typeOf(env)
        if @t.typeOf(env) == STNat
            STNat
        else
            nil
        end
    end
end
```

Then the `typecheck` is just applying `typeOf`:
```
def typecheck(st: STTerm) : Boolean = typeOf(Map(),st) match {  
  case None => false  
 case Some(t) => true  
}
```

```
class STTerm
    def typecheck
        if typeOf(Array([]))
            true
        else
            false
        end
    end
end
```

## translation to untyped lambda calculus
Translating from `STTerm` to `ULTerm` is just using the `ULTerm`  syntax to represent based on each rule given.
```
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
```

`STVar` to `ULVar`:
```
def eraseTypes
    ULVar.new(@index)
end
```

`STAbs` to `ULAbs`:
```
def eraseTypes
    ULAbs.new(@t2.eraseTypes)
end
```

`STSuc`:
```
def eraseTypes
    ULApp.new(ULAbs.new(ULAbs.new(ULAbs.new(ULApp.new(ULVar.new(1),ULApp.new(ULApp.new(ULVar.new(2),ULVar.new(1)),ULVar.new(0)))))), @t.eraseTypes)
end
```

`STTrue`:
```
def eraseTypes
    ULAbs.new(ULAbs.new(ULVar.new(1)))
end
```
