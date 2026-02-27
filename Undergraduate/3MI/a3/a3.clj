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

(defrecord Config [stmt sig])

(defn emptyState [] 0)

(defn updateState [sigma x n]
    (fn [x] n) )

;((updateState emptyState :x 1) :x)

(defn evaluate [const] (.integer const))

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


;((.sig (reduce (Config. (GCAssign. :x (GCConst. 1)) emptyState))) :x)

