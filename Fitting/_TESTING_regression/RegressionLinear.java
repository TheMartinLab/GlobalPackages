/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regression;

import jama.Matrix;

public class RegressionLinear extends RegressionXY {


	@Override
	protected Matrix constructX() {
		if(XJMatrixCalc != null) {
			return XJMatrixCalc.constructX(this);
		}
		Matrix X = new Matrix(convertedData.length, 2);
		double x, y;
		for(int i = 0; i < convertedData.length; i++) {
			y = convertedData[i][1];
			x = convertedData[i][0];
			if(convertedBounds[0].checkWithinBounds(x) && convertedBounds[1].checkWithinBounds(y)) {
				X.set(i, 0, 1);
				X.set(i, 1, convertedData[i][0]);
				//System.out.println(i + ": " + convertedData[i][0]);
			} else {
				X.set(i, 0, 0);
				X.set(i, 1, 0);
				//System.out.println(i + ": " + convertedData[i][0] + " (is being set to 0)");
			}
		}
		return X;
	}
	
	@Override
	protected Matrix constructY() {
		Matrix Y = new Matrix(convertedData.length, 1);
		for(int i = 0; i < convertedData.length; i++) {
			Y.set(i, 0, convertedData[i][1]);
		}
		return Y;
	}


	@Override
	public void doFit() throws RegressionException {
		convertData();
		convertBounds();
		calcB();
		calcError(constructX());
		finalCheck();
	}
}
