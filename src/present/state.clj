(ns present.state
  (:import [com.googlecode.lanterna.input KeyType]))

(defn initial []
  {:loading true})

(defn update-key [state message]
  (case [(keyword (.toLowerCase (.toString (.getKeyType (:key message)))))
         (.getCharacter (:key message))]
    [:escape nil] (assoc state :exit true)
    [:character \q] (assoc state :exit true)
    [:character \j] (update state :current-slide #(min (inc %) (dec (count (:slides state)))))
    [:character \k] (update state :current-slide #(max (dec %) 0))
    state))

(defn update-state [state message]
  (case (:message message)
    :key-pressed (update-key state message)
    :load-failed (-> state
                     (dissoc :slides)
                     (assoc :load-error (:load-error message)))
    :load-succeeded (-> state
                        (dissoc :load-error)
                        (assoc :slides (:slides message)
                               :current-slide 0))
    state))
