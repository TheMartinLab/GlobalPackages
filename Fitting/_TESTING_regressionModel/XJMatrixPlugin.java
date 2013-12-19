/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

import _TESTING_regression.RegressionXY;
import jama.Matrix;

public abstract class XJMatrixPlugin {
	public abstract Matrix constructX(RegressionXY reg);
	public abstract Matrix constructJ(RegressionXY reg);
	public abstract double[] convertParametersTo(RegressionXY reg, double[] parameters);
}
