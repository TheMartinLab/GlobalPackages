/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regression;

import java.io.File;
import java.io.FileNotFoundException;

import readFile.ReadFile;

import io.PrintToFile;
import io.StringConverter;
import jama.Matrix;
import _TESTING_regressionModel.Bounds;
import _TESTING_regressionModel.RegressionModel;
import _TESTING_regressionModel.SharpHancockModel;
import _TESTING_regressionModel.Bounds.BoundingCondition;
import _TESTING_regressionModel.XJMatrixPlugin_SharpHancockNonLinear;

public class RegressionNonLinearTest {

	public double[][] getData() { 
		try {
			return ReadFile.read(new File("RegressionLinearTest.data"), 2, 0, "\t");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		RegressionNonLinearTest rlt = new RegressionNonLinearTest();
		double[][] data = rlt.getData();
		double[] parameters = new double[] {0.001, 110, 3};
		boolean[] isFittable = new boolean[] {true, true, false};
		Bounds[] b = new Bounds[3];
		Bounds[] xyBounds = new Bounds[2];
		b[0] = new Bounds();
		b[1] = new Bounds();
		b[2] = new Bounds();
		
		xyBounds[0] = new Bounds();
		xyBounds[1] = new Bounds();
		
		b[0].addBound(.0001, BoundingCondition.GREATER_THAN);
		b[0].addBound(.1, BoundingCondition.LESS_THAN);
		b[1].addBound(10, BoundingCondition.GREATER_THAN);
		b[1].addBound(120, BoundingCondition.LESS_THAN);
		b[2].addBound(.5, BoundingCondition.GREATER_THAN);
		b[2].addBound(8, BoundingCondition.LESS_THAN);
		RegressionModel sh = new SharpHancockModel(parameters, isFittable, b);

		System.out.println("Initial Parameters: " + StringConverter.arrayToTabString(sh.getAllParameters()));
		
		RegressionXY r = new RegressionNonLinear();
		r.setXJMatrixPlugin(new XJMatrixPlugin_SharpHancockNonLinear());
		
		r.setModel(sh);
		r.setData(data);
		
		xyBounds[1].addBound(0.0, BoundingCondition.GREATER_THAN);
		xyBounds[1].addBound(.50, BoundingCondition.LESS_THAN_OR_EQUAL_TO);
		r.setBounds(xyBounds);


		r.doFit();
		Matrix corr = r.getCorrelation();
		double[][] corrVals = corr.getArray();
		Matrix covar = r.getCovariance();
		double[][] covarVals = covar.getArray();
		double[] stdev = r.getStdDev();
		double[] params = r.getModel().getAllParameters();

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

		new PrintToFile(r.getConvertedData(), r.getFit(), params, "nonlinearfittingtest_after.txt");
		
	}
}
