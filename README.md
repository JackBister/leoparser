#"Leonardo" parser

This is a parser for an assignment given in the programming paradigms class at the royal institute of technology (KTH) in Sweden.

In the assignment, you are supposed to parse a fictional programming language that causes a tortoise named "Leonard" to walk around. Leonard has a pen strapped to him that will draw on a canvas as long as it is in the "down" position, which can be chosen by the user. The assignment didn't include actually making a drawing out of it and at the moment I don't feel like doing that, so I'll leave that as an exercise for the reader. The program does print out the color, starting position and ending position of each line Leonard draws.

##Commands
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

##Notice to would-be cheaters
I don't know if this assignment will be reused for next years' class, but if it is I would heavily recommend against copying this code. Since this code has been submitted to Kattis you will in all likelihood get caught for plagiarizing and punished accordingly.
