/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import io.StringConverter;

import java.util.Arrays;
import java.util.Vector;

import jama.Matrix;
import equations_1d.Equation1;

public class _TESTING_NonLinearFitting extends Fitting {

	private double[] data;
	private double[] time;
	private Constraint[] c;
	private boolean[] initIsFittable;
	private int numFittable = 0;
	private double delta = 1e-5;
	private double sumSq;
	private double minTime, maxTime;
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
	
	public _TESTING_NonLinearFitting(double[] data, double[] time, Equation1 e, Constraint[] c, boolean[] isFittable,
				double alphaMin, double alphaMax) {
		super(e, isFittable);
		this.data = data;
		this.time = time;
		this.e = e;
		this.c = c;
		this.alphaMin = alphaMin;
		this.alphaMax = alphaMax;
		this.initIsFittable = Arrays.copyOf(isFittable, isFittable.length);
	}
	public _TESTING_NonLinearFitting(double[][] dataAndTime, Equation1 e, Constraint[] c, boolean[] isFittable, 
			double alphaMin, double alphaMax) {
		super(e, isFittable);
		data = new double[dataAndTime.length];
		time = new double[dataAndTime.length];
		for(int i = 0; i < dataAndTime.length; i++) {
			data[i] = dataAndTime[i][1];
			time[i] = dataAndTime[i][0];
		}
		this.c = c;
		this.alphaMin = alphaMin;
		this.alphaMax = alphaMax;
		this.initIsFittable = Arrays.copyOf(isFittable, isFittable.length);
		minTime = e.getParam(1);
		maxTime = findPercent(alphaMax);
	}
	public int go() {
 		int fitProblems = fitAvrami();
		isFittable = Arrays.copyOf(initIsFittable, initIsFittable.length);
		switch(fitProblems) {
		case 1:
			return 1;
		default:
			return determineError();
		}
	}
	private int fitAvrami() {
		int numFittable = numFittable(isFittable);
		paramIdxs = new int[numFittable];
		int idx = 0;
		boolean[] originalIsFittable = Arrays.copyOf(isFittable, isFittable.length);
		double newSumSq, oldSumSq;
		newSumSq = sumOfSquares();
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { paramIdxs[idx++] = i; }
		}
		int fitParam;
		do {
			oldSumSq = newSumSq; 
			idx = 0;
			for(int i = 0; i < paramIdxs.length; i++) {
				fitParam = fitParam(paramIdxs[i]);
				switch (fitParam) {
				case 1: return 1;
				}
				isFittable = originalIsFittable;
			}
			newSumSq = sumOfSquares();
		} while(!isConverged(oldSumSq, newSumSq));
		fitParam = fitParams(paramIdxs);
		return fitParam;
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
	private int fitParam(int idx) {
		//System.out.println("\n\nFitting param: " + idx);
		Arrays.fill(isFittable, false);
		isFittable[idx] = true;
		return fit();
	}
	private int fitParams(int[] idx) {
		//System.out.println("\n\nFitting params: " + Arrays.toString(idx));
		Arrays.fill(isFittable, false);
		for(int i = 0; i < idx.length; i++) {
			isFittable[idx[i]] = true;
		}
		return fit();
	}
	private int fit() {
		double[] newParams;
		Vector<double[]> paramsList = new Vector<double[]>();
		paramsList.add(e.getParams());
		//double[] initParams = e.getParams();
		//System.out.println("Initial params: " + Arrays.toString(initParams));
		double oldSumSq, newSumSq;
		newSumSq = sumOfSquares();
		int idx = 0;
		//double initDelta = delta;
		do {
			idx++;
			oldSumSq = newSumSq;
			newParams = step();
			if(newParams == null) {
				return 1;
			}
			paramsList.add(newParams);
			e.setParams(newParams);
			newSumSq = sumOfSquares();
			//System.out.println("Params at step " + idx + ": " + Arrays.toString(newParams));
			if(checkParamsForNaN() ) {
				return 1;
			}
		} while(!isConverged(oldSumSq, newSumSq) && idx < 1000);
		paramsList.add(newParams);
		//System.out.println(paramsList.toString());
		return 0;
	}
	protected int numFittable(boolean[] isFittable) {
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
		double sumSq = sumOfSquares();
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
	private int getDegreesOfFreedom() {
		int numData = 0;
		for(int i = 0; i < data.length; i++) {
			if(!(time[i] < minTime || time[i] > maxTime)) {
				numData++;
			}
		}
		return numData-numFittable;
	}
	private double findPercent(double fraction) {
		if(fraction == 1) { return time[time.length-1 ]; } 
		boolean found = false;
		int idx = 0;
		while(!found) {
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
			// constraint so that the non-lienar routine only fits the
			// region between minTime and maxTime
			for(int i = 0; i < matrixVals.length; i++) {
				if(!(time[i] < minTime || time[i] > maxTime)) {
					matrixVals[i][j] = (fit[i] - originalFit[i])/delta;
				}
			}
			e.decrementParam(delta, j);
		}
		return new Matrix(matrixVals);
	}
	private double[] getResiduals() {
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
		e.setParam(minTime, 1);
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
			return null;
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
	private boolean isConverged(double oldSumSq, double newSumSq) {
		if(Math.abs(oldSumSq - newSumSq) < delta/2) {
			return true;
		}
		if(oldSumSq == newSumSq) { return false; }
		return false;
	}
	public double sumOfSquares() {
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
		sumSq = sumSquares;
		return sumSquares;
	}
	public double[][] getTimeDataFit() {
		double[][] timeDataFit = new double[time.length][3];
		double[] fit = getFit();
		
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
	/*
	 * Setters
	 */
	
	public void setDelta(double delta) { this.delta = delta; }
	public void setIsFittable(boolean[] isFittable) { initIsFittable = isFittable; }
	public void setIsFittable(boolean b, int idx) { initIsFittable[idx] = b; }
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
	public double[] getStdDev() { return param_stdDev; }
	public int getNumDataPoints() {
		sumOfSquares();
		return numDataPointsInFit; 
	}
}
