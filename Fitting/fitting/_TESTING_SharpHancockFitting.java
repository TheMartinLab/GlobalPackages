/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import io.StringConverter;

import jama.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import readFile.ReadFile;

import equations_1d.Equation1;
import equations_1d.SharpHancockEquation;

public class _TESTING_SharpHancockFitting extends Fitting {

	private double[][] timeAndData;
	private int ERROR_CODE = 0;
	public _TESTING_SharpHancockFitting(double[][] timeAndData, Equation1 e) {
		super(e, null);
		this.timeAndData = timeAndData;
		this.e = e;
	}
	public int go() {
		double[][] SH = convertBoundedToSH();
		double[][] X = getX(SH);
		double[][] Y = getY(SH);
		double[] params = regress(X, Y);
		params = paramsToAvramiVals(params);
		e.setParams(params);
		return ERROR_CODE;
	}
	public double[][] convertBoundedToSH() {
 		double[][] SH;
 		Vector<double[]> vals = new Vector<double[]>();
 		double x, y;
		for(int i = 0; i < timeAndData.length; i++) {
			if(withinLimits_X(timeAndData[i][0]) && withinLimits_Y(timeAndData[i][1])) {
				x = convertTime(timeAndData[i][0]);
				y = convertAlpha(timeAndData[i][1]);
				vals.add(new double[] {x, y});
			}
		}
		SH = new double[vals.size()][2];
		SH = vals.toArray(SH);
		return SH;
	}
	public double[][] convertAllToSH() {
 		double[][] SH;
 		Vector<double[]> vals = new Vector<double[]>();
 		double x, y;
		for(int i = 0; i < timeAndData.length; i++) {
			x = convertTime(timeAndData[i][0]);
			y = convertAlpha(timeAndData[i][1]);
			vals.add(new double[] {x, y});
		}
		SH = new double[vals.size()][2];
		SH = vals.toArray(SH);
		return SH;
	}
	public double[] regress(double[][] x, double[][] y) {
		Matrix X = new Matrix(x);
		Matrix XT = X.transpose();
		Matrix Y = new Matrix(y);
		Matrix XTX = XT.times(X);
		Matrix inv = XTX.inverse();
		Matrix invXT = inv.times(XT);
		Matrix invXTY = invXT.times(Y);
		double[] params = invXTY.getColumnPackedCopy();
		double kA = Math.exp(params[0]/params[1]);
		double n = params[1];
		double t0 = e.getParam(1);
		e.setParams(new double[] {kA, t0, n});
		return params;
	}
	
	public double[][] getY(double[][] data) {
		double[][] Y = new double[data.length][1];
		for(int i = 0; i < data.length; i++) {
			Y[i][0] = data[i][1];
		}
		return  Y;
	}
	public double[][] getX(double[][] data) {
		double[][] X = new double[data.length][2];
		for(int i = 0; i < data.length; i++) {
			X[i][0] = 1;
			X[i][1] = data[i][0];
		}
		return  X;
	}
	/**
	 * 
	 * @param min Minimum value on the original data set (i.e. not a linearized form of alpha)
	 * @param max Maximum value in the original data set (i.e. not a linearized form of alpha)
	 */
	public void setLimits_X(double min, double max) {
		minX = min;
		maxX = max;
		limitX = true;
	}
	public void setLimits_Y(double min, double max) {
		minY = min;
		maxY = max;
		limitY = true;
	}
	public double[] paramsToAvramiVals(double[] params) {
		double[] vals = new double[3];
		vals[2] = params[1];
		vals[0] = Math.exp(params[0]/params[1]);
		vals[1] = e.getParam(1);
		return vals;
	}
	public void printTimeDataFit() {
		File out = new File("test.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream ps = new PrintStream(fos);
		ps.println("time\talpha\tSH time\tSH alpha\tfit");
		double[][] SHAll = convertAllToSH();
		double[][] SHBounded = convertBoundedToSH();
		int max = Math.max(timeAndData.length, SHAll.length);
		for(int i = 0; i < max; i++) {
			if(i < timeAndData.length) {
				ps.print(StringConverter.arrayToTabString(timeAndData[i]));
			} else { ps.println("\t\t"); }
			if(i < SHAll.length) {
				ps.print(StringConverter.arrayToTabString(SHAll[i]));
			}
			if(i < SHBounded.length) {
				ps.print(SHBounded[i][0] + "\t");
				ps.print(e.evaluate(SHBounded[i][0]));
			}
			ps.println();
		}
	}

	public double convertAlpha(double alpha) {
		double val = Math.log(-1 * Math.log(1-alpha));
		return val;
	}
	public double convertTime(double time) {
		double val = Math.log(time - e.getParam(1));
		return val;
	}
	
	public static void main(String[] args) {
		// GET SOME DATA
		File f = new File("SharpHancockData.txt");
		double[][] timeAndData = null;
		try {
			timeAndData = ReadFile.read(f, 2, 0, "\t");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// INITIALIZE THE FITTING EQUATION
		double t0 = 10;
		double n = 3;
		double kA = Math.pow(30000, -1./3.);
		Equation1 e = new SharpHancockEquation(new double[] {kA, t0, n});
		
		// INITIALIZE THE FITTING OBJECT
		SharpHancockFitting shf = new SharpHancockFitting(timeAndData, e);
		shf.setLimits_X(10, timeAndData[timeAndData.length-1][0]);
		shf.setLimits_Y(0, 0.5);
		double[][] SH = shf.convertBoundedToSH();
		double[][] X = shf.getX(SH);
		double[][] Y = shf.getY(SH);
		double[] params = shf.regress(X, Y);
		System.out.println(StringConverter.arrayToTabString(params));
		params = shf.paramsToAvramiVals(params);
		System.out.println(StringConverter.arrayToTabString(params));
		shf.printTimeDataFit();
	}
	@Override
	public double getSumSq() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getNumDataPoints() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public double[][] getTimeDataFit() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public double[] getStdDev() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public double sumOfSquares() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean[] getIsFittable() {
		// TODO Auto-generated method stub
		return null;
	}
}
