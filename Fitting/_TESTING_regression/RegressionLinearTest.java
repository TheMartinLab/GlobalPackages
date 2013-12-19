/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regression;

import java.io.File;
import java.io.FileNotFoundException;

import readFile.ReadFile;

import io.StringConverter;
import jama.Matrix;
import _TESTING_regressionModel.Bounds;
import _TESTING_regressionModel.RegressionModel;
import _TESTING_regressionModel.SharpHancockModel;
import _TESTING_regressionModel.Bounds.BoundingCondition;

public class RegressionLinearTest {

	public double[][] getData() { 
		try {
			return ReadFile.read(new File("RegressionLinearTest.data"), 2, 0, "\t");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		RegressionLinearTest rlt = new RegressionLinearTest();
		double[][] data = rlt.getData();
		double[] parameters = new double[] {0.01, 110, 3};
		boolean[] isFittable = new boolean[] {true, false, true};
		Bounds[] b = new Bounds[3];
		Bounds[] xyBounds = new Bounds[2];
		b[0] = new Bounds();
		b[1] = new Bounds();
		b[2] = new Bounds();
		
		xyBounds[0] = new Bounds();
		xyBounds[1] = new Bounds();
		
		b[0].addBound(0, BoundingCondition.GREATER_THAN);
		b[1].addBound(100, BoundingCondition.GREATER_THAN);
		b[1].addBound(120, BoundingCondition.LESS_THAN);
		b[2].addBound(.5, BoundingCondition.GREATER_THAN);
		b[2].addBound(8, BoundingCondition.LESS_THAN);
		RegressionModel sh = new SharpHancockModel(parameters, isFittable, b);

		System.out.println("Initial Parameters: " + StringConverter.arrayToTabString(sh.getAllParameters()));
		
		RegressionLinear rl = new RegressionLinear();
		rl.setModel(sh);
		rl.setData(data);
		
		xyBounds[1].addBound(.0, BoundingCondition.GREATER_THAN_OR_EQUAL_TO);
		xyBounds[1].addBound(.50, BoundingCondition.LESS_THAN_OR_EQUAL_TO);
		rl.setBounds(xyBounds);
		
		rl.doFit();
		Matrix corr = rl.getCorrelation();
		double[][] corrVals = corr.getArray();
		Matrix covar = rl.getCovariance();
		double[][] covarVals = covar.getArray();
		double[] stdev = rl.getStdDev();
		double[] params = rl.getModel().getAllParameters();

		System.out.println("Final Parameters: " + StringConverter.arrayToTabString(params));

		System.out.println("Standard Deviation: " + StringConverter.arrayToTabString(stdev));
		
		System.out.println("Correlation Matrix: ");
		for(int i = 0; i < corrVals.length; i++) {
			System.out.println("\t" + StringConverter.arrayToTabString(corrVals[i]));
		}
		
		System.out.println("Covariance Matrix: ");
		for(int i = 0; i < covarVals.length; i++) {
			System.out.println("\t" + StringConverter.arrayToTabString(covarVals[i]));
		}
		
	}
}
