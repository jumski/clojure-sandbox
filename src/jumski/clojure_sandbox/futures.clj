(ns jumski.clojure-sandbox.futures)

(defn y []
  (do
    (future (Thread/sleep 2000) 23)))


