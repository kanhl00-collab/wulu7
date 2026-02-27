module GCL
    
    class GCExpr end
    class GCTest end
    class GCStmt end
    
    class GCConst < GCExpr
        attr_reader :const
        def initialize(integer)
            @const = integer
        end
        
        def evaluate(command, result, memory)
            @const
        end
        
    end
    
    class GCVar < GCExpr
        attr_reader :variable
        def initialize(symbol)
            @variable = symbol
        end
        
        def evaluate(command, result, memory)
            if result.length > 0
                result[0]
            else
                0
            end
        end
        
    end
    
    class GCOp < GCExpr
        attr_reader :exp1, :exp2, :op
        def initialize(exp1, exp2, op)
            @exp1 = exp1
            @exp2 = exp2
            @op = op
        end
        
        def evaluate(command, result, memory)
            case @op
                when :plus
                    return @exp1.evaluate(command, result, memory) + @exp2.evaluate(command, result, memory)
                when :minus
                    return @exp1.evaluate(command, result, memory) - @exp2.evaluate(command, result, memory)
                when :times
                    return @exp1.evaluate(command, result, memory) * @exp2.evaluate(command, result, memory)
                when :div
                    return @exp1.evaluate(command, result, memory) / @exp2.evaluate(command, result, memory)
            end
        end
    end
    
    class GCComp < GCTest
        attr_reader :exp1, :exp2, :comp
        def initialize(exp1,exp2,comp)
            @exp1 = exp1
            @exp2 = exp2
            @comp = comp
        end
        
        def evaluate(command, result, memory)
            case @comp
                when :less
                    return @exp1.evaluate(command, result, memory) < @exp2.evaluate(command, result, memory)
                when :eq
                    return @exp1.evaluate(command, result, memory) == @exp2.evaluate(command, result, memory)
                when :greater
                    return @exp1.evaluate(command, result, memory) > @exp2.evaluate(command, result, memory)
            end
        end
    end
    
    class GCAnd < GCTest
        attr_reader :test1, :test2
        def initialize(test1,test2)
            @test1 = test1
            @test2 = test2
        end
        
        def evaluate(command, result, memory)
            return @test1.evaluate(command, result, memory) && @test2.evaluate(command, result, memory)
        end
    end
    
    class GCOr < GCTest
        attr_reader :test1, :test2
        def initialize(test1,test2)
            @test1 = test1
            @test2 = test2
        end
        
        def evaluate(command, result, memory)
            return @test1.evaluate(command, result, memory) || @test2.evaluate(command, result, memory)
        end
        
    end
    
    class GCTrue < GCTest
        def evaluate(command, result, memory)
            true
        end
    end
    
    class GCFalse < GCTest
        def evaluate(command, result, memory)
            false
        end
    end
    
    class GCSkip < GCStmt
        
    end
    
    class GCAssign < GCStmt
        attr_reader :var, :exp
        def initialize(var,exp)
            @var = var
            @exp = exp
        end
        
        def eval(command, result, memory)
            command[0] = @var
            result[0] = @exp.evaluate(command, result, memory)
            new = updateState(memory, @var, result[0])
        end
    end
    
    class GCCompose < GCStmt
        attr_reader :sta1, :sta2
        def initialize(sta1,sta2)
            @sta1 = sta1
            @sta2 = sta2
        end
        
        def eval(command, result, memory)
            @sta1.eval(command, result, memory)
            @sta2.eval(command, result, memory)
            new = updateState(memory, @var, result[0])
        end
    end
    
    class GCIf < GCStmt
        attr_reader :list
        def initialize(list)
            @list = list
        end
        
        def eval(command,result,memory)
            pickedList = @list.sample
            if pickedList[0].evaluate(command, result, memory)
                pickedList[1].eval(command, result, memory)
            end
        end
    end
    
    class GCDo < GCStmt
        attr_reader :list
        def initialize(list)
            @list = list
        end
        
        def eval(command, result, memory)
            pickedList = @list.sample
            if pickedList[0].evaluate(command, result,memory)
                pickedList[1].eval(command, result, memory)
                self.eval(command, result, memory)
            end
        end
        
    end
    
    
    def emptyState
        lambda{0}
    end
    
    def updateState(sigma,x,n)
        sigma = lambda{|x| n}
    end
    
    def stackEval(command, result, memory)
        command[0].eval(command, result, memory)
        #updateState(memory, resultList[0], resultList[1])
    end

end


