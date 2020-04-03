(ns jumski.clojure-sandbox.lambda-calculus.numerals
  (:require [jumski.clojure-sandbox.lambda-calculus.macro :refer [L]]
            [clojure.test :refer [is testing]]))

;;; NUMERALS
;;; Based on https://dzone.com/articles/lambda-calculus-in-clojure-part-2
;;; Adapted to use simpler macro

;; Church numeral of n is the number of times fencapsulates n.
;; For example, for n = 3, the function f encapsulates n three times:

; zero is special case, it just returns parameter
(def zero
  (L f. (L x. x)))

; others are n-times applications of f to x
; (def one
;   (L f. x. (f x)))
; (def two
;   (L f. x. (f (f x))))
; (def three
;   (L f. x. (f (f (f x)))))

; The successor of n is a function that:
;   - Takes f
;   - Returns another function that takes x
;   - Applies f to the result of applying n f to x. This is a way of generalizing the n compositions of f that we have seen in the general case n of the Church numerals.
(def succ
  (L n. (L f. (L x. (f ((n f) x))))))

; define numerals in terms of successors of previous one
(def one
  (succ zero))
(def two
  (succ (succ zero)))
(def three
  (succ (succ (succ zero))))


(def n-zero?
  (L n. ((n (L x. false)) true)))

(def plus1
  (L x. (+ x 1)))

(def n->int
  (L n. ((n plus1) 0)))

(defn int->n [i]
  (if (zero? i)
    zero
    (succ (int->n (- i 1)))))

(L f. (L x. (f ((zero f) x))))

(def L->str
  (L f. ((f (L n. (format "(f %s)" n))) "n")))
(def num->str
  (L f. (format "L f. n. %s" (L->str f))))

(testing "num->str"
    (is (= (num->str zero) "L f. n. n"))
    (is (= (num->str one) "L f. n. (f n)"))
    (is (= (num->str two) "L f. n. (f (f n))"))
    ; three
    (is (= (num->str (succ (succ (succ zero)))) "L f. n. (f (f (f n)))")))
