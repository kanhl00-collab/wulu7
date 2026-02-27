(defrecord GuardedCommand [guard command])
(GuardedCommand. '(> x 5) '(- x 1))
;(let [grd-cmd (GuardedCommand. '(> x 5) '(- x 1))]
;  (printf "The guard of this command is %s\n" (:guard grd-cmd))
;  (printf "The command of this command is %s\n" (.command grd-cmd)))

(defn first-allowed-command
  "Find the first command in a sequence of guarded `commands`
whose `.guard` evaluates to a truthy value and return its `.command`.
Returns `nil` if none of the guards are satisfied."
  [commands]
  ;; If the `commands` list is empty, "do nothing" by returning `nil`.
  (if (empty? commands) nil
      ;; Otherwise, deconstruct the `commands` list into
      ;; the first `command` and the `rest`.
      (let [[command & rest] commands]
        ;; Diagnostic print statement, if needed.
        ;(printf "Checking command %s with guard %s and command %s\n" command (.guard command) (.command command))
        ;; Now check the `guard`, and if it's satisfied, return the first `command`.
        (if (eval (.guard command)) (.command command)
            ;; Otherwise, continue to check the `rest` of the guarded commands.
            (first-allowed-command rest)))))


(defmacro guarded-deterministic-if
  "Given a sequence of `GuardedCommands`, `commands`,
select the first guarded command whose `.guard` evaluates
to a truthy value and evaluate its `.command`."
  [& commands]
  ;; The body must be quoted, so that nothing is evaluated until runtime.
  `(eval ;; Evaluate...
    (first-allowed-command ;; ...the command returned by first-allowed-command...
     [~@commands]))) ;; to which we pass a vector of the commands.
;; The ~@ applied to `commands` here "splices" the elements of `commands` into place here.
;; That is, each element of the sequence `commands` is inserted here in order.
;; But not literally as a sequence (between parentheses or brackets.) Hence we wrap in [].
;; The use of the [] is actually quite particular; using a quoted list, '(...), would not work.
;; Because the guarded commands within would be treated as sequences instead of records.



(defn allowed-commands
    [commands]
    (if (empty? commands) nil
    (let [[command & rest] commands]
    ;; apply first-allowed-command on every single element
    (cons (first-allowed-command `(~command)) (allowed-commands rest)))))
    ;(first-allowed-command (last commands)))
  ;  (defn allowed-commands-helper [commands n]
  ;           (if (empty? commands) nil
  ;           (concat (allowed-commands-helper (commands) (+ n 1)) )
  ;(allowed-commands-helper & commands 0))

    ;(if (empty? commands) nil
    ;    (concat ((first-allowed-command (rest commands)))(allowed-commands (drop-last commands)))))



(defmacro guarded-if
  [& commands]
  `(eval (rand-nth;; Evaluate...
    (allowed-commands ;; ...the command returned by first-allowed-command...
     [~@commands]))))

(defmacro guarded-do
  [& commands]
  (doseq [i (allowed-commands [~@commands])] `(eval i)))
 
(defn gcd [x y]
  (guarded-deterministic-if
   (GuardedCommand. `(= ~x 0) y)
   (GuardedCommand. `(= ~y 0) x)
   (GuardedCommand. `(= ~x ~y) x)
   (GuardedCommand. `(> ~x ~y) gcd (- x y) y)
   (GuardedCommand. `(> ~y ~x) gcd x (- y x))))
