(ns jumski.clojure-sandbox.lambda-calculus.booleans
  (:require [jumski.clojure-sandbox.lambda-calculus.macro :refer [L]]
            [clojure.test :refer [is testing]]))

;;; CHURCH BOOLEANS
;;; https://dzone.com/articles/lambda-calculus-in-clojure-part-1

;; T(rue) and F(alse)
;; Booleans are defined in terms of a choice
;; True will return the first choice and False the other

; true => La.Lb.a
(def T
  (L a. b. a))

; false => La.Lb.b
(def F
  (L a. b. b))

(testing "T and F"
  (is (= :first (T :first :second)))
  (is (= :second (F :first :second))))

(def ->bool
  "Helper function that evaluates boolean value passing it clojure booleans."
  (L f. (f true false)))
(testing "->bool"
  (is (->bool T))
  (is (not (->bool F))))

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
