(ns jumski.clojure-sandbox.atoms-and-conflicts)

(def counter (atom 0))
(def foo (atom 1))
(defn slow-inc [n]
    (swap! counter inc)
      (Thread/sleep 200)
        (inc n))

(comment
  (doall
    (map (fn [_]
      (let [_ (pmap
                (fn [_]
                  (swap! foo slow-inc))
                (range 100))
            c @counter
            f @foo]
        {:counter c :foo f}))
        (range 10)))
  )
