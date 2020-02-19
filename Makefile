run:
	clojure -m present.core sample-presentations/hello-world
.PHONY: run

repl:
	clojure -A:nrepl
.PHONY: repl
