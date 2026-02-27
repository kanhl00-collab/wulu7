
def construct_const(value)
    e = Expr.new(:constant, 0, value)
end

def construct_neg(expr)
    e = Expr.new(:unary, :neg, expr)
    #e.ar = :unary
    #e.op = neg
    #e.arg = expr
end

def construct_abs(expr)
    e = Expr.new(:unary, :abs, expr)
    #e.ar = unary
    #e.op = abs
    #e.arg = expr
end

def construct_plus(e1,e2)
    e = Expr.new(:binary,:plus,[e1,e2])
    #e.ar = binary
    #e.op = plus
    #e.arg = [e1,e2]
end

def construct_minus(e1,e2)
    e = Expr.new(:binary, :minus, [e1,e2])
    #e.ar = binary
    #e.op = minus
    #e.arg = [e1,e2]
end

def construct_times(e1,e2)
    e = Expr.new(:binary, :times, [e1,e2])
    #e.ar = binary
    #e.op = times
    #e.arg = [e1,e2]
end

def construct_exp(e1,e2)
    e = Expr.new(:binary, :exp, [e1,e2])
    #e.ar = :binary
    #e.op = :exp
    #e.arg = [e1,e2]
end

class Expr
    attr_accessor :ar, :op, :arg
    arity = [:constant, :unary, :binary]
    def initialize(ar=nil,op=nil,arg=nil)
        @ar = ar
        @op = op
        @arg = arg
    end

    def interpret
        case @ar
            when :constant
                return @arg
            when :unary
                r = @arg.interpret
                case @op
                    when :neg
                        return -r
                    when :abs
                        return abs(r)
                end
            when :binary
                r1 = @arg[0].interpret
                r2 = @arg[1].interpret
                case @op
                    when :plus
                        return r1+r2
                    when :times
                        return r1*r2
                    when :minus
                        return r1 - r2
                    when :exp
                        return r1**r2
                end
        end
    end
end
