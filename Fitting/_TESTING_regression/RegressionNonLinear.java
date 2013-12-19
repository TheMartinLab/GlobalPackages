/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regression;

import _TESTING_regressionModel.RegressionModel.ParameterOptions;
import io.PrintToFile;
import io.StringConverter;

import jama.Matrix;

public class RegressionNonLinear extends RegressionXY {

	private final static int MAX_CALLS = 150;
	@Override
	public void doFit() throws RegressionException {
		convertData();
		convertBounds();
		//new PrintToFile(getConvertedData(), getFit(), model.getAllParameters(), "nonlinearfittingtest_before.txt");
		double oldSumOfSquares;
		double newSumOfSquares = calcSumOfSquares();
		int convergenceCalls = 0;
		do {
			oldSumOfSquares = newSumOfSquares;
			convertData();
			convertBounds();
			int df = calcDegreesOfFreedom();
			if(df <= minimumNumberOfDegreesOfFreedom)
				throw new RegressionException("There are : " + df + " degrees of freedom.  Need at least 1");
			calcB();
			newSumOfSquares = calcSumOfSquares();
			//System.out.println("old sum sq: " + oldSumOfSquares + " and new sum sq: " + newSumOfSquares);
		} while(!converged(oldSumOfSquares, newSumOfSquares) && convergenceCalls++ < MAX_CALLS);
		calcError(constructX());
	}
	protected double[] calcB() throws RegressionException {
		Matrix J = constructJ();
		Matrix Y = constructY();
 		Matrix JT = J.transpose();
 		Matrix inv = null;
 		try {
 			inv = (JT.times(J)).inverse();
 		} catch(RuntimeException re) {
 			throw new RegressionException(re.getLocalizedMessage());
 		}
		Matrix JDagger = inv.times(JT);
		double[] betterFittableParams = JDagger.times(Y).getColumnPackedCopy();
		betterFittableParams = convertParametersTo(betterFittableParams);
		//System.out.println(StringConverter.arrayToTabString(betterFittableParams));
		double[] allOldParams = model.getAllParameters();
		double[] allNewParams = new double[allOldParams.length];
		int idx_kA = 0, idx_t0 = 1, idx_n = 2, fittableIdx = 0;
		if(model.getIsParameterFittable(ParameterOptions.kA))
			{ allNewParams[idx_kA] = betterFittableParams[fittableIdx++] + allOldParams[idx_kA]; }
		else 
			{ allNewParams[idx_kA] = allOldParams[idx_kA]; }
		if(model.getIsParameterFittable(ParameterOptions.t0))
			{ allNewParams[idx_t0] = betterFittableParams[fittableIdx++] + allOldParams[idx_t0]; }
		else 
			{ allNewParams[idx_t0] = allOldParams[idx_t0]; }
		if(model.getIsParameterFittable(ParameterOptions.n)) 
			{ allNewParams[idx_n] = betterFittableParams[fittableIdx++] + allOldParams[idx_n]; }
		else
			{ allNewParams[idx_n] = allOldParams[idx_n]; }
		model.setParameters(allNewParams);
		//System.out.println(StringConverter.arrayToTabString(allNewParams));
		return allNewParams;
	}
	protected boolean converged(double oldSumSq, double newSumSq) {
		if(newSumSq > 2*oldSumSq) { throw new RegressionException("Fitting routine is not converging."); }
		if(Math.abs(newSumSq - oldSumSq) < .5*delta) {
			return true;
		} 
		return false;
	}
	protected Matrix constructJ() {
		Matrix alternate = checkForAlternateAlgorithm();
		if(alternate != null) { return alternate; }
		convertData();
		convertBounds();
		
		double[] parameters = model.getAllParameters();
		boolean[] fittable = model.getAreParametersFittable();
		int numberOfFittableParameters = model.determineNumberOfFittableParameters();
		int parameterIdx = 0;
		Matrix J = new Matrix(convertedData.length, numberOfFittableParameters);
		//double[] originalFit = getFit();
		double[] newFit;
		for(int i = 0; i < parameters.length; i++) {
			if(fittable[i]) {
				parameters[parameterIdx] += delta;
				model.setParameters(parameters);
				newFit  = getFit();
				for(int j = 0; j < convertedData.length; j++) {
					J.set(parameterIdx, j, (convertedData[i][1] - newFit[j])/delta);
				}
				parameters[parameterIdx++] -= delta;
			}
		}
		return J;
	}
	protected Matrix checkForAlternateAlgorithm() {
		if(this.XJMatrixCalc != null) {
			return XJMatrixCalc.constructJ(this);
		}
		return null;
	}

	@Override
	protected Matrix constructY() {
		/*
		Matrix Y = new Matrix(convertedData.length, 1);
		for(int i = 0; i < convertedData.length; i++) {
			Y.set(i, 0, convertedData[i][1]);
		}
		return Y;*/
		return new Matrix(getResiduals(), 1).transpose();
	}
}
