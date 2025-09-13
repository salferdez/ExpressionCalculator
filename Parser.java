public class Parser {
    private final String input;
    private int pos;

    public Parser(String input) {
        this.input = input;
        this.pos = 0;
    }

    /*
    Priority: Unary neg -> Parentheses -> Mult || Div -> Sum || Diff
    After that, the left sides of Expr have preference on evaluation.
    I.e., if a new Expr is created, the results of parsing should (generally) be placed on the left side


    Consider that each parser will be called from the bottom-up:
    parse -> parseExpression -> parseMultDiv -> parseUnaryNegParentheses -> parseNumber

    However, the order of evaluation will be inverted:

    parseNumber -> parseUnaryNegParentheses -> parseMultDiv -> parseExpression -> parse
     */

    /**
     * @param s the input string
     * @return the processed Expr ready to be evaluated in Calculadora class
     */
    public static Expr parseExpressionString(String s) {
        return new Parser(s).parse();
    }

    /**
     * General parse function.
     * <p>
     * Starts the chain of parse functions calling each other.
     * Makes sure that all characters were processed
     * @throws
      */
    public Expr parse() {
        Expr result = parseExpression();
        // In order to consume any missing spaces and make sure that pos value doesn't fall short
        skipSpaces();
        if (pos < input.length()) {
            throw new FailedParsingException("Failed at char: " + pos);
        }
        return result;
    }

    /**
     * Compares next char (once all spaces have been skipped) with the argument.
     * DOESN'T advance position, just looks for it.
     * <p>
     *     If sucessful, we can use {@link #match(char)} to advance pos
     * </p>
     * @param c the char to be searched for
     * @return true if char was found, false otherwise
     */
    private boolean peek(char c) {
        skipSpaces();
        return pos < input.length() && input.charAt(pos) == c;
    }

    /**
     * Compares next char (once all spaces have been skipped) with the argument.
     * If found, advances the position once.
     *
     * <p>
     *     NOTE: we still check for length and character matching in case {@link #peek(char)} wasn't used beforehand
     * </p>
     * @param c the char to be searched for
     * @return true if the char was found, false otherwise
     */
    private boolean match(char c) {
        skipSpaces();
        if (pos < input.length() && input.charAt(pos) == c) {
            pos++;
            return true;
        }
        return false;
    }

    /** Handles sums and differences, once all others higher-priority operators have been taken care of.
     *
     * <p>
     *     Uses recursion to create nested expressions
     * </p>
     * @return newly created
     */
    private Expr parseExpression() {
        Expr result = parseMultDiv();
        while (true) {
            skipSpaces();
            if (peek('+')) {
                match('+');
                result = new ExpressionHandler.Expression(result, Opr.ADD, parseMultDiv());
            } else if (peek('-')) {
                match('-');
                result = new ExpressionHandler.Expression(result, Opr.SUB, parseMultDiv());
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Handles multiplication and divisions
     * <p>
     *     Uses recursion to break down expressions containing multiplication or division operations, creating nested
     *     expressions.
     * </p>
     * @return the expr, once signs and parentheses were taken care of, that takes care of * and / operators
     */
    private Expr parseMultDiv() {
        Expr result = parseUnaryNegParentheses();
        while (true) {
            skipSpaces();
            if (peek('*')) {
                match('*');
                result = new ExpressionHandler.Expression(result, Opr.MUL, parseUnaryNegParentheses());
            } else if (peek('/')) {
                match('/');
                result = new ExpressionHandler.Expression(result, Opr.DIV, parseUnaryNegParentheses());
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Handles unary negation and parentheses (higher priority operators)
     * @return the newly created expression with appropriate sign and respecting the parentheses order
     */
    private Expr parseUnaryNegParentheses() {
        skipSpaces();
        int sign = 1;
        while (peek('+') || peek('-')) {
            if (match('-')){
                sign *= -1;
            }
            else {
                match('+');
            }
        }

        skipSpaces();
        Expr result;
        // We either find and expression enclosed in parentheses or an int val
        if (peek('(')) {
            match('(');
            result = parseExpression();
            skipSpaces();
            if (!match(')')) {
                throw new FailedParsingException("Missing closing parenthesis at position: " + pos);
            }
        } else {
            result = parseNumber();
        }

        // Processing unary negation sign
        if (sign == -1) {
            // Creates a new Expression that will yield the correspondent negative int when evaluated
            result = new ExpressionHandler.Expression(new ExpressionHandler.IntVal(0), Opr.SUB, result);
        }
        return result;
    }

    /**
     * Parses int numbers.
     * <p> As long as all read chars are digits, advances position. Once a whitespace/operator is found,
     * a substring is formed with chars from starting position till the one where the last digit was found
     * </p>
     *
     * NOTE: unary negation is taken care of at parseUnaryNegParentheses
     * @return an expression that contains an int value ready to be evaluated
     */
    private Expr parseNumber() {
        skipSpaces();
        int start = pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
        }
        if (start == pos) {
            throw new FailedParsingException("Expected number at position: " + pos);
        }
        int value = Integer.parseInt(input.substring(start, pos));
        return new ExpressionHandler.IntVal(value);
    }

    /**
     * Advances position until all spaces have been skipped
     *
     * <p>
     *     NOTE: spaces should be taken care of by the IO part at {@link Calculadora} but
     *     I will implement this just in case
     * </p>
     */
    private void skipSpaces() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))){
            pos++;
        }
    }
}
