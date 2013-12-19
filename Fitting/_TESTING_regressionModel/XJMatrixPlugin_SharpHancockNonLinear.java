/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

import _TESTING_regression.RegressionXY;
import _TESTING_regressionModel.RegressionModel.ParameterOptions;
import jama.Matrix;

public class XJMatrixPlugin_SharpHancockNonLinear extends XJMatrixPlugin {

	public Matrix constructX(RegressionXY reg) {
		return constructJ(reg);
	}

	@Override
	public Matrix constructJ(RegressionXY reg) {
		double[][] data = reg.getInitialData();
		RegressionModel model = reg.getModel();
		int numFittable = model.determineNumberOfFittableParameters();
		int paramIdx;
		Matrix J = new Matrix(data.length, numFittable);
		double partialN, partialt0, partialkA, t;
		double n = model.getSpecificParameter(ParameterOptions.n);
		double kA = model.getSpecificParameter(ParameterOptions.kA);
		double t0 = model.getSpecificParameter(ParameterOptions.t0);
		Bounds[] bounds = reg.getConvertedBounds();
		double[][] convertedData = reg.getConvertedData();
		for(int i = 0; i < data.length; i++) {
			t = data[i][0];
			if(bounds[0].checkWithinBounds(convertedData[i][0]) && bounds[1].checkWithinBounds(convertedData[i][1])) {
				partialN = Math.log((t - t0)) + Math.log(kA) + reg.getDelta(); 
				partialt0 = -n / (t - t0) + reg.getDelta();
				partialkA = n / kA + reg.getDelta();
			}
			else { 
				partialN = 0;
				partialt0 = 0;
				partialkA = 0;
			}
			paramIdx = 0;
			if(model.getIsParameterFittable(ParameterOptions.kA)) {
				J.set(i, paramIdx++, partialkA);
			}
			if(model.getIsParameterFittable(ParameterOptions.t0)) {
				J.set(i, paramIdx++, partialt0);
			}
			if(model.getIsParameterFittable(ParameterOptions.n)) {
				J.set(i, paramIdx++, partialN);
			}
		}
		return J;
	}

	@Override
	public double[] convertParametersTo(RegressionXY reg, double[] parameters) {
		return parameters;
	}
}