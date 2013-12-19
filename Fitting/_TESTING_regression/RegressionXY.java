/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regression;

import io.StringConverter;
import _TESTING_regressionModel.Bounds;
import _TESTING_regressionModel.XJMatrixPlugin;
import jama.Matrix;
/**
 * The data is expected to be in [x, y] columnar format
 * @author DHD
 *
 */
public abstract class RegressionXY extends Regression {

	protected XJMatrixPlugin XJMatrixCalc;
	/* ABSTRACT METHODS */
	protected abstract Matrix constructY();
	/* CONCRETE METHODS */
	protected double[] convertParametersTo(double[] parameters) {
		if(XJMatrixCalc!= null) {
			return XJMatrixCalc.convertParametersTo(this, parameters);
		}
		return model.convertParametersTo(parameters);
	}
	public void setXJMatrixPlugin(XJMatrixPlugin plugin) { XJMatrixCalc = plugin; }
	protected int determineNumberOfFittableDataPoints() {
		int numPoints = 0;
		for(int i = 0; i < convertedData.length; i++) {
			if(convertedBounds[0].checkWithinBounds(convertedData[i][0]) &&
					convertedBounds[1].checkWithinBounds(convertedData[i][1])) {
				numPoints++;
			}
		}
		return numPoints;
	}
	protected void calcError(Matrix J) {
		double sumOfSquares = calcSumOfSquares();
		double degreesOfFreedom = calcDegreesOfFreedom();
		double varianceEstimate = 1 / degreesOfFreedom * sumOfSquares;
		int numFittableParams = model.determineNumberOfFittableParameters();
		
		Matrix JT = J.transpose();
		Matrix err = (JT.times(J)).inverse();
		
		covariance = err.times(varianceEstimate);
		correlation = (Matrix) covariance.clone();
		double[][] corrVals = correlation.getArray();
		double[] parameterStandardDeviation = new double[numFittableParams];
		// set the diagonal of the correlation matrix
		for(int i = 0; i < numFittableParams; i++) {
			for(int j = 0; j < numFittableParams; j++) {
				if(i == j) {
					parameterStandardDeviation[i] = Math.sqrt(covariance.get(i, i));
					corrVals[i][i] = 1.; 
				}
			}
		}
		// update the Regression model with the new parameter standard deviations
		model.setStandardDeviation(parameterStandardDeviation);
		
		// set the off-diagonals of the correlation matrix
		for(int i = 0; i < numFittableParams; i++) {
			for(int j = 0; j < numFittableParams; j++) {
				if(i != j) {
					corrVals[i][j] = covariance.get(i, j) / parameterStandardDeviation[i] / parameterStandardDeviation[j]; 
				}
			}
		}
	}


	protected double[] calcB() throws RegressionException {
		Matrix X = constructX();
		Matrix Y = constructY();
 		Matrix XT = X.transpose();
		Matrix inv = (XT.times(X)).inverse();
		Matrix XDagger = inv.times(XT);
		double[] betterFittableParams = XDagger.times(Y).getColumnPackedCopy();
		betterFittableParams = convertParametersTo(betterFittableParams);
		//System.out.println(StringConverter.arrayToTabString(betterFittableParams));
		double[] allOldParams = model.getAllParameters();
		double[] allNewParams = new double[allOldParams.length];
		boolean[] isFittable = model.getAreParametersFittable();
		int fittableIdx = 0;
		for(int i = 0; i < allOldParams.length; i++) {
			if(isFittable[i]) {
				allNewParams[i] = betterFittableParams[fittableIdx++];
			} else {
				allNewParams[i] = allOldParams[i];
			}
		}
		model.setParameters(allNewParams);
		return allNewParams;
	}
	
	@Override
	protected int calcDegreesOfFreedom() {
		int df = 0;
		for(int i = 0; i < convertedData.length; i++) {
			if(convertedBounds[0].checkWithinBounds(convertedData[i][0]) &&
					convertedBounds[1].checkWithinBounds(convertedData[i][1])) {
				df++;
			}
		}
		int numFittableParams = model.determineNumberOfFittableParameters();
		return df - numFittableParams;
	}

	@Override
	public double[] getFit() {
		double[] fit = new double[convertedData.length];
		for(int i = 0; i < fit.length; i++) {
			if(convertedBounds[0].checkWithinBounds(convertedData[i][0]) && 
					convertedBounds[1].checkWithinBounds(convertedData[i][1]))
				fit[i] = model.evaluate(convertedData[i][0]);
		}
		return fit;
	}
	
	@Override
	protected double[] calcResiduals() {
		double[] fit = getFit();
		double[] resid = new double[fit.length];
		for(int i = 0; i < resid.length; i++) {
			if(convertedBounds[0].checkWithinBounds(convertedData[i][0]) && 
					convertedBounds[1].checkWithinBounds(convertedData[i][1]))
			resid[i] = convertedData[i][1] - fit[i];
		}
		return resid;
	}

	@Override
	protected double calcSumOfSquares() {
		double[] residuals = calcResiduals();
		double sumSq=0;
		for(int i = 0; i < residuals.length; i++) {
			if(convertedBounds[0].checkWithinBounds(convertedData[i][0]) && 
					convertedBounds[1].checkWithinBounds(convertedData[i][1]))
				sumSq += Math.pow(residuals[i], 2);
		}
		return sumSq;
	}
	@Override
	protected void convertData() {
		convertedData = model.convertData(initialData);
	}
	@Override
	protected void convertBounds() {
		convertedBounds = new Bounds[initialBounds.length];
		convertedBounds[0] = model.convertXBounds(initialBounds[0]);
		convertedBounds[1] = model.convertYBounds(initialBounds[1]);
	}	
	
	protected Matrix constructJ() {
		return constructX();
	}	
	protected Matrix constructX() {
		return constructJ();
	}
}
