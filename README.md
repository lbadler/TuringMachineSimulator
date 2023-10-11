# TuringMachineSimulator
This project simulates the execution of a turing machine on a provided input string.

The turing machine should be in a separate file encoded in the following manner:
Line 1 should include the starting state
- ex: "start: qinit"
Line 2 should include the list of accepting states, separated by commas and spaces
 - ex: "accept: qgood, qyes, qhappy"
Line 3 should include the list of rejecting states, separated by commas and spaces
- ex: "reject: qgood, qyes, qhappy"

The remainder of the file is used for transitions, encoded as follows:
"qinit, 0 -> qmoving, 1, r"
The 5 elements of this transition are as follows:
Current state
Current character
New State
Replacement character
Direction to move (either r or l)
Separators must be -> and , as in the example, 1 transition per line

Blanks are represented as _
There is no explicit comment syntax, but lines beyond the first 3 not containing an -> are ignored


Also included is a sample Turing machine, that can be used both as an example of the encoding and to run the simulation.
This machine takes an input of two binary strings separated by #, and accepts if the first is a subsequence of the second
ex: 00#100 accepts, 010#001 rejects
