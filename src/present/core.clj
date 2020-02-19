(ns present.core
  (:require [instaparse.core :as insta]
            [present.render :as r]
            [present.state :as s])
  (:import [com.googlecode.lanterna.terminal.ansi UnixTerminal]
           [com.googlecode.lanterna.terminal TerminalResizeListener]
           [com.googlecode.lanterna.screen TerminalScreen]))

(def state-storage (atom (s/initial)))

(defn load-presentation [filename]
  (let [result ((insta/parser (clojure.java.io/resource "grammar.bnf")) (slurp filename))]
    (if (insta/failure? result)
      {:message :load-failed
       :load-error (with-out-str (print (insta/get-failure result)))}
      {:message :load-succeeded
       :slides (into [] result)})))

(defn -main [& args]
  (when (empty? args)
    (println "usage: clojure -m present.core FILE")
    (System/exit 1))
  (let [screen (TerminalScreen. (UnixTerminal.))]
    (swap! state-storage s/update-state (load-presentation (first args)))
    (.startScreen screen)
    (try 
      (.setCursorPosition screen nil)
      (.addResizeListener
        (.getTerminal screen)
        (reify TerminalResizeListener
          (onResized [this term new-size] (r/render! screen @state-storage))))
      (while (not (:exit @state-storage))
        (r/render! screen @state-storage)
        (swap! state-storage s/update-state
               {:message :key-pressed
                :key (.readInput screen)}))
      (.stopScreen screen)
      (catch Exception e
        (.stopScreen screen)
        (.printStackTrace e)))))
