**Memory Structure**

The language's memory structure consists of two variables (here called `x` and `y`), three primary stacks (arranged in a wrapping ring, of which one will be selected at any time), and an additional limited-access stack which may only contain `CONTINUATION` objects.

**Types**

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

The special null value has type ID -1.

QUEUE is the only mutable type. The serialization of QUEUEs for continuations is currently undefined.

**Stringifying Objects**

INTs and FLOATs should be stringified to strings containing a base-10 representation of their values. null becomes "null", true becomes "true", and false becomes "false". Strings should remain unchanged.

Code blocks should be stringified as their source wrapped in braces.

Queues should be stringified by joining the stringified versions of their elements (wrapping strings in quotes), separated by commas; then wrapping the resulting string in square brackets.

The stringification of CONTINUATION objects is undefined.

**Instructions:**

```
<A base-10 integer literal>      Parse as an INT and store it to x.
<A base-10 float literal>        Parse as a FLOAT and store it to x.
'<any character>                 Store the code point of that character to x.
"<any string>"                   Creates a string and stores it to x. Now supports escaping characters.
{<any sequence of commands>}     Creates a code block and stores it to x. Code block literals may be nested.
(<any sequence of commands>)     Run the code between the parentheses only if converting x to a BOOLEAN¹ yields true. These may be nested.
                                  -Any parentheses left open at the end of a block will be closed automatically
[<any sequence of commands>]     Run the code between the parentheses repeatedly while converting x to a BOOLEAN yields true. These may be nested.
                                  -This counts as a separate block from the surrounding scope, thus, x may be used as a continue statement.
                                  -Any brackets left open at the end of the surrounding block will be closed automatically
=                                Pop a value off the stack. If x and this object are equal, set x to true. Otherwise, set x to false.
                                  -Continuations should be compared by reference, queues by contents, code blocks by source, and all other types by value.
                                  -INTs and FLOATs may be equal, but, other than that, values of different types may not be equal.
~                                Dependent on the type of x:
                                  -If x is an INT, take its bitwise NOT and store it to x.
                                  -If x is a code block, run it.
                                  -If x is a QUEUE, remove its first element and add it to the selected stack.
                                  -Otherwse, throw an exception.
e                                Dependent on the type of x:
                                  -If x is an INT or a FLOAT, raise 2 to the power of x and store the result in x as a FLOAT.
                                  -Otherwse, throw an exception.
E                                Dependent on the type of x:
                                  -If x is an INT or a FLOAT, raise 10 to the power of x and store the result in x as a FLOAT.
                                  -Otherwse, throw an exception.
_                                Dependent on the type of x:
                                  -If x is a STRING, parse it as an INT and store the result to x.
                                  -If x is a FLOAT, truncate it to an INT and store the result to x.
                                  -If x is a BOOLEAN, convert it to an INT (true=1,false=0) and store the result to x.
                                  -Otherwise, throw an exception.
@                                Dependent on the type of x:
                                  -If x is an INT or a FLOAT, take its square root as a FLOAT and store it in x.
                                  -Otherwise, throw an exception.
R                                Dependent on the type of x:
                                  -If x is an INT, pick a random integer on [0,x) and store it in x
                                  -If x is a FLOAT, pick a random float on [0,x) and store it in x
                                  -Otherwise, pick a random float on [0,1) and store it in x
<                                Select the stack to the left (stack selection is wrapping)
>                                Select the stack to the right (stack selection is wrapping)
?                                Convert x to a BOOLEAN and store the result in x.
!                                Convert x to a BOOLEAN, negate it, and store the result in x.
I                                Read a line from the input and store it to x.
N                                Read a line from the input, parse it as an INT, and store to x.
F                                Read a line from the input, parse it as a FLOAT, and store to x.
p                                Print x.²
P                                Print x, followed by a newline.
q                                Print x, wrapped in quotes.
Q                                Print x, wrapped in quotes and followed by a newline.
n                                Print a newline.
a                                Until the selected stack is empty, pop objects off it and print them, with a newline after each.
f                                If x is a string, replace all instances of %s with elements polled from y if y is a queue or from the stack otherwise, storing the result in x. If x is not a string, throw an exception.
D                                Get the approximate number of milliseconds since midnight UTC on January 1, 1970 and store it in x.
T                                Get the approximate number of microseconds since program execution started and store it in x.
C                                Create a CONTINUATION object, add it to the continuation stack, and store it in x.
L                                Dependent on the type of x:
                                  -If x is a CONTINUATION, load it³
                                  -Otherwise, load a CONTINUATION popped off the continuation stack³
+                                Dependent on the types of x and a value popped off the stack⁴:
                                  -If x is null, set x to the popped object.
                                  -If both are INTs, sum them as an INT and store the result in x.
                                  -If both are BOOLEANs, OR them and store the result in x.
                                  -If one is an INT and the other is a FLOAT; or if both are FLOATs, sum them as a FLOAT and store the result in x.
                                  -If one is an INT and the other is a BOOLEAN, convert the boolean to an INT (true=1,false=0), sum them, and store the result in x. 
                                  -If x is a QUEUE, add the popped object to the end of it.
                                  -If x is a STRING, append a string representation of the popped object and store the result in x.
                                  -If both are code blocks, merge their sources into a new code block and store the result in x.
                                  -If x is a code block, append a string representation of the popped object to its source and store the result in x.
                                  -If the popped object is a STRING, prepend a string representation of x and store the result in x.
                                  -Otherwise, throw an exception.  
*                                Dependent on the types of x and a value popped off the stack⁴:
                                  -If both are INTs, find their product as an INT and store the result in x.
                                  -If both are BOOLEANs, AND them and store the result in x.
                                  -If one is an INT and the other is a FLOAT; or if both are FLOATs, find their product as a FLOAT and store the result in x. 
                                  -If one is an INT and the other is a STRING, repeat the string that many times and store the result in x. 
                                  -If one is an INT and the other is a code block, execute the code block that many times
                                  -If one is an INT and the other is a queue, merge that many copies of the queue and store the result in x.
                                  -Otherwise, throw an exception                          
|                                If converting x to a boolean would yield true, leave x unchanged. Otherwise, pop from the selected stack and store to x.
&                                If converting x to a boolean would yield false, leave x unchanged. Otherwise, pop from the selected stack and store to x.
s                                Add x to the selected stack.
o                                Pop from the selected stack and store in x.
k                                Without popping, store the top value on the selected stack in x.
d                                Duplicate the top value on the selected stack.
#                                Get the size of the selected stack and store it to x.
$                                Create an empty queue and store it to x.
v                                Set y equal to x.
l                                Set x equal to y.
`                                Exchange the values of x and y.
t                                Get the ID of the type of x, and store it in x.
x                                Halt execution of current code block.
h                                Halt execution of program.
;                                If x is a positive integer, set x to true if it is prime or false otherwise; else, throw an exception.
-⁵                               Dependent on type of x and an object o popped off the selected stack.
                                   -If both are INTs, find x-o as an INT and store it in x.
                                   -If one is a FLOAT and the other is an INT, or both are floats, find x-o as a FLOAT and store it in x.
                                   -If both are STRINGs, remove all instances of o from x and store the result in x.
                                   -If both are BOOLEANs, xor them and store the result in x.
                                   -Otherwiswe, throw an exception.
```

¹false, null, the empty string, an empty queue, and 0 (as an INT or a FLOAT) all become false. Anything else becomes true.

²Unless execution is explicity halted using the h command, this will be done automatically upon termination

³When a CONTINUATION is loaded, both variables, the contents of the 3 primary stacks, and the location of the stack selection pointer will be restored to their states as of when the CONTINUATION was created.

⁴Situations higher on this list have precedence over situations lower on it

⁵Only when not part of a number literal

---

![](https://i.creativecommons.org/l/by-sa/4.0/88x31.png)

This work is licensed under a [Creative Commons Attribution-ShareAlike 4.0 International License.](http://creativecommons.org/licenses/by-sa/4.0/)
