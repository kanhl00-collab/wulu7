# Our top-level ULTerm class defines some default
# methods to track what kind of term we have
# (which must be overidden in non-default cases)
# as well as the shift, substitute and eval methods
# which are defined in terms of other methods
# defined by the subclasses.
class ULTerm

  # By default, we assume terms are irreducible,
  # not abstractions, and not values.
  # Subclasses which should have these properties
  # must override these methods.
  # (In our basic calculus with call-by-value semantics,
  # only applications are reducible and only abstractions
  # are values. This can be changed for different calculi/semantics.)
  def reduce; nil end
  def absBody; nil end
  def isValue?; false end
  
  # Shifting is just walking, where in the base case,
    # we either increment the variable by shiftAmount or
    # leave it alone.
    def shift(shiftAmount)
      # walk is an iterator.
      # The block tells us what to do with variables.
      walk(0) { |x,currentBinders|
        if x >= currentBinders
          ULVar.new(x+shiftAmount)
        else
          ULVar.new(x)
        end }
    end

    # Substitution is just walking, where we either
    # replace the variable, or leave it alone.
    def substitute(x,r)
      walk(0) { |y,currentBinders|
        if y == x + currentBinders
          r
        else
          ULVar.new(y)
        end }
    end
    
    def eval
       r = nil
       r_next = self
       # Keep reducing until it fails (reduce returns nil.)
       # This is the recommended "do...while" form in Ruby.
       loop do
         r = r_next
         r_next = r.reduce
         break unless r_next
       end

       return r
    end
end

class ULVar < ULTerm
  attr_reader :index

  # We require our variables are only indexed by integers.
  def initialize(i)
    unless i.is_a?(Integer)
      throw "Constructing a lambda term out of non-lambda terms"
    end
    @index = i
  end
  
  def walk(currentBinders,&block)
    # This is a variable. Run the code in &block.
    # (yield does this; it "yields" control to the block.)
    yield(@index, currentBinders)
  end

  def to_s
    @index.to_s
  end

  def ==(r); r.is_a?(ULVar) && r.index == @index end
end

class ULAbs < ULTerm
  attr_reader :t

  def initialize(t)
    unless t.is_a?(ULTerm)
      throw "Constructing a lambda term out of a non-lambda term"
    end
    @t = t
  end
  
  def walk(currentBinders,&block)
    # Increment the local variable counter within the variable binder.
    t = @t.walk(currentBinders+1,&block)
    ULAbs.new(t)
  end

  # Abstractions are an abstraction (of course),
  # with body @t,
  # and are also considered values.
  def absBody; @t end
  def isValue?; true end
  
  def to_s
    "lambda . " + @t.to_s
  end

  def ==(r); r.is_a?(ULAbs) && r.t == @t end
end

class ULApp < ULTerm
  attr_reader :t1
  attr_reader :t2

  def initialize(t1,t2)
    unless t1.is_a?(ULTerm) && t2.is_a?(ULTerm)
      throw "Constructing a lambda term out of non-lambda terms"
    end
    @t1 = t1; @t2 = t2
  end
  
  def walk(currentBinders,&block)
    t1 = @t1.walk(currentBinders,&block)
    t2 = @t2.walk(currentBinders,&block)
    ULApp.new(t1,t2)
  end

  # Applications can be reduced.
  def reduce
    if @t1.absBody && @t2.isValue?
      body = @t1.absBody
      (body.substitute(0,@t2.shift(1))).shift(-1)
    elsif @t1.isValue?
      r = @t2.reduce
      if r
        ULApp.new(@t1,r)
      else
        nil
      end
    else
      r = @t1.reduce
      if r
        ULApp.new(r,@t2)
      else
        nil
      end
    end
  end

  def to_s
    "(" + @t1.to_s + ") (" + @t2.to_s + ")"
  end

  def ==(r); r.is_a?(ULApp) && r.t1 == @t1 && r.t2 == @t2 end
end

########################################################
class STType end

class STNat < STType
  # Comparison and printing methods
  def ==(type); type.is_a?(STNat) end
  def to_s; "nat" end
end

class STBool < STType
  # Comparison and printing methods
  def ==(type); type.is_a?(STBool) end
  def to_s; "bool" end
end

# Functions have a domain type and a codomain type.
class STFun < STType
  attr_reader :dom
  attr_reader :codom
  
  def initialize(dom, codom)
    unless dom.is_a?(STType) && dom.is_a?(STType)
      throw "Constructing a type out of non-types"
    end
    @dom = dom; @codom = codom
  end

  # Comparison and printing methods
  def ==(type); type.is_a?(STFun) && type.dom == @dom && type.codom == @codom end
  def to_s; "(" + dom.to_s + ") -> (" + codom.to_s + ")" end
end

# Example use: the type "nat -> bool" is written STFun.new(STNat.new,STBool.new)


