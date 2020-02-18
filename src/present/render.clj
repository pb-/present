(ns present.render
  (:require [clojure.string :as string])
  (:import [com.googlecode.lanterna TextColor$ANSI]))

(defn ^:private make-centered [text columns]
  (fn [row string]
    (let [column (quot (- columns (count string)) 2)]
      (.putString text column row string))))

(defn ^:private too-small! [text term-size]
  (let [center! (make-centered text (.getColumns term-size))
        middle (quot (.getRows term-size) 2)]
    (.setForegroundColor text TextColor$ANSI/RED)
    (center! (dec middle) "Terminal too small")
    (center! (inc middle) "Try making it bigger")))

(defn ^:private load-error! [text error]
  (.setForegroundColor text TextColor$ANSI/RED)
  (doseq [[line row] (map vector (string/split-lines error) (rest (range)))]
    (.putString text 1 row line)))

(defn ^:private debug! [text term-size state]
  (.setForegroundColor text TextColor$ANSI/WHITE)
  (.putString text 0 0 (str "DEBUG"))
  (.putString text 1 1 (str "current term size " (.toString term-size)))
  (.putString text 1 2 (format "min term size {%dx%d}" (:min-columns state) (:min-rows state))))

(defn render! [screen state]
  (let [term-size (or (.doResizeIfNecessary screen) (.getTerminalSize screen))
        text (.newTextGraphics screen)]
    (.clear screen)
    (cond
      (:load-error state) (load-error! text (:load-error state))
      (:debug? state) (debug! text term-size state)
      (or
        (< (.getColumns term-size) (:min-columns state))
        (< (.getRows term-size) (:min-rows state))) (too-small! text term-size)
      :else (do
              (.putString text 1 1 (str (get (:slides state) (:current-slide state))))))
    (.refresh screen)))


