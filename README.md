# Present

Present is a presentation tool for creating simple text-based presentations. Presentations are defined in a declarative text-based format and rendered to a terminal.


## Example presentation

From <sample-presentations/hello-world>.

```
@ version 1
# slide
:
|Using present

|John Doe
:

# slide
Some slide with a
two-line title
:
* point one
 * subpoint

* point two
:
```


## Development

A recent version of Clojure is required.

```shell
make repl  # start a network repl

make run   # run present
```
