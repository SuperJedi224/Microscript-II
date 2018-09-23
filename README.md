# Microscript II

Originally published November 7, 2015

Relocated from Pastebin November 8, 2015

---

Microscript II is a dynamically-typed successor to Microscript, one of my earlier programming language designs.

It is intended primarily for code golfing.

Compared to Microscript, it has 3 main stacks (plus an extra limited-access stack to which continuations are automatically pushed on initialization) instead of the original 2 and multiple new data types.

Not counting the special `null` value (type id -1), it has the following types:
```
Type                   ID        Description
INT                    0         64-bit two's complement signed integer
FLOAT                  1         64-bit IEE754 float
BOOLEAN                2         Can be true or false
STRING                 3         A string of characters in some implementation-specific encoding (ASCII or a superset thereof, the reference implementation uses UTF-16)
CODE                   4         A block of code (functions as an anonymous subroutine)
QUEUE                  5         A queue of zero or more objects
CONTINUATION           6         A snapshot of the interpreter's memory (variables, primary stacks, and stack selection; but not the Continuation stack) at the time the continuation object is initialized
```

The full language specs can be found by opening language-specs.md.

---

![](https://i.creativecommons.org/l/by-sa/4.0/88x31.png)

This work is licensed under a [Creative Commons Attribution-ShareAlike 4.0 International License.](http://creativecommons.org/licenses/by-sa/4.0/)

