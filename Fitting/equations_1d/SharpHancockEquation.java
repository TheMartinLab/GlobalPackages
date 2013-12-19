/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_1d;

public class SharpHancockEquation extends Equation1 {

	/**
	 * 
	 * @param parameters [kA, t0, n]
	 * @param isFittingParameter corresponding boolean values for each parameter
	 */
	public SharpHancockEquation(double[] parameters) {
		super(parameters);
	}

	@Override
	public double evaluate(double a) {
		double val = params[2] * a + params[2] * Math.log(params[0]);
		return val;
	}

	@Override
	public String toString(double val) {
		return "e^(-(" + params[0] + " * (" + val + "-" + params[1] + "))^" + params[2];
	}
	@Override
	public String toString() {
		return "e^(-(" + params[0] + " * (t-" + params[1] + "))^" + params[2];
	}

}
