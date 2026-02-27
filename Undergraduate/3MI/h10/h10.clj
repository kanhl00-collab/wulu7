(load-file "./collection.clj")

(defn summingPairs [xs sum]
  (letfn [(summingPairsHelper [xs the_pairs]
            ;; If `xs` is empty, we're done.
            (if (empty? xs) the_pairs
                ;; Otherwise, decompose `xs` into the `fst` element
                ;; and the `rest`.
                (let [[fst & rest] xs]
                  (let [
                    futurefst (future fst)
                    futurerest (future rest)
                  ]
                  ;; We use the `recur` form to make the recursive call.
                  ;; This ensures tail call optimisation
                  (recur
                   (deref futurerest)
                   ;; Concatenate `the_pairs` we have so far with the sequence
                   ;; of every `[fst snd]` where `snd` is in `rest` with
                   ;; `fst + snd <= sum`. The `doall` outside the `concat`
                   ;; forces it to be calculated immediately; without this,
                   ;; we get a (lazy) buildup of `concat`'s which may
                   ;; cause a stack overflow when looking at the result.
                   (doall
                    (concat the_pairs
                            (for [snd (deref futurerest) ;; For each `snd` in `rest`...
                                  :when (<= (+ (deref futurefst) snd) sum)]
                              ;;... put `[fst snd]` into this sequence.
                              [(deref futurefst) snd]))))))))]
    (summingPairsHelper xs [])))
    

