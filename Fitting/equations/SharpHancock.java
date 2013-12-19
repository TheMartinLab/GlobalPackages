/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations;

public class SharpHancock extends Equation {

	/**
	 * 
	 * @param parameters [kA, t0, n]
	 * @param isFittingParameter corresponding boolean values for each parameter
	 */
	public SharpHancock(double[] parameters) {
		super(parameters);
	}

	@Override
	public double evaluate(double x) {
		double t0 = params[1];
		if(x < t0) { return 0; }
		
		double kA = params[0];
		double n = params[2];
		double d = Math.exp(-1*Math.pow(kA * (x-t0), n));
		return 1-d;
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
