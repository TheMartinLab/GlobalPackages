/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_1d;

public class EquationTest {

	public static void main(String[] args) {
		double[] coeff = {1, 1, 2};
		double x = 5;
		Equation1 e = new Polynomial1(coeff);
		System.out.println("Equation: " + e);
		System.out.println("Value at " + x + " is: " + e.toString(x) + " = " + e.evaluate(x));
		
		coeff = new double[] {.01, 15, 3};
		x = 300;
		e = new Avrami(coeff);
		System.out.println("Equation: " + e);
		System.out.println("Value at " + x + " is: " + e.toString(x) + " = " + e.evaluate(x));
		
	}
}
