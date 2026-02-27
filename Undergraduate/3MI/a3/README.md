# COMPSCI 3MI3 Assignment3

## Representing GCL and GCLe in Ruby
We first make a module for GCL
```
module GCL
end
```
Then define types `GCExpr`, `GCTest`, and `GCStmt` inside this module.
```
class GCExpr end

class GCTest end

class GCStmt end
```
`GCConst`,`GCOp`, and `GCVar` are subclasses of `GCExpr`.
`GCConst` takes an integer as argument.
`GCVar` takes a symbol as variable name.
`GCOp` takes two `GCExpr` and one of `:plus`, `minus`,`times`,and `div`.

```
class GCConst < GCExpr
    attr_reader :const
    def initialize(integer)
        @const = integer
    end
end
```
Above is an example.
`GCComp`,`GCAnd`,`GCOr`,`GCTrue`,and `GCFalse` are subclasses of `GCTest`.
`GCTrue` and `GCFalse` takes no arguments.
```
class GCTrue < GCTest end
```
`GCSkip`,`GCAssign`,`GCCompose`, `GCIf` and `GCDo`  are subclasses of `GCStmt`.


For GCLe, we create separate module.
`module GCLe end`
GCLe is just adding `GCProgram` and `GCLocal`.
`GCProgram` is another type that takes a list of symbols for the global variable names and a `GCStmt`. `GCLocal` is another subclass of `GCStmt` that takes a symbol for the variable name and a `GCStmt`.

```
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
end
```

## Representing GCL  in Clojure

In Clojure, use records to represent subclasses.
```
(defrecord GCConst [integer])
(defrecord GCVar [variable])
(defrecord GCOp [exp1 exp2 op])

(defrecord GCComp [exp1 exp2 comp])
(defrecord GCAnd [test1 test2])
(defrecord GCOr [test1 test2])
(defrecord GCTrue [])
(defrecord GCFalse [])

(defrecord GCSkip [])
(defrecord GCAssign [var exp])
(defrecord GCCompose [sta1 sta2])
(defrecord GCIf [lists])
(defrecord GCDo [lists])
```

## Stack machine for GCL in Ruby
The method `emptyState` is a lambda that takes no argument and returns 0.
```
def emptyState
    lambda{0}
end
```

Then `updateState(sigma,x,n)` returns a lambda which maps `x` to `n`.
```
def updateState(sigma,x,n)
    sigma = lambda{|x| n}
end
```

`stackEval` is evaluate each command and then updateState.

For `GCSkip` do nothing.
For `GCAssign`, put variable on command stack and evaluated expression on result stack.
```
def eval(command, result, memory)
    command[0] = @var
    result[0] = @exp.evaluate(command, result, memory)
    new = updateState(memory, @var, result[0])
end
```
For `GCCompose`, evaluate first statement then second statement.
```
def eval(command, result, memory)
    @sta1.eval(command, result, memory)
    @sta2.eval(command, result, memory)
    new = updateState(memory, @var, result[0])
end
```
For `GCIf`, randomly pick one in the list of guarded commands, if the guard evaluate to true then carry out the command, if false do nothing.
```
def eval(command,result,memory)
    pickedList = @list.sample
    if pickedList[0].evaluate(command, result, memory)
        pickedList[1].eval(command, result, memory)
    end
end
```
`GCDo` is repeatedly pick one guarded command in list until the guard of the command evaluate to false.
```
def eval(command, result, memory)
    pickedList = @list.sample
    if pickedList[0].evaluate(command, result,memory)
        pickedList[1].eval(command, result, memory)
        self.eval(command, result, memory)
    end
end
```


## Small-step semantics for GCL in Clojure

The input and output for `reduce` is `Config`.
```
(defrecord Config [stmt sig])
```
`emptyState` takes no argument and returns 0.
```
(defn emptyState [] 0)
```
`updateState` takes a lambda,` x`, `n` and updates this lambda to map `x` to `n`.
```
(defn updateState [sigma x n]

    (fn [x] n) )
```

`((updateState emptyState :x 1) :x)` should return 1.

For `GCAssign`, `reduce` is assigning the variable. The new statement should be `GCSkip`. `x` in `updateState`  is the variable and `n` is the expression.
For `GCIf`, pick one guarded command. If the guard is `GCTrue`, the new statement is the command, otherwise remain the same.
For `GCCompose`, reduce the first `GCStmt`.
For `GCDo`, pick one guarded command. If the guard is `GCTrue`, the new statement is the command, otherwise remain the same.
For `GCSkip`, do nothing.
```
(defn reduce [config]
(cond
(instance? GCAssign (.stmt config)) (Config. (GCSkip.) (updateState (.sig config) (.var (.stmt config)) (evaluate (.exp (.stmt config)))))
(instance? GCIf (.stmt config))  (let
[picked (rand-nth (.lists (.stmt config)))]
(if
(= (GCTrue.) (picked 0))  (Config. (picked 1) (.sig config))
config))
(instance? GCCompose (.stmt config)) (reduce (.sta1 (.stmt config)))
(instance? GCDo (.stmt config)) (let
[picked (rand-nth (.lists (.stmt config)))]
(if
(= (GCTrue.) (picked 0)) (Config. (picked 1) (.sig config))
) config )
:else config))
```

## Big-step semantics for GCLe in Ruby

Check if the value assigned to the variable is global. If it is global, `wellScoped` is true. Check if the variable assigned is local. If it is local, `wellScoped` is true.
```
def check(list)
    if @sta.is_a?(GCAssign)
        return @sta.var == @var
    end
end
```
This checks if the local variable is the variable assigned.
```
def check(list)
    if @exp.is_a?(GCVar)
        return list.include?(@exp.variable)
    end
end
```
This checks if the value assigned is in the list of global variables.
```
def wellScoped(program)
    program.sta.check(program.list)
end
```
This is the overall function.

`update_state` is a `Hash` for  variables.
`temporary` is a `Hash` for temporary values.

```
update_state = Hash.new
temporary = Hash.new
```

Initialize all global variables to 0.
```
for i in 0..program.list.size
    update_state[program.list[i]] = 0
end
```
Evaluate `GCProgram`.
```
program.sta.evaluate(temporary)
```
Update the temporary value to assigned value in `GCAssign`.
```
def evaluate(temporary)
    temporary[@var] = @exp.evaluate(temporary)
end
```
Evaluating `GCVar` is getting the value for the variable from `temporary`.
```
def evaluate(temporary)
    temporary[@variable]
end
```
For each value in `temporary`, put it in `update_state`.
```
temporary.each do |key, value|
    update_state[key] = value
end
```
