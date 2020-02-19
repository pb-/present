run:
	clojure -m present.core
.PHONY: run

repl:
	clojure -A:nrepl
.PHONY: repl
