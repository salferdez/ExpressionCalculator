import java.io.*;
import java.util.Scanner;

public class ExpressionCalculator{

    public static void main(String[] args) {
        // System.in and System.out by default
        InputStream input = System.in;
        PrintStream output = System.out;

        /*
        Handles reading from or printing to according to the number of arguments
        (only first two accounted for)
         */
        try {
            if (args.length >= 1) {
                input = new FileInputStream(args[0]);
            }
            if (args.length >= 2) {
                output = new PrintStream(new FileOutputStream(args[1]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
            return;
        }

        Scanner sc = new Scanner(input);

        while (sc.hasNextLine()) {
            // Takes care of spaces so they are not needed to account for while parsing
            String line = sc.nextLine().trim();
            if (line.isEmpty()){
                // Respects empty lines
                output.println();
                continue;
            }

            try {
                Expr expr = Parser.parseExpressionString(line);
                int result = expr.evaluate();
                output.println(result);
            } catch (Exception e) {
                output.println(e.getClass() + ": " + e.getMessage());
            }
        }

        sc.close();
        if (output != System.out) {
            output.close();
        }
    }

}
