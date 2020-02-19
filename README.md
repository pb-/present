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


## Key bindings

Vim like, generally.

| Key(s)                                  | Action                             |
| --------------------------------------- | ---------------------------------- |
| q (escape)                              | Quit                               |
| j l (space) (enter) (arrow right/down)  | Next slide                         |
| h k (backspace) (arrow left/up)         | Previous slide                     |
| g (home)                                | First slide                        |
| G (end)                                 | Last slide                         |
| d                                       | Show debug information             |


## Development

A recent version of Clojure is required.

```shell
make repl  # start a network repl

make run   # run present
```
