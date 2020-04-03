(ns jumski.clojure-sandbox.lambda-calculus
  (:require [clojure.test :refer [is testing]]))

(defmacro L
  "Builds a anonymous function definition based on passed forms.
  Forms which name ends with a dot will be used as arguments to that function,
  and rest of the forms will be just inlined as function body.

  Examples:
  (L a. b. a)       ;=> (fn [a b] a)
  (L p. q. (p q q)) ;=> (fn [p q] (p q q))

  Adapted from https://dzone.com/articles/lambda-calculus-in-clojure-part-1"
  [& forms]
  (letfn [(is-arg? [form] (and (symbol? form)
                               (= \. (last (name form)))))
          (normalize-arg [form] (-> (name form)
                                    butlast
                                    clojure.string/join
                                    symbol))]
    (let [[args body] (partition-by is-arg? forms)
          args (map normalize-arg args)]
      `(fn [~@args] ~@body))))

;;; CHURCH BOOLEANS
;;; https://dzone.com/articles/lambda-calculus-in-clojure-part-1

;; T(rue) and F(alse)
;; Booleans are defined in terms of a choice
;; True will return the first choice and False the other

; true => La.Lb.a
(def T
  (L a. b. a))
(is (= :first (T :first :second)))

; false => La.Lb.b
(def F
  (L a. b. b))
(is (= :second (F :first :second)))


(def ->bool
  "Helper function that evaluates boolean value passing it clojure booleans."
  (L f. (f true false)))
(is (->bool T))
(is (not (->bool F)))

;;; LOGIC
;;; Logic is defined as composition of T and F choices

; And => Lp.Lq. p q p
(def And
  (L p. q. (p q p)))
(testing "And"
  (is (= T (And T T)))
  (is (= F (And T F)))
  (is (= F (And F T)))
  (is (= F (And F F))))

; Or => Lp.Lq. p p q
(def Or
  (L p. q. (p p q)))
(testing "Or"
  (is (= F (Or F F)))
  (is (= T (Or F T)))
  (is (= T (Or T F)))
  (is (= T (Or T T))))

; Not => Lp. p F T
(def Not
  (L p. (p F T)))
(testing "Not"
  (is (= F (Not T)))
  (is (= T (Not F))))

; Xor
(def Xor
  (L p. q. (p (Not q) q)))
(testing "Xor"
  (is (= F (Xor T T)))
  (is (= T (Xor T F)))
  (is (= T (Xor F T)))
  (is (= F (Xor F F))))

; Lp.a.b. p a b
(def If
  (L p. a. b. (p a b)))
(testing "If"
  (is (= :first (If T :first :second)))
  (is (= :second (If F :first :second))))

;;; CHURCH NUMERALS
