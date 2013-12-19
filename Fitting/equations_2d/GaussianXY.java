/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_2d;

public class GaussianXY extends Equation2 {

	private double A, muX, sigmaX, muY, sigmaY;
	/**
	 * http://en.wikipedia.org/wiki/Gaussian_function#Two-dimensional_Gaussian_function
	 * 
	 * @param parametersX [A, mu, sigma]
	 * @param parametersY [mu, sigma]
	 */
	public GaussianXY(double[] parametersX, double[] parametersY) {
		super(parametersX, parametersY);
		A = parametersX[0];
		muX = parametersX[1];
		sigmaX = parametersX[2];
		muY = parametersY[0];
		sigmaY = parametersY[1];
	}

	@Override
	public double evaluate(double x, double y) {
		A = paramsX[0];
		muX = paramsX[1];
		sigmaX = paramsX[2];
		muY = paramsY[0];
		sigmaY = paramsY[1];
		return A * Math.exp(-1*(Math.pow(x-muX,2)/(2*Math.pow(sigmaX, 2)) + 
				(Math.pow(y-muY,2)/(2*Math.pow(sigmaY, 2)))));
		
	}

	@Override
	public String toString(double x, double y) {
		return A +" * " + "e^(-1*(" + x + " - " + muX + ")^2 / (2 * " + sigmaX + "^2)" +  
			"(" + y + " - " + muY + ")^2 / (2 * " + sigmaY + "^2)";
	}

}
