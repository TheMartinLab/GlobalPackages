/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import io.StringConverter;

import java.util.Vector;

import equations_1d.Equation1;

import jama.Matrix;

public abstract class Fitting {

	protected double[] y;
	protected double[] x;
	protected Matrix corr, covar;
	protected boolean[] isFittable;
	protected boolean limitX = false, limitY = false;
	protected double minX, maxX, minY, maxY;
	protected Equation1 e;
	protected double[] param_stdDev;

	public Fitting(Equation1 e, boolean[] isFittable) {
		this.e = e;
		this.isFittable = isFittable;
	}
	
	public abstract int go();
	

	/* generic methods */
	/**
	 * 
	 * @param min Minimum value on the original data set (i.e. not a linearized form of alpha)
	 * @param max Maximum value in the original data set (i.e. not a linearized form of alpha)
	 */
	public void setLimits_X(double min, double max) {
		minX = min;
		maxX = max;
		limitX = true;
	}
	public void setLimits_Y(double min, double max) {
		minY = min;
		maxY = max;
		limitY = true;
	}
	protected boolean withinLimits_X(double x) {
		if(!limitX) { return true; }
		if(x > minX && x <= maxX) { return true; }
		return false;
	}
	protected boolean withinLimits_Y(double y) {
		if(!limitY) { return true; }
		if(y >= minY && y <= maxY) { return true; }
		return false;		
	}
	public double[] getFit() {
		double[] fit = new double[x.length];
		for(int i = 0; i < fit.length; i++) {
			if(x[i] > minX && x[i] < maxX) {
				fit[i] = e.evaluate(x[i]);
			} else {
				fit[i] = 0;
			}
		}
		return fit;
	}
	protected int numFittable(boolean[] isFittable) {
		int numFittable = 0;
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { numFittable++; }
		}
		return numFittable;
	}
	
	public void setData(double[] data) { this.y = data; }
	

	public double getSumSq() { return sumOfSquares(); }
	public abstract double sumOfSquares();
	public double[] getParams() { return e.getParams(); }
	public abstract boolean[] getIsFittable();
	public abstract int getNumDataPoints();
	public abstract double[][] getTimeDataFit();
	public abstract double[] getStdDev();
	public String getParameters() {
		String s = "\nParameters:\n";
		s += StringConverter.arrayToTabString(e.getParams());
		s += "\nEnd of Parameters\n";
		return s;
	}
	public String getError() {
		String s = "Error: \n";
		s += "Standard deviation in parameters: \n";
		double[] stdev = getStdDev();
		s += StringConverter.arrayToTabString(stdev);
		s += "\n";
		s += "Correlation matrix:\n";
		double[][] d;
		if(covar != null) {
			d = covar.getArray();
		} else {
			d = new double[1][1];
		}
		for(int i = 0; i < d.length; i++) {
			s += StringConverter.arrayToTabString(d[i]) + "\n";
		}
		s += "Covariance matrix:\n";
		if(corr != null) {
			d = corr.getArray();
		} else {
			d = new double[1][1];
		}
		for(int i = 0; i < d.length; i++) {
			s += StringConverter.arrayToTabString(d[i]) + "\n";
		}
		s += "End of error\n";
		return s;
	}
}
