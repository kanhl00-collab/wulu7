(load-file "./unwindrec.clj")

(unwindrec exponent [m n]
            (= n 0) 1
            (> n 0) (* m (exponent m (- n 1)))
            (throw (Exception. "No negative exponents.")))
            

(defn exponent-tr [m n]
  (unwindrec exponent-tr-helper [m n collect]
             (= n 0) collect
             (> n 0) (exponent-tr-helper m (- n 1) (* collect m))
             (throw (Exception. "No negative exponents.")))
  (exponent-tr-helper m n 1))


(unwindrec sumlist [xs]
            (empty? xs) 0
            :else (+ (first xs) (sumlist (rest xs)))
            (throw (Exception. "Error.")))
            
;; plus until empty
(defn sumlist-tr [xs]
  (unwindrec sumlist-tr-helper [xs collect]
             (empty? xs) collect
             :else (sumlist-tr-helper (rest xs) (+ collect (first xs)))
             (throw (Exception. "Error.")))
  (sumlist-tr-helper xs 0))



(unwindrec flattenlist [xs]
            (empty? xs) ()
            :else (concat (first xs) (flattenlist (rest xs)))
            (throw (Exception. "Error.")))


(defn flattenlist-tr [xs]
  (unwindrec flattenlist-tr-helper [xs collect]
             (empty? xs) collect
             :else (flattenlist-tr-helper (rest xs) (concat collect (first xs)))
             (throw (Exception. "Error.")))
  (flattenlist-tr-helper xs ()))
  

(unwindrec postfixes [xs]
            (empty? xs) '(())
            :else (concat `(~xs) (postfixes (rest xs)))
            (throw (Exception. "Error.")))


(defn postfixes-tr [xs]
  (unwindrec postfixes-tr-helper [xs collect]
             ;; add empty list to result
             (empty? xs) (concat collect '(()))
             ;; add itself
             :else (postfixes-tr-helper (rest xs) (concat collect `(~xs)))
             (throw (Exception. "Error.")))
  (postfixes-tr-helper xs ()))