module GCLe
    
    class GCExpr end
    class GCTest end
    class GCStmt end
    
    class GCConst < GCExpr
        attr_reader :const
        def initialize(integer)
            @const = integer
        end
        
        def evaluate(temporary)
            @const
        end
        
    end
    
    class GCVar < GCExpr
        attr_reader :variable
        def initialize(symbol)
            @variable = symbol
        end
        
        def evaluate(temporary)
            temporary[@variable]
        end
    end
    
    class GCOp < GCExpr
        attr_reader :exp1, :exp2, :op
        def initialize(exp1, exp2, op)
            @exp1 = exp1
            @exp2 = exp2
            @op = op
        end
        
        def evaluate(temporary)
            case @op
                when :plus
                    return @exp1.evaluate(temporary) + @exp2.evaluate(temporary)
                when :minus
                    return @exp1.evaluate(temporary) - @exp2.evaluate(temporary)
                when :times
                    return @exp1.evaluate(temporary) * @exp2.evaluate(temporary)
                when :div
                    return @exp1.evaluate(temporary) / @exp2.evaluate(temporary)
            end
        end
        
    end
    
    class GCComp < GCTest
        attr_reader :exp1, :exp2, :comp
        def initialize(exp1,exp2,comp)
            @exp1 = exp1
            @exp2 = exp2
            @comp = comp
        end
        
        def evaluate(temporary)
            case @comp
                when :less
                    return @exp1.evaluate(temporary) < @exp2.evaluate(temporary)
                when :eq
                    return @exp1.evaluate(temporary) == @exp2.evaluate(temporary)
                when :greater
                    return @exp1.evaluate(temporary) > @exp2.evaluate(temporary)
            end
        end
        
    end
    
    class GCAnd < GCTest
        attr_reader :test1, :test2
        def initialize(test1,test2)
            @test1 = test1
            @test2 = test2
        end
        
        def evaluate(temporary)
            @test1.evaluate(temporary) && @test2.evaluate(temporary)
        end
        
    end
    
    class GCOr < GCTest
        attr_reader :test1, :test2
        def initialize(test1,test2)
            @test1 = test1
            @test2 = test2
        end
        
        def evaluate(temporary)
            @test1.evaluate(temporary) || @test2.evaluate(temporary)
        end
        
    end
    
    class GCTrue < GCTest
        def evaluate(temporary)
            true
        end
    end
    
    class GCFalse < GCTest
        def evaluate(temporary)
            false
        end
    end
    
    class GCSkip < GCStmt
        def check(list) end
    end
    
    class GCAssign < GCStmt
        attr_reader :var, :exp
        def initialize(var,exp)
            @var = var
            @exp = exp
        end
        
        def check(list)
            if @exp.is_a?(GCVar)
                return list.include?(@exp.variable)
            end
        end
        
        def evaluate(temporary)
            temporary[@var] = @exp.evaluate(temporary)
        end
        
    end
    
    class GCCompose < GCStmt
        attr_reader :sta1, :sta2
        def initialize(sta1,sta2)
            @sta1 = sta1
            @sta2 = sta2
        end
        
        def check(list) end
        
        def evaluate(temporary)
            @sta1.evaluate(temporary)
            @sta2.evaluate(temporary)
        end
        
    end
    
    class GCIf < GCStmt
        attr_reader :list
        def initialize(list)
            @list = list
        end
        
        def check(list) end
        
        def evaluate(temporary)
            pickedList = @list.sample
            if pickedList[0].evaluate(temporary)
                pickedList[1].evaluate(temporary)
            end
        end
            
        
    end
    
    class GCDo < GCStmt
        attr_reader :list
        def initialize(list)
            @list = list
        end
        
        def check(list) end
        
        def evaluate(temporary)
            pickedList = @list.sample
            if pickedList[0].evaluate(temporary)
                pickedList[1].evaluate(temporary)
                self.evaluate(temporary)
            end
        end
            
        
    end
    
    class GCProgram
        attr_reader :list, :sta
        def initialize(list,sta)
            @list = list
            @sta = sta
        end
    end
    
    class GCLocal < GCStmt
        attr_reader :var, :sta
        def initialize(var,sta)
            @var = var
            @sta = sta
        end
        
        def check(list)
            if @sta.is_a?(GCAssign)
                return @sta.var == @var
            end
        end
        
        def evaluate(temporary)
            @sta.evaluate(temporary)
        end
    end
    
    def wellScoped(program)
        program.sta.check(program.list)
        
    end
    
    update_state = Hash.new
    temporary = Hash.new
    
    def eval(program)
        update_state = Hash.new
        temporary = Hash.new
        for i in 0..program.list.size
            update_state[program.list[i]] = 0
        end
        program.sta.evaluate(temporary)
        temporary.each do |key, value|
            update_state[key] = value
        end
    end
end
