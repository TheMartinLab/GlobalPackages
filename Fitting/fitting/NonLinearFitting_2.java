/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import jama.Matrix;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import equations_1d.Avrami;
import equations_1d.Equation1;

public class NonLinearFitting_2 {

	private double[] data;
	private double[] time;
	private Equation1 e;
	private Constraint[] c;
	private boolean[] isFittable;
	private double delta = 1e-6;
	private double minTime, maxTime;
	private double[] param_stdDev;
	private Matrix corr, covar;
	
	/* constants for switch blocks */
	private final static int DIVIDE = 0;
	private final static int MULTIPLY = 1;
	private final static int ADD = 2;
	private final static int SUBTRACT = 3;
	/* end constants for switch blocks */
	
	
	public NonLinearFitting_2(double[] data, double[] time, Equation1 e, Constraint[] c, boolean[] isFittable) {
		this.data = data;
		this.time = time;
		this.e = e;
		this.c = c;
		this.isFittable = isFittable;
	}
	public NonLinearFitting_2(double[][] dataAndTime, Equation1 e, Constraint[] c, boolean[] isFittable) {
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
	/* Non Linear Fitting Algorithm Control */
	
	public void run() {
		boolean converged;
		boolean[] isFittable = this.isFittable;
		converged = fit(isFittable);
		System.out.println("Final parameters: " + Arrays.toString(e.getParams()));
		determineError();
	}
	
	private boolean fit(boolean[][] fittableSets) {
		boolean[] converged = new boolean[fittableSets.length];
		boolean allConverged = false;
		for(int i = 0; i < fittableSets.length; i++) {
			converged[i] = fit(fittableSets[i]);
			allConverged = converged[i];
		}
		
		int numFittable = numFittable(fittableSets[0]);
		if(!allConverged) {
			if(numFittable > 1) {
				fit(getReducedParameterSets(fittableSets));
			} else {
				for(int i = 0; i < converged.length && converged[i]; i++) {
					significantlyChangeParameters(fittableSets[i]);
					fit(fittableSets);	
				}
			}
		} 
		return true;
	}
	private boolean fit(boolean[] fittableParameters) {
		boolean converged = false;
		/* fitting algorithm */
		
		/* end fitting algorithm */
		
		if(!converged) {
			if(numFittable(fittableParameters) == 0) {
				return false;
			}
			fit(getReducedParameterSets(fittableParameters));
			fit(fittableParameters);
		}
		return true;
	}


	public void significantlyChangeParameters(boolean[] fittableSets) {
		double initSumSq = sumOfSquares();
		double[] initParams = e.getParams();
		double newParam;
		double newSumSq;
		boolean modifySuccess = false;
		int operation = DIVIDE;
		boolean increment = true;
		double deltaMultiplier = 1e3;
		while(!modifySuccess) {
			for(int i = 0; i < fittableSets.length; i++) {
				if(fittableSets[i]) {
					newParam = initParams[i];
					switch(operation) {
					case DIVIDE:
						newParam /= (deltaMultiplier*delta);
						break;
					case MULTIPLY:
						newParam *= (deltaMultiplier*delta);
						break;
					case ADD:
						newParam += (deltaMultiplier*delta);
						break;
					case SUBTRACT:
						newParam -= (deltaMultiplier*delta);
						break;
					}
					e.setParam(newParam, i);
					i = fittableSets.length;
				}
			}
			if(increment) {
				operation++;
				if(operation == 3) { increment = false; }
			} else {
				operation--;
				if(operation == 0) { 
					increment = true;
					deltaMultiplier *= 10;
				}
			}
			
			newSumSq = sumOfSquares();
			if(newSumSq < initSumSq) {
				modifySuccess = true;
			}
		}
	}
	/* END Non Linear Fitting Algorithm Control */
	
	/* FITTING METHODS */
	private int numFittable(boolean[] isFittable) {
		int numFittable = 0;
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { numFittable++; }
		}
		return numFittable;
	}
	public double[] getFit() {
		double[] fit = new double[time.length];
		for(int i = 0; i < fit.length; i++) {
			fit[i] = e.evaluate(time[i]);
		}
		return fit;
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
		int numFittable = calcNumFittable(isFittable);
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
	/* END FITTING METHODS */
	
	/* ERROR DETERMINATION */
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
	/* END ERROR DETERMINATION */
	
	/* DECISION MAKING: WHICH PARAMETERS TO USE */
	/**
	 * Calls getReducedParameterSets(boolean[]) for each of the isFittable[][] boolean sets: if 3 parameters are true in 
	 * each of the isFittable[] sets then the return value will be the 3 sets of two parameters that are true.  i.e. 
	 * {{T T T F}, {T T F T}, {T F T T}, {F T T T} } are the parameter options, the return value will be 
	 * { {T T F F}, {T F T F}, {T F F T}, {F T T F}, {F T F T}, {F F T T} } 
	 * @param isFittable The parameter set to reduce
	 * @return the sets of parameters to use for the non linear fitting 
	 */
	private boolean[][] getReducedParameterSets(boolean[][] isFittable) {
		boolean[][] tempVals = null;
		Vector<boolean[]> allSets = new Vector<boolean[]>(); 
		for(int i = 0; i < isFittable.length; i++) {
			tempVals = getReducedParameterSets(isFittable[i]);
			for(int j = 0; j < tempVals.length; j++) {
				allSets.add(tempVals[j]);
			}
		}
		
		boolean[] cur_i, cur_j;
		
		boolean found;
		do {
			found = false;
			for(int i = 0; i < allSets.size(); i++) {
				cur_i = allSets.get(i);
				for(int j = i+1; j < allSets.size(); j++) {
					if(i==j) { continue; }
					cur_j = allSets.get(j);
					if(compareParameterSets(cur_j, cur_i)) {
						allSets.remove(j);
						found = true;
						i = j = allSets.size();
					}
				}
				
			}
		} while(found);
		
		tempVals = new boolean[allSets.size()][tempVals[0].length];
		
		tempVals = allSets.toArray(tempVals);
		
		return tempVals;
	}
	/**
	 * Check two arrays of boolean values to determine if they're identical.  i.e. T T F F and T T F F are identical, so the 
	 * return value is true.  T T F F and T F T F are not identical, so the return value is false. 
	 * To be used in conjunction with getReducedParameterSets(boolean[]).
	 * @param b1 an array of boolean values
	 * @param b2 another array of boolean values.
	 * @return false if the two parameter sets are not the same length or are the same length but do not contain the same
	 * boolean values. true if the parameter sets are the same length and contain the same order of boolean values.
	 */
	private boolean compareParameterSets(boolean[] b1, boolean[] b2) {
		if(b1.length != b2.length) { return false; }
		int identical = 0;
		for(int i = 0; i < b1.length; i++) {
			if(b1[i] == b2[i]) {
				identical++;
			}
		}
		if(identical == b1.length) {
			return true;
		}
		return false;
	}
	/**
	 * if 3 parameters are true in the isFittable[] set then the return value will be the 3 sets of two parameters that are true.  i.e. 
	 * {T T T F} are the parameter options, the return value will be 
	 * { {T T F F}, {T F T F}, {F T T F} } 
	 * @param isFittable The parameter set to reduce
	 * @return the sets of parameters to use for the non linear fitting 
	 */
	private boolean[][] getReducedParameterSets(boolean[] isFittable) {
		
		int numFittable = 0;
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) {
				numFittable++;
			}
		}
		
		int startIdx = 0;
		boolean[][] reducedParameterFittable = new boolean[numFittable][isFittable.length];
		for(int i = 0; i < numFittable; i++) {
			for(int j = startIdx; j < reducedParameterFittable[0].length; j++) {
				reducedParameterFittable[i][j] = isFittable[j];
			}
		}
		for(int i = 0; i < reducedParameterFittable.length; i++) {
			for(int j = startIdx; j < isFittable.length; j++) {
				if(reducedParameterFittable[i][j]) {
					reducedParameterFittable[i][j] = !reducedParameterFittable[i][j];
					do {
						startIdx++;
					} while(startIdx < isFittable.length && !isFittable[startIdx]);
					j = isFittable.length;
				}
			}
		}
		
		return reducedParameterFittable;
	}
	
	/* END DECISION MAKING: WHICH PARAMETERS TO USE */
	
	
	
}
