/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_1d;

public class Gaussian1 extends Equation1 {

	private double A, mu, sigma;
	/**
	 * A gaussian has three parameters: the pre-exponential (A), the peak center (mu) and the standard deviation (sigma)
	 * f(x) = A * exp(-(x-mu)^2/(2*sigma^2))
	 * @param parameters [A, mu, sigma]
	 */
	public Gaussian1(double[] parameters) {
		super(parameters);
		A = params[0];
		mu = params[1];
		sigma = params[2];
	}

	@Override
	public double evaluate(double x) {
		A = params[0];
		mu = params[1];
		sigma = params[2];
		return A*Math.exp(-1*Math.pow(x-mu, 2)/(2*Math.pow(sigma,2)));
	}

	@Override
	public String toString(double val) {
		A = params[0];
		mu = params[1];
		sigma = params[2];
		return A + "* e^(- (" + val + " - " + mu + ")^2 / (2 * " + sigma + ")^2)";
	}

}
