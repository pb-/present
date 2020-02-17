(ns present.render
  (:require [clojure.string :as string])
  (:import [com.googlecode.lanterna TextColor$ANSI]))

(defn ^:private make-centered [text columns]
  (fn [row string]
    (let [column (unchecked-divide-int (- columns (count string)) 2)]
      (.putString text column row string))))

(defn ^:private too-small! [text term-size]
  (let [center! (make-centered text (.getColumns term-size))
        middle (unchecked-divide-int (.getRows term-size) 2)]
    (.setForegroundColor text TextColor$ANSI/RED)
    (center! (dec middle) "Terminal too small")
    (center! (inc middle) "Try making it bigger")))

(defn ^:private load-error! [text error]
  (.setForegroundColor text TextColor$ANSI/RED)
  (doseq [[line row] (map vector (string/split-lines error) (rest (range)))]
    (.putString text 1 row line)))

(defn render! [screen state]
  (let [term-size (or (.doResizeIfNecessary screen) (.getTerminalSize screen))
        text (.newTextGraphics screen)]
    (.clear screen)
    (cond
      (:load-error state) (load-error! text (:load-error state))
      (< (.getColumns term-size) 70) (too-small! text term-size)
      :else (do
              (.putString text 1 1 "hello world")
              (.putString text 1 2 (str "term size " (.toString term-size)))))
    (.refresh screen)))


