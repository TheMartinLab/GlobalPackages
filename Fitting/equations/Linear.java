/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations;

public class Linear extends Equation {

	/**
	 * 
	 * @param parameters [kA, t0, n]
	 * @param isFittingParameter corresponding boolean values for each parameter
	 */
	public Linear(double[] parameters) {
		super(parameters);
	}

	@Override
	public double evaluate(double x) {
		double y = params[0];
		for(int i = 1; i < params.length; i++) {
			y += x*params[i];
		}
		return y;
	}

	@Override
	public String toString(double val) {
		String eq = "y = " + params[0] + " + ";
		for(int i = 1; i < params.length; i++) {
			eq += params[0] + " * " + val;
		}
		return eq;
	}
	@Override
	public String toString() {
		String eq = "y = " + params[0] + " + ";
		for(int i = 1; i < params.length; i++) {
			eq += params[0] + " * x";
		}
		return eq;
	}

}
