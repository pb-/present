(ns present.core
  (:require [instaparse.core :as insta])
  (:import [com.googlecode.lanterna.terminal.ansi UnixTerminal]
           [com.googlecode.lanterna.terminal TerminalResizeListener]
           [com.googlecode.lanterna.screen TerminalScreen]))

(def state-storage (atom {:loading true}))

(defn load-presentation [filename]
  ((insta/parser (clojure.java.io/resource "grammar.bnf")) (slurp filename)))

(comment
  (load-presentation "sample-presentations/hello-world"))

(defn render [screen]
  (let [text (.newTextGraphics screen)]
    (.clear screen)
    (.doResizeIfNecessary screen)
    (.putString text 1 1 "hello world")
    (.putString text 1 2 (str "term size " (.toString (.getTerminalSize screen))))
    (.refresh screen)))


(defn -main []
  (let [screen (TerminalScreen. (UnixTerminal.))]
    (.startScreen screen)
    (.setCursorPosition screen nil)
    (.addResizeListener
      (.getTerminal screen)
      (reify TerminalResizeListener
        (onResized [this term new-size] (render screen))))
    (render screen)
    (.readInput screen)
    (.stopScreen screen)))
