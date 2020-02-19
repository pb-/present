# Present

Present is a presentation tool for creating simple text-based presentations. Presentations are defined in a declarative text-based format and rendered to a terminal.

Try it with

```shell
clojure -m present.core sample-presentations/hello-world

# or simply
make
```


## Example presentation

From [sample-presentations/hello-world](sample-presentations/hello-world).

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

# slide
A pipe character in
front centers text

|I am centered

# slide
A colon inserts
vertical space
:
I am on the bottom
(can be used multiple times)
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


## Future features

 * Hot reload the presentation file
 * Markup for colors
