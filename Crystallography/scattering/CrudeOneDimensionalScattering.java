/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package scattering;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;

import readFile.ReadFile;


public class CrudeOneDimensionalScattering {

	private double[][] newScatteringPattern, oldScatteringPattern;
	private HashMap<Double, Integer> map;
	private double averageCrystalliteSize = 5;
	private double wavelength = .13702;
	private double stepSize;
	private DecimalFormat twoDForm = new DecimalFormat("#.####");
	private double minQ, maxQ;
	private int numSigma = 8;
	private double shapeFactor = 1;
	
	private void initScatteringPattern() {
		int numDataPoints = (int) Math.rint((maxQ - minQ) / stepSize);
		newScatteringPattern = new double[numDataPoints][2];
		map = new HashMap<Double, Integer>(numDataPoints);
		double val;
		for(int i = 0; i < numDataPoints; i++) {
			val = Double.valueOf(twoDForm.format(minQ + i*stepSize));
			newScatteringPattern[i][0] = val;
			map.put(val, i);
		}
	}
	private double calcSigma(double Q) {
		double theta = Math.asin(Q*wavelength/4/Math.PI);
		double sigma = shapeFactor*wavelength/averageCrystalliteSize/Math.cos(theta);
		return sigma/2.;
	}
	private double[][] applyGaussian(double[] vals) {
		double I = vals[1];// / vals[2];
		double Q = roundToStep(vals[0]);
		double sigma = calcSigma(Q);
		double min = roundToStep(Q - numSigma*sigma);
		double max = roundToStep(Q + numSigma*sigma);
		int numSteps = (int) Math.rint((max - min) / stepSize);
		double[][] gaussian = new double[numSteps][2];
		for(int i = 0; i < numSteps; i++) {
			gaussian[i][0] = roundToStep(min + i*stepSize);
			gaussian[i][1] = calcGaussian(sigma, gaussian[i][0], Q, I);
		}
		return gaussian;
	}
	private double calcGaussian(double sigma, double pos, double center, double I) {
		return I*Math.exp(-1*Math.pow(pos-center, 2)/(2*Math.pow(2*sigma, 2)));
	}
	private double roundToStep(double val) {
		val /= stepSize;
		val = Math.rint(val);
		val *= stepSize;
		return Double.valueOf(twoDForm.format(val));
	}
	private void calcBroadenedScattering() {
		for(int i = 0; i < oldScatteringPattern.length; i++) {
			addNewScattering(applyGaussian(oldScatteringPattern[i]));
		}
	}
	private void addNewScattering(double[][] gaussian) {
		int idx;
		for(int i = 0; i < gaussian.length; i++) {
			if(gaussian[i][0] > maxQ) { continue; }
			if(gaussian[i][0] < minQ) { continue; }
			if(map.get(gaussian[i][0]) == null) { continue; }
			idx = map.get(gaussian[i][0]);
			newScatteringPattern[idx][1] += gaussian[i][1];
		}
	}
	private void readFile(File aFile, int numColumns) {
		try {
			oldScatteringPattern = readFile.ReadFile.read(aFile, ReadFile.UNKNOWN_NUM_COLUMNS, ReadFile.UNKNOWN_NUM_ROWS, 2, "\t");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private double[] getMaxQAndMinQ(double[][] data) {
		double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
		for(int i = 0; i < data.length; i++) {
			if(min > data[i][0]) { min = data[i][0]; }
			if(max < data[i][0]) { max = data[i][0]; }
		}
		return new double[] {min, max};
	}
	public static void main(String[] args) {
		CrudeOneDimensionalScattering oneD = new CrudeOneDimensionalScattering();
		oneD.stepSize = .01;
		File theFile = new File("C:\\Users\\JDM_5\\workspace\\JNI\\tetragonalParacrystallineTest_1D_256_4+(100).xray");
		oneD.readFile(theFile, 3);
		for(int i = 0; i < oneD.oldScatteringPattern.length; i++) {
			for(int j = 0; j < oneD.oldScatteringPattern[i].length; j++) {
				System.out.print(oneD.oldScatteringPattern[i][j] + "\t");
			}
			System.out.println();
		}
		
		double[] minAndMax = oneD.getMaxQAndMinQ(oneD.oldScatteringPattern);
		System.out.println("Min and Max:");
		for(int i = 0; i < minAndMax.length; i++) {
			System.out.println(minAndMax[i]);
		}
		oneD.minQ = minAndMax[0];
		oneD.maxQ = minAndMax[1];
		oneD.initScatteringPattern();
		oneD.calcBroadenedScattering();
		System.out.println("\n\n\n\n\n");
		double Q, I;
		for(int i = 0; i < oneD.newScatteringPattern.length; i++) {
			I = oneD.newScatteringPattern[i][1];
			System.out.println(I);
		}
	}
}
