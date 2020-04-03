(ns jumski.clojure-sandbox.lambda-calculus.macro
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

(testing "L macro expansion"
  (is (= '(clojure.core/fn [a b c] (a b c))
         (macroexpand-1 '(L a. b. c. (a b c))))))
