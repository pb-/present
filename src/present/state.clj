(ns present.state
  (:import [com.googlecode.lanterna.input KeyType]))

(defn initial []
  {:loading true})

(defn update-state [state message]
  (case (:message message)
    :key-pressed (if (or (= (.getKeyType (:key message)) KeyType/Escape)
                         (= (.getCharacter (:key message)) \q))
                   (assoc state :exit true)
                   state)
    :load-failed (-> state
                     (dissoc :slides)
                     (assoc :load-error (:load-error message)))
    state))
