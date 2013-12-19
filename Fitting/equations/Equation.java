/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations;

import java.util.Arrays;

public abstract class Equation {

	double[] params;
	
	public Equation(double[] parameters) {
		params = parameters;
		
	}
	public abstract double evaluate(double x);
	public void setParams(double[] newParams) { params = newParams; }
	public double[] getParams() {
		return Arrays.copyOf(params, params.length);
	}
	public double getParam(int idx) { return params[idx]; }
	public abstract String toString(double val);
	public void setParam(double val, int idx) { params[idx] = val; }
	public void incrementParam(double increment, int idx) { params[idx] += increment; }
	public void decrementParam(double decrement, int idx) { params[idx] -= decrement; }
}
