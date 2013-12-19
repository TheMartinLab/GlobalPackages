/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import jama.Matrix;
import equations_1d.Equation1;

public abstract class _TESTING_Fitting {

	protected Equation1 e;
	protected double[][] initialData, initialBounds;
	protected double[][] convertedData, convertedBounds;
	protected Matrix J, Y, b, covar, corr;
	protected double[] stdDev;

	/** METHODS TO OVERRIDE **/
	protected abstract double[][] convertData();
	protected abstract double[][] convertBounds();
	public abstract double[] getFit();
	
	/** GENERIC METHODS **/
	
	/** GETTERS/SETTERS **/
	public Matrix getCovar() { return covar; }
	public Matrix getCorr() { return corr; }
	public double[] getStdDev() { return stdDev; }
}
