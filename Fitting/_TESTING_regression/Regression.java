/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regression;

import jama.Matrix;
import _TESTING_regressionModel.Bounds;
import _TESTING_regressionModel.RegressionModel;

public abstract class Regression {

	protected double[][] initialData;
	protected Bounds[] initialBounds;
	protected double[][] convertedData;
	protected Bounds[] convertedBounds;
	protected RegressionModel model;
	protected int minimumNumberOfDegreesOfFreedom = 1;
	protected double delta = 1e-5;
	// ERROR parameters
	protected Matrix covariance, correlation;

	/* Main Methods of the Regression object */
	public abstract void doFit() throws RegressionException;
	protected abstract double calcSumOfSquares();
	protected abstract int calcDegreesOfFreedom();
	protected abstract double[] calcResiduals();
	protected abstract void convertData();
	protected abstract void convertBounds();
	protected abstract int determineNumberOfFittableDataPoints();
	protected double[] convertParametersTo(double[] parameters) {
		return model.convertParametersTo(parameters);
	}
	
	public void finalCheck() throws RegressionException {
		int df = calcDegreesOfFreedom();
		if(df <= minimumNumberOfDegreesOfFreedom) {
			throw new RegressionException("There are : " + df + " degrees of freedom.  Need at least " + minimumNumberOfDegreesOfFreedom);
		}
	}
	/* SETTERS */
	/**
	 * @param data [y, x1, x2, ... , xn]
	 */
	public void setData(double[][] data) { initialData = data; }
	/**
	 * @param bounds [y_bounds, x1_bounds, x2_bounds, ... , xn_bounds]
	 */
	public void setBounds(Bounds[] bounds) { initialBounds = bounds; }
	public void setModel(RegressionModel model) { this.model = model; }
	public void setMinimumNumberOfDegreesOfFreedom(int df) { 
		minimumNumberOfDegreesOfFreedom = df;
	}
	/* ABSTRACT GETTERS */
	public double[][] getInitialData() { return initialData; }
	public Bounds[] getInitialBounds() { return initialBounds; }
	public double[][] getConvertedData() { return convertedData; }
	public Bounds[] getConvertedBounds() { return convertedBounds; }
	public abstract double[] getFit();
	
	/* CONCRETE GETTERS */
	public double getSumOfSquares() { return calcSumOfSquares(); }
	public int getDegreesOfFreedom() { return calcDegreesOfFreedom(); }
	public double[] getResiduals() { return calcResiduals(); }
	public RegressionModel getModel() { return model; }
	public double[] getStdDev() { return model.getAllStandardDeviations(); }
	public Matrix getCovariance() {
		return covariance; 
	}
	public Matrix getCorrelation() {
		return correlation; 
	}
	public double getDelta() { return delta; }
}
