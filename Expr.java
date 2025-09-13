/**
 * Algebraic data type:
 * Expr = Expr Opr Expr | Int
 * <p>
 *     Or, expressed like a production rule of a GCL:
 *     Expr -> Expr Opr Expr | Int
 * </p>
 */
public interface Expr {
    int evaluate();
}