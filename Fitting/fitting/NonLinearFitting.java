/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import io.StringConverter;

import java.util.Arrays;
import java.util.Vector;

import _TESTING_regression.RegressionException;
import _TESTING_regression.RegressionXY;

import jama.Matrix;
import equations_1d.Avrami;
import equations_1d.Equation1;

public class NonLinearFitting extends RegressionXY {

	private double[] data;
	private double[] time;
	private Equation1 e;
	private Constraint[] c;
	private boolean[] isFittable, initIsFittable;
	private int numFittable = 0;
	private double delta = 1e-5;
	private double sumSqResid, sumSqTotal;
	private double minTime, maxTime;
	private double[] param_stdDev;
	private int[] paramIdxs;
	private Matrix corr, covar;
	private int numDataPointsInFit;
	private double alphaMin, alphaMax;
	/* constants for switch blocks */
	private final static int DIVIDE = 0;
	private final static int MULTIPLY = 1;
	private final static int ADD = 2;
	private final static int SUBTRACT = 3;
	private final static int MAX_NUM_ITERATIONS = 35;
	/* end constants for switch blocks */
	
	public NonLinearFitting(double[] data, double[] time, Equation1 e, Constraint[] c, boolean[] isFittable,
				double alphaMin, double alphaMax) {
		this.data = data;
		this.time = time;
		this.e = e;
		this.c = c;
		this.isFittable = isFittable;
		this.alphaMin = alphaMin;
		this.alphaMax = alphaMax;
		this.initIsFittable = Arrays.copyOf(isFittable, isFittable.length);
	}
	public NonLinearFitting(double[][] dataAndTime, Equation1 e, Constraint[] c, boolean[] isFittable, 
			double alphaMin, double alphaMax) {
		data = new double[dataAndTime.length];
		time = new double[dataAndTime.length];
		for(int i = 0; i < dataAndTime.length; i++) {
			data[i] = dataAndTime[i][1];
			time[i] = dataAndTime[i][0];
		}
		this.e = e;
		this.c = c;
		this.isFittable = isFittable;
		this.alphaMin = alphaMin;
		this.alphaMax = alphaMax;
		this.initIsFittable = Arrays.copyOf(isFittable, isFittable.length);
		minTime = e.getParam(1);
		maxTime = findPercent(alphaMax);
	}
	public NonLinearFitting() {
		
	}
	public void run() throws RegressionException {
		isFittable = Arrays.copyOf(initIsFittable, initIsFittable.length);
 		fitAvrami();
		isFittable = Arrays.copyOf(initIsFittable, initIsFittable.length);
	}
	private void fitAvrami() throws RegressionException {
		int numFittable = numFittable(isFittable);
		paramIdxs = new int[numFittable];
		int idx = 0;
		boolean[] originalIsFittable = Arrays.copyOf(isFittable, isFittable.length);
		double newSumSq, oldSumSq;
		newSumSq = sumOfResidualSquares();
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { paramIdxs[idx++] = i; }
		}
		int iterations = 0;
		int maxIterations = 250;
		do {
			oldSumSq = newSumSq; 
			idx = 0;
			for(int i = 0; i < paramIdxs.length; i++) {
				fitParam(paramIdxs[i]);
				isFittable = originalIsFittable;
			}
			newSumSq = sumOfResidualSquares();
			if(++iterations > maxIterations) { 
				throw new RegressionException("Max fitting iterations in NonLinearFitting.java reached. (" + maxIterations + ")");
			}
		} while(!isConverged(oldSumSq, newSumSq));
		fitParams(paramIdxs);
	}
	private boolean checkParamsForNaN() {
		double[] params = e.getParams();
		for(int i = 0; i < params.length; i++) {
			if(Double.isNaN(params[i])) {
				return true;
			}
		}
		return false;
	}
	private void fitParam(int idx) throws RegressionException {
		//System.out.println("\n\nFitting param: " + idx);
		Arrays.fill(isFittable, false);
		isFittable[idx] = true;
		fit();
	}
	private void fitParams(int[] idx) {
		//System.out.println("\n\nFitting params: " + Arrays.toString(idx));
		Arrays.fill(isFittable, false);
		for(int i = 0; i < idx.length; i++) {
			isFittable[idx[i]] = true;
		}
		fit();
	}
	private void fit() throws RegressionException {
		double[] newParams;
		Vector<double[]> paramsList = new Vector<double[]>();
		paramsList.add(e.getParams());
		//double[] initParams = e.getParams();
		//System.out.println("Initial params: " + Arrays.toString(initParams));
		double oldSumSq, newSumSq;
		newSumSq = sumOfResidualSquares();
		int idx = 0;
		//double initDelta = delta;
		do {
			idx++;
			oldSumSq = newSumSq;
			newParams = step();
			if(newParams == null) {
				throw new RegressionException("one of the parameters is null in the fit() method");
			}
			paramsList.add(newParams);
			e.setParams(newParams);
			newSumSq = sumOfResidualSquares();
			//System.out.println("Params at step " + idx + ": " + Arrays.toString(newParams));
			if(checkParamsForNaN() ) {
				throw new RegressionException("NaN in parameters in the fit() method");
			}
		} while(!isConverged(oldSumSq, newSumSq) && idx < MAX_NUM_ITERATIONS*3);
		paramsList.add(newParams);
		//System.out.println(paramsList.toString());
	}
	private int numFittable(boolean[] isFittable) {
		int numFittable = 0;
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { numFittable++; }
		}
		return numFittable;
	}
	private int determineError() {
		numFittable = numFittable(isFittable);
		double df = getDegreesOfFreedom();
		if(df < 3) {
			//System.out.println("df: " + df);
		}
		double sumSq = sumOfResidualSquares();
		double var_est = 1/(df) * sumSq;
		
		Matrix J = calcJ();
		Matrix JT = J.transpose();
		Matrix err = null;
		try {
			err = (JT.times(J)).inverse();
		} catch(RuntimeException re) {
			return 1;
		}
		covar = err.times(var_est);
		param_stdDev = new double[numFittable];
		double[][] matrixVals = new double[numFittable][numFittable];
		// set the diagonal of the matrix
		for(int i = 0; i < numFittable; i++) {
			for(int j = 0; j < numFittable; j++) {
				if(i==j) {
					param_stdDev[i] = Math.sqrt(covar.get(i, i));
					matrixVals[i][i] = param_stdDev[i] / param_stdDev[i];
				}
			}
		}
		// set the off-diagonals of the matrix
		for(int i = 0; i < numFittable; i++) {
			for(int j = 0; j < numFittable; j++) {
				if(i!=j) {
					matrixVals[i][j] = covar.get(i, j) / (param_stdDev[i] * param_stdDev[j]);
				}
			}
		}
		corr = new Matrix(matrixVals);
		return 0;
	}
	protected int calcDegreesOfFreedom() {
		int numData = 0;
		for(int i = 0; i < data.length; i++) {
			if(!(time[i] < minTime || time[i] > maxTime)) {
				numData++;
			}
		}
		return numData-numFittable;
	}
	public int getDegreesOfFreedom() {
		return calcDegreesOfFreedom();
	}
	private double findPercent(double fraction) {
		if(fraction == 1) { return time[time.length-1 ]; } 
		boolean found = false;
		int idx = 0;
		while(!found && idx < time.length) {
			try {
				if(data[idx] > fraction) {
					if(time[idx] < e.getParam(1)) {
						return e.getParam(1);
					}
					return time[idx];
				}
			} catch(ArrayIndexOutOfBoundsException fuck) {
				fuck.printStackTrace();
			}
			idx++;
		}
		return 0;
	}
	public double getFisherZ() {
		double z = 0;
		double r2 = getR2();
		z = 0.5 * Math.log((1.+r2)/(1.-r2));
		return z;
	}
	public double getR2() {
		double r2 = 0;
		sumOfResidualSquares();
		sumOfTotalSquares();
		r2 = 1-(sumSqResid / sumSqTotal);
		if(r2 < 0) {
			return 0;
		}
		return r2;
	}
	
	private Matrix calcJ() {
		int numFittable = numFittable(isFittable);
		double[][] matrixVals = new double[data.length][numFittable];
		double[] originalFit = getFit();
		Vector<Integer> indices = new Vector<Integer>();
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { indices.add(i); }
		}
		Integer[] idxs = new Integer[indices.size()];
		idxs = indices.toArray(idxs);
		// fill out the matrix
		for(int j = 0; j < numFittable; j++) {
			e.incrementParam(delta, idxs[j]);
			double[] fit = getFit();
			double[] data = getData();
			// constraint so that the non-lienar routine only fits the
			// region between minTime and maxTime
			for(int i = 0; i < matrixVals.length; i++) {
				if(!(time[i] < minTime || time[i] > maxTime)) {
					matrixVals[i][j] = (fit[i] - originalFit[i])/delta;
				}
			}
			e.decrementParam(delta, idxs[j]);
		}
		return new Matrix(matrixVals);
	}
	public double getSumOfSquares() { return getSumSq(); }
	public double[] calcResiduals() { return getResiduals(); }
	public double[] getResiduals() {
		double[] originalFit = getFit();
		double[] resid = new double[data.length];
		for(int i = 0; i < resid.length; i++) {
			resid[i] = data[i]-originalFit[i];
		}
		return resid;
	}
	private void setBounds() {
		if(alphaMin == 0.0) {
			minTime = e.getParam(1);
		} else {
			minTime = findPercent(alphaMin);
			//System.out.println("finding min percent corresponding to alphaMin: " + alphaMin);
		}
		maxTime = findPercent(alphaMax);
		if(minTime > maxTime) { minTime = maxTime-1; }
		if(minTime < time[0]) { minTime = time[0]; }
		//e.setParam(minTime, 1);
	}
	private double[] step() {
		setBounds();
		double[] initialParams = e.getParams();
		Matrix J = calcJ();
		Matrix JT = J.transpose();
		Matrix inv = null;
		try {
			inv = (JT.times(J)).inverse();
		} catch(RuntimeException re) {
			throw new RegressionException(re.getLocalizedMessage());
		}
		Matrix JDagger = inv.times(JT);
		//Matrix isOne = JDagger.times(J);
		Matrix newParams = JDagger.times(toMatrix(getResiduals()));
		int idx = Math.min(newParams.getRowDimension(), newParams.getColumnDimension());
		double[] betterParams = new double[initialParams.length];
		
		double[][] mat = newParams.getArray();
		idx = 0;
		for(int i = 0; i < initialParams.length; i++) {
			if(isFittable[i]) {
				betterParams[i] = mat[idx++][0]+initialParams[i];
			} else {
				betterParams[i] = initialParams[i];
			}
			//System.out.print("param " + i + ": " + betterParams[i]);
		}
		//double initSumSq = sumOfSquares();
		e.setParams(betterParams);
		//double finalSumSq = sumOfSquares();
		
		//System.out.println("\ninit: " + initSumSq + " final: " + finalSumSq + " difference: " + (initSumSq-finalSumSq));
		return betterParams;
		
	}
	public Matrix toMatrix(double[] d) {
		double[][] mat = new double[d.length][1];
		
		for(int i = 0; i < mat.length; i++) {
			mat[i][0] = d[i];
		}
		return new Matrix(mat);
	}
	private boolean isConverged(double oldSumSq, double newSumSq) throws RegressionException {
		if(Double.isInfinite(oldSumSq) || Double.isInfinite(newSumSq)) {
			throw new RegressionException("Sum of squares is infinite.");
		}
		if(Math.abs(oldSumSq - newSumSq) < delta/2) {
			return true;
		}
		if(oldSumSq == newSumSq) { return false; }
		return false;
	}
	private double sumOfTotalSquares() {
		setBounds();
		double average = 0;
		numDataPointsInFit=0;
		
		for(int i = 0; i < data.length; i++) {
			if(!(time[i] < minTime || time[i] > maxTime)) {
				average += data[i];
				numDataPointsInFit++;
			}
		}
		average /= (double) numDataPointsInFit; 
		sumSqTotal = 0;
		for(int i = 0; i < data.length; i++) {
			if(!(time[i] < minTime || time[i] > maxTime)) {
				sumSqTotal += Math.pow(data[i]-average, 2);
			}
		}
		
		return sumSqTotal;
	}
	private double sumOfResidualSquares() {
		setBounds();
		double[] fit = getFit();
		double sumSquares = 0;
		numDataPointsInFit=0;
		for(int i = 0; i < fit.length; i++) {
			if(!(time[i] < minTime || time[i] > maxTime)) {
				sumSquares += Math.pow(fit[i]-data[i], 2);
				numDataPointsInFit++;
			}
		}
		sumSqResid = sumSquares;
		return sumSquares;
	}
	public double[][] getTimeDataFit() {
		double[][] timeDataFit = new double[time.length][3];
		double[] fit = getUnboundedFit();
		
		for(int i = 0; i < timeDataFit.length; i++) {
			timeDataFit[i][0] = time[i];
			timeDataFit[i][1] = data[i];
			timeDataFit[i][2] = fit[i];
		}
		return timeDataFit;
	}
	public double[] getFit() {
		double[] fit = new double[time.length];
		for(int i = 0; i < fit.length; i++) {
			if(time[i] < minTime || time[i] > maxTime) {
				fit[i] = 0;
			} else {
				fit[i] = e.evaluate(time[i]);
			}
		}
		return fit;
	}
	public double[] getUnboundedFit() {
		double[] fit = new double[time.length];
		double val;
		for(int i = 0; i < fit.length; i++) {
			val = e.evaluate(time[i]);
			if(val < 0) { val = 0; }
			fit[i] = val;
		}
		return fit;
	}
	public String getParameters() {
		String s = "\nParameters:\n";
		s += StringConverter.arrayToTabString(e.getParams());
		s += "\nEnd of Parameters\n";
		return s;
	}
	public String getError() {
		String s = "Error: \n";
		s += "Standard deviation in parameters: \n";
		s += StringConverter.arrayToTabString(param_stdDev);
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
	/*
	 * Setters
	 */
	
	public void setDelta(double delta) { this.delta = delta; }
	public void setIsFittable(boolean[] isFittable) { 
		initIsFittable = Arrays.copyOf(isFittable, isFittable.length);
		this.isFittable = Arrays.copyOf(initIsFittable, initIsFittable.length);
	}
	public void setIsFittable(boolean b, int idx) { 
		initIsFittable[idx] = b;
		isFittable = Arrays.copyOf(initIsFittable, initIsFittable.length);
	}
	public void setData(double[] data) { this.data = data; }
	public void setConstraint(Constraint[] c) { this.c = c; }
	public void setConstraint(Constraint c, int idx) { this.c[idx] = c; }
	public void setTime(double[] t) { time = t; }
	
	/*
	 * Getters
	 */
	
	public double[] getData() { return data; }
	public double getDelta() { return delta; }
	public Equation1 getEquation() { return e; }
	public Constraint[] getConstraints() { return c; }
	public boolean[] getIsFittable() { return isFittable; }
	public double[] getTime() { return time; }
	public Matrix getCovar() {
		determineError();
		return covar; 
	}
	public Matrix getCorr() {
		determineError();
		return corr; 
	}
	public double[] getStdDev() {
		if(param_stdDev == null) {
			determineError();
		}
		return param_stdDev; 
	}
	public double[] getParams() { return e.getParams(); }
	public double getSumSq() { return sumOfResidualSquares(); }
	public int getNumDataPoints() {
		sumOfResidualSquares();
		return numDataPointsInFit; 
	}
	@Override
	protected Matrix constructY() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void doFit() throws RegressionException {
		run();
		int df = calcDegreesOfFreedom();
		if(df < this.minimumNumberOfDegreesOfFreedom) {
			throw new RegressionException("Minimum number of degrees of freedom not met. df: " + df + " min df: " + minimumNumberOfDegreesOfFreedom); 
		}
	}
	public void setEquation(Equation1 e2) { e = e2; }
	public void setAlphaMin(double val) { alphaMin = val; }
	public void setAlphaMax(double val) { alphaMax = val; }
}
