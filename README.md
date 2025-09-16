# A simple calculator that implements concepts from Formal Grammars theory

If provided, reads expressions from an input file and write the evaluated results in a output file. E.g., exp.in and exp.out. Else, if one or both are not specified, it reads from / writes to System.In / System.Out.

- ExpressionHandler.java: defines the Expression class inspired by Algebraic Data Types

- Parser.java: simple, not-so-efficient parser that tries to build recursively a (kind of) derivation tree of expressions, then to be evaluated.

- ExpressionCalculator.java: handles IO and calls Parser methods

NOTE: this is an exercise (from scratch, no skeleton code) for a third-year course of my Computer Science degree, where I'm beggining to implement concepts learned in a previous Automata Theory and Formal Languages course.

* Usage: javac ExpressionHandler.java -> java ExpressionHandler exp.in exp.out || java ExpressionHandler exp.in (output to System.out) || java ExpressionHandler (input by System.In, output to System.Out)
