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

(defn ^:private compute-spaces [rows total-space]
  (let [indexes (keep-indexed #(when %2 %1) rows)
        index-map (into {} (map-indexed (fn [i v] [v i]) indexes))
        num-verticals (count index-map)
        vertical-space (+ (- total-space (count rows)) num-verticals)
        pad-index-bound (- num-verticals (mod vertical-space num-verticals))]
    (map-indexed
      (fn [i row]
        (if row
          (+
           (quot vertical-space num-verticals)
           (if (>= (index-map i) pad-index-bound) 1 0))
          1))
      rows)))

(defn ^:private slide! [text term-size state]
  (let [slide (get (:slides state) (:current-slide state))
        rows (rest slide)
        spaces (compute-spaces (map #{[:vertical-space]} rows) (.getRows term-size))
        columns (.getColumns term-size)
        middle (fn [string columns] (quot (- columns (count string)) 2))]
    (loop [current-row 0
           [[row space] :as rows] (map vector rows spaces)]
      (when (not-empty rows)
        (let [column (if (= (first row) :centered-text) (middle (second row) columns) 0)
              string (cond (string? row) row
                           (= (first row) :centered-text) (second row)
                           (= row [:vertical-space]) ""
                           :else (str row))]
          (.putString text column current-row string))
        (recur (+ current-row space) (rest rows))))))

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
      :else (slide! text term-size state))
    (.refresh screen)))


(comment
  (compute-spaces [false] 9)
  (compute-spaces [false false true false true] 9)
  (compute-spaces [false false true false true] 10)
)
