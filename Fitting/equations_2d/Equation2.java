/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_2d;

import java.util.Arrays;

public abstract class Equation2 {

	double[] paramsX, paramsY;
	
	public Equation2(double[] parametersX, double[] parametersY) {
		paramsX = parametersX;
		paramsY = parametersY;
		
	}
	public abstract double evaluate(double x, double y);
	public void setParams(double[] newParams) { paramsX = newParams; }
	public double[] getParamsX() {
		return Arrays.copyOf(paramsX, paramsX.length);
	}
	
	public final static int X = 0;
	public final static int Y = 1;
	public double getParam(int x_or_y, int idx) { 
		switch(x_or_y) {
		case X:
			return paramsX[idx];
		case Y:
			return paramsY[idx];
		default:
			throw new RuntimeException("Must use the class variables \"X\" or \"Y\" as the first argument");
		}
	}
	public abstract String toString(double x, double y);
	public void setParam(int x_or_y, double val, int idx) { 
		switch(x_or_y) {
		case X:
			paramsX[idx] = val;
			break;
		case Y:
			paramsY[idx] = val;
			break;
		default:
			throw new RuntimeException("Must use the class variables \"X\" or \"Y\" as the first argument");
		}
	}
	public void incrementParam(int x_or_y, double increment, int idx) { 
		switch(x_or_y) {
		case X:
			paramsX[idx] += increment;
			break;
		case Y:
			paramsY[idx] += increment;
			break;
		default:
			throw new RuntimeException("Must use the class variables \"X\" or \"Y\" as the first argument");
		}
	}
	public void decrementParam(int x_or_y, double decrement, int idx) { 
		switch(x_or_y) {
		case X:
			paramsX[idx] -= decrement;
			break;
		case Y:
			paramsY[idx] -= decrement;
			break;
		default:
			throw new RuntimeException("Must use the class variables \"X\" or \"Y\" as the first argument");
		}
	}
}
