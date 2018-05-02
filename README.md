# "Leonardo" interpreter
This is an interpreter for an assignment given in the programming paradigms class at the royal institute of technology (KTH) in Sweden.

"Leonardo" is a [graphics turtle](https://en.wikipedia.org/wiki/Turtle_graphics).

## Command line options
`-i <filename>` to specify the input file containing commands. If not specified, stdin will be used as the input.

`-o <filename` to specify where to output the resulting image. The default is "a.png".

## Commands
The language has 8 keywords:

`FORW d` moves Leonardo d steps forward.

`BACK d` moves Leonardo d steps backwards.

`LEFT Θ` turns Leonardo Θ degrees to the left.

`RIGHT Θ` turns Leonardo Θ degrees to the right.

`UP` lifts the pen up if it is down.

`DOWN` lowers the pen if it is up.

`COLOR #rrggbb` sets the color of the pen using standard RGB formatting. e.g. #FF0000 would make the pen red.

`REP r "cmds"` repeats the commands contained within the quotes r times. Quotes are optional if you want to repeat a single statement.

All commands must be followed by a ., except for REP. All commands are case insensitive. Leonardo starts at position (0,0) facing (1,0).
