run:
	clj -m present.core
.PHONY: run

repl:
	clj -A:nrepl
.PHONY: repl
