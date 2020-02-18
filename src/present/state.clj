(ns present.state
  (:import [com.googlecode.lanterna.input KeyType]))

(defn initial []
  {:loading true
   :debug false})

(defn ^:private slide-min-rows [slide]
  (dec (count slide)))

(defn ^:private row-min-columns [row]
  (cond
    (string? row) (count row)
    (and (vector? row) (= (first row) :centered-text)) (count (second row))
    :else 0))

(defn ^:private slide-min-columns [slide]
  (apply max (map row-min-columns (rest slide))))

(defn ^:private slides-min-rows [slides]
  (apply max (map slide-min-rows slides)))

(defn ^:private slides-min-columns [slides]
  (apply max (map slide-min-columns slides)))

(defn ^:private update-key [state message]
  (case [(keyword (.toLowerCase (.toString (.getKeyType (:key message)))))
         (.getCharacter (:key message))]
    ([:escape nil] [:character \q]) (assoc state :exit true)
    [:character \j] (update state :current-slide #(min (inc %) (dec (count (:slides state)))))
    [:character \k] (update state :current-slide #(max (dec %) 0))
    [:character \d] (update state :debug not)
    state))

(defn update-state [state message]
  (case (:message message)
    :key-pressed (update-key state message)
    :load-failed (-> state
                     (dissoc :slides)
                     (assoc :loading false
                            :load-error (:load-error message)))
    :load-succeeded (-> state
                        (dissoc :load-error)
                        (assoc :loading false
                               :slides (:slides message)
                               :current-slide 0
                               :min-columns (slides-min-columns (:slides message))
                               :min-rows (slides-min-rows (:slides message))))
    state))


(comment
  (slide-min-rows [:slide "hello" "world"])
  (slide-min-columns [:slide "hello" [:vertical-space] "w" [:centered-text "banana"]])

  )