#####################################################################




class STTerm
    def typeOf; nil end
    def absBody; nil end
    def typecheck
        
        if typeOf(Array([]))
            true
        else
            false
        end
    end
end

class STVar < STTerm
    attr_reader :index

    # We require our variables are only indexed by integers.
    def initialize(i)
      unless i.is_a?(Integer)
        throw "Constructing a lambda term out of non-lambda terms"
      end
      @index = i
    end
    
    def typeOf(env)
        env[@index]
    end
    
    def eraseTypes
        ULVar.new(@index)
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
  
  def typeOf(env)
      env.push(@t1)
      t3 = @t2.typeOf(env)
      if t3
          STFun.new(@t1,t3)
      else
          nil
      end
  end
  
  def eraseTypes
      ULAbs.new(@t2.eraseTypes)
  end
  
  def absBody; @t2 end
  
  def ==(term); term.is_a?(STAbs) && term.t1 == @t1 && term.t2 == @t2 end
end

class STApp < STTerm
  attr_reader :t1
  attr_reader :t2

  def initialize(t1,t2)
    unless t1.is_a?(STTerm) && t2.is_a?(STTerm)
      throw "Constructing a lambda term out of non-lambda terms"
    end
    @t1 = t1; @t2 = t2
  end
  
  def typeOf(env)
      t3 = @t1.typeOf(env)
      t4 = @t2.typeOf(env)
      if t3.is_a?(STFun) && t3.dom.is_a?(t4)
          t3.codom
      else
          nil
      end
  end
  
  def eraseTypes
      ULApp.new(@t1.eraseTypes, @t2.eraseTypes)
  end
  
  def ==(term); term.is_a?(STApp) && term.t1 == @t1 && term.t2 == @t2 end
end

class STZero < STTerm
    def typeOf(env)
        STNat
    end
    
    def eraseTypes
        ULAbs.new(ULAbs.new(ULVar.new(0)))
    end
end

class STSuc < STTerm
  attr_reader :t

  def initialize(t)
    unless t.is_a?(STTerm)
      throw "Constructing a lambda term out of non-lambda terms"
    end
    @t = t
  end
  
  def typeOf(env)
      if @t.typeOf(env) == STNat
          STNat
      else
          nil
      end
  end
  
  def eraseTypes
      ULApp.new(ULAbs.new(ULAbs.new(ULAbs.new(ULApp.new(ULVar.new(1),ULApp.new(ULApp.new(ULVar.new(2),ULVar.new(1)),ULVar.new(0)))))), @t.eraseTypes)
  end
  
  def ==(term); term.is_a?(STSuc) && term.t == @t end
end

class STIsZero < STTerm
  attr_reader :t

  def initialize(t)
    unless t.is_a?(STTerm)
      throw "Constructing a lambda term out of non-lambda terms"
    end
    @t = t
  end
  
  def typeOf(env)
      if @t.typeOf(env) == STNat
          STBool
      else
          nil
      end
  end
  
  def eraseTypes
      ULApp.new(ULAbs.new(ULApp.new(ULApp.new(ULVar.new(0), ULAbs.new(STFalse.new.eraseTypes)),STTrue.new.eraseTypes)),@t.eraseTypes)
  end
      
  def ==(term); term.is_a?(STIsZero) && term.t == @t end
end

class STTrue < STTerm

    def typeOf(env)
        STBool
    end
    
    def eraseTypes
        ULAbs.new(ULAbs.new(ULVar.new(1)))
    end
end

class STFalse < STTerm

    def typeOf(env)
        STBool
    end
    
    def eraseTypes
        ULAbs.new(ULAbs.new(ULVar.new(0)))
    end

end

class STTest < STTerm
  attr_reader :t1
  attr_reader :t2
  attr_reader :t3

  def initialize(t1,t2,t3)
    unless t1.is_a?(STTerm) && t2.is_a?(STTerm) && t3.is_a?(STTerm)
      throw "Constructing a lambda term out of non-lambda terms"
    end
    @t1 = t1; @t2 = t2; @t3 = t3
  end
  
  def typeOf(env)
      t4 = @t1.typeOf(env)
      t5 = @t2.typeOf(env)
      t6 = @t3.typeOf(env)
      if t4 == STBool
          if t5 == t6
              t5
          else
              nil
          end
      else
          nil
      end
  end
  
  def eraseTypes
      ULApp.new(ULApp.new(ULApp.new(ULAbs.new(ULAbs.new(ULAbs.new(ULApp.new(ULApp.new(ULVar.new(2),ULVar.new(1)),ULVar.new(0))))),@t1.eraseTypes),@t2.eraseTypes),@t3.eraseTypes)
  end
  
  def ==(term); term.is_a?(STTest) && term.t1 == @t1 && term.t2 == @t2 && term.t3 == @t3 end

end


