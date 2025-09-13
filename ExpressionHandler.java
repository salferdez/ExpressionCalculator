public class ExpressionHandler{

    public static class IntVal implements Expr {

        private final int value;

        public IntVal(int value) {
            this.value = value;
        }

        @Override
        public int evaluate() {
            return value;
        }
    }
		
		/**
		 * Algebraic data type (example in Haskell: data Expr = Expr Opr Expr | Int)
		 * In Grammar Theory notation: Expr -> Expr Opr Expr | Int
		 *
		 * <p>
		 *		NOTE: with operators of same priority, Expressions on the left have higher priority in the moment of
		 *		evaluation that Expressions on the right.
		 * </p>
		 */
    public static class Expression implements Expr {
        private final Expr left;
        private final Opr operator;
        private final Expr right;

        public Expression(Expr left, Opr operator, Expr right) {
            this.operator = operator;
            this.right = right;
            this.left = left;
        }

        public Opr getOperator() {
            return operator;
        }

        public Expr getRight() {
            return right;
        }

        public Expr getLeft() {
            return left;
        }

        /**
         * Evaluates each left and right Expr recursively until only int values to be operated remain.
         *
         * <p>
         *     Note that, between same order operators, the left side has priority by default.
         * </p>
         *
         * @return the int value result of all the operations
         */
        @Override
        public int evaluate() {
            int l = left.evaluate();
            int r = right.evaluate();
            switch (operator) {
                case ADD: return l + r;
                case SUB: return l - r;
                case MUL: return l * r;
                case DIV:
                    try {
                        return l / r;
                    }catch (ArithmeticException e) {
                        throw new ArithmeticException("Division by zero");
                    }
                default: throw new RuntimeException("Failed evaluation of operator: " + operator);
            }
        }
    }
}
