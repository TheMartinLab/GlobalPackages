/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_1d;

public class Polynomial1 extends Equation1 {

	/**
	 * 
	 * @param coeff In order of decreasing order: ax^3 + bx^2 + cx + d = y means give me coefficients in order {a, b, c, d}
	 * @param isFittable Indicates whether the corresponding fitting coefficient is a fittable parameter or not.
	 */
	public Polynomial1(double[] coeff) {
		super(coeff);
	}
	
	@Override
	public double evaluate(double x) {
		double[] vals = new double[params.length];
		for(int i = 0; i < params.length; i++) {
			vals[i] = params[i];
		}
		for(int i = 0; i < vals.length; i++) {
			for(int j = 0; j < i; j++) {
				vals[j] *= x;
			}
		}
		double val = 0;
		for(int i = 0; i < vals.length; i++) {
			val += vals[i];
		}
		return val;
	}

	public double getCoeff(int idx) throws ArrayIndexOutOfBoundsException {
		return params[idx];
	}
	
	@Override
	public String toString() {
		int len = params.length-1;
		String s = "";
		for(int i = 0; i < params.length-1; i++, len--) {
			s += params[i] + "x^" + len + " + ";
		}
		s += params[params.length-1];
		
		return s;
	}
	
	@Override
	public String toString(double val) {
		int len = params.length-1;
		String s = "";
		for(int i = 0; i < params.length-1; i++, len--) {
			s += params[i] + " * " + val + "^" + len + " + ";
		}
		s += params[params.length-1];
		
		return s;
	}	
	@Override
	public Object clone() {
		double[] paramsCopy = params.clone();
		return new Polynomial1(paramsCopy);
	}
}
