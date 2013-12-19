/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import java.util.Arrays;
import java.util.Vector;

import jama.Matrix;
import equations_1d.Avrami;
import equations_1d.Equation1;

public class NonLinearFitting_1 implements Runnable {

	private double[] data;
	private double[] time;
	private Equation1 e;
	private Constraint[] c;
	private boolean[] isFittable;
	private int numFittable = 0;
	private double delta = 1e-6;
	private double minTime, maxTime;
	private double[] param_stdDev;
	private Matrix corr, covar;
	
	public NonLinearFitting_1(double[] data, double[] time, Equation1 e, Constraint[] c, boolean[] isFittable) {
		this.data = data;
		this.time = time;
		this.e = e;
		this.c = c;
		this.isFittable = isFittable;
	}
	public NonLinearFitting_1(double[][] dataAndTime, Equation1 e, Constraint[] c, boolean[] isFittable) {
		data = new double[dataAndTime.length];
		time = new double[dataAndTime.length];
		for(int i = 0; i < dataAndTime.length; i++) {
			data[i] = dataAndTime[i][1];
			time[i] = dataAndTime[i][0];
		}
		this.e = e;
		this.c = c;
		this.isFittable = isFittable;
		minTime = e.getParam(1);
		maxTime = find50Percent();
	}
	public void run() {
		double[] newParams;
		Vector<double[]> paramsList = new Vector<double[]>();
		paramsList.add(e.getParams());
		System.out.println("Initial params: " + Arrays.toString(e.getParams()));
		double oldSumSq, newSumSq;
		newSumSq = sumOfSquares();
		int idx = 0;
		do {
			idx++;
			oldSumSq = newSumSq;
			newParams = step();
			paramsList.add(newParams);
			e.setParams(newParams);
			newSumSq = sumOfSquares();
			System.out.println("Params at step " + idx + ": " + Arrays.toString(newParams));
		} while(!isConverged(oldSumSq, newSumSq));
		determineError();
		paramsList.add(newParams);
		System.out.println(paramsList.toString());
	}
	private void determineError() {
		double var_est = 1/((double) getDegreesOfFreedom()) * sumOfSquares();
		
		Matrix J = calcJ();
		Matrix JT = J.transpose();
		Matrix err = (JT.times(J)).inverse();
		covar = err.times(var_est);
		param_stdDev = new double[numFittable];
		double[][] matrixVals = new double[numFittable][numFittable];
		for(int i = 0; i < numFittable; i++) {
			param_stdDev[i] = Math.sqrt(covar.get(i, i));
			matrixVals[i][i] = param_stdDev[i];
		}
		for(int i = 0; i < numFittable; i++) {
			for(int j = i; j < numFittable; j++) {
				matrixVals[i][j] = covar.get(i, j) / (matrixVals[i][i] * matrixVals[j][j]);
			}
		}
		
		corr = new Matrix(matrixVals);
		
	}
	private int getDegreesOfFreedom() {
		int numData = 0;
		for(int i = 0; i < data.length; i++) {
			if(!(time[i] <= minTime || time[i] >= maxTime)) {
				numData++;
			}
		}
		return numData-numFittable;
	}
	private double find50Percent() {
		double half = .5;
		boolean found = false;
		int idx = 0;
		while(!found) {
			if(data[idx] > half) {
				return time[idx];
			}
			idx++;
		}
		return 0;
	}
	private Matrix calcJ() {
		int numFittable = 0;
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { numFittable++; }
		}
		this.numFittable=numFittable;
		double[][] matrixVals = new double[data.length][numFittable];
		double[] originalFit = getFit();
		// fill out the matrix
		for(int j = 0; j < matrixVals[0].length && isFittable[j]; j++) {
			e.incrementParam(delta, j);
			double[] fit = getFit();
			for(int i = 0; i < matrixVals.length; i++) {
				matrixVals[i][j] = (fit[i] - originalFit[i])/delta;
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
	private double[] step() {
		double[] initialParams = e.getParams();
		Matrix J = calcJ();
		Matrix JT = J.transpose();
		Matrix inv = (JT.times(J)).inverse();
		Matrix JDagger = inv.times(JT);
		Matrix isOne = JDagger.times(J);
		Matrix newParams = JDagger.times(toMatrix(getResiduals()));
		int idx = Math.min(newParams.getRowDimension(), newParams.getColumnDimension());
		double[] betterParams = new double[initialParams.length];
		
		double[][] mat = newParams.getArray();
		idx = 0;
		for(int i = 0; i < initialParams.length; i++, idx++) {
			if(isFittable[i]) {
				betterParams[i] = mat[idx][0]+initialParams[i];
			} else {
				betterParams[i] = initialParams[i];
			}
			System.out.print("param " + i + ": " + betterParams[i]);
		}
		double initSumSq = sumOfSquares();
		e.setParams(betterParams);
		double finalSumSq = sumOfSquares();
		
		System.out.println("init: " + initSumSq + " final: " + finalSumSq + " difference: " + (initSumSq-finalSumSq));
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
		return false;
	}
	private double sumOfSquares() {
		double[] fit = getFit();
		double sumSquares = 0;
		for(int i = 0; i < fit.length; i++) {
			if(!(time[i] < minTime || time[i] > maxTime)) {
				sumSquares += Math.pow(fit[i]-data[i], 2);
			}
		}
		return sumSquares;
	}
	public double[][] getTimeDataFit() {
		double[][] timeDataFit = new double[time.length][3];
		double[] fit = getFit();
		
		for(int i = 0; i < timeDataFit.length; i++) {
			timeDataFit[i][0] = time[i];
			timeDataFit[i][0] = data[i];
			timeDataFit[i][0] = fit[i];
		}
		return timeDataFit;
	}
	public double[] getFit() {
		double[] fit = new double[time.length];
		for(int i = 0; i < fit.length; i++) {
			fit[i] = e.evaluate(time[i]);
		}
		return fit;
	}
	
	/*
	 * Setters
	 */
	
	public void setDelta(double delta) { this.delta = delta; }
	public void setIsFittable(boolean[] isFittable) { this.isFittable = isFittable; }
	public void setIsFittable(boolean b, int idx) { isFittable[idx] = b; }
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
	public Matrix getCovar() { return covar; }
	public Matrix getCorr() { return corr; }
	public double[] getStdDev() { return param_stdDev; }
	public double[] getParams() { return e.getParams(); }
}
