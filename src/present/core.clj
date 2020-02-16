(ns present.core
  (:require [instaparse.core :as insta]
            [present.render :as r]
            [present.state :as s])
  (:import [com.googlecode.lanterna.terminal.ansi UnixTerminal]
           [com.googlecode.lanterna.terminal TerminalResizeListener]
           [com.googlecode.lanterna.screen TerminalScreen]))

(def state-storage (atom (s/initial)))

(defn load-presentation [filename]
  ((insta/parser (clojure.java.io/resource "grammar.bnf")) (slurp filename)))

(comment
  (load-presentation "sample-presentations/hello-world"))

(defn -main []
  (let [screen (TerminalScreen. (UnixTerminal.))]
    (.startScreen screen)
    (try 
      (.setCursorPosition screen nil)
      (.addResizeListener
        (.getTerminal screen)
        (reify TerminalResizeListener
          (onResized [this term new-size] (r/render! screen @state-storage))))
      (r/render! screen @state-storage)
      (.readInput screen)
      (.stopScreen screen)
      (catch Exception e
        (.stopScreen screen)
        (.printStackTrace e)))))
