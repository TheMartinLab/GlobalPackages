/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package spotPicking;

import geometry.JVector;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;

import readFile.ReadFile;

import calculate.DiffractionPatternTest;

import lattice.BraggReflection;

public class BraggCompareTest {

	public BraggCompareTest() {
		run();
	}
	private double[][] readFile(File f) throws FileNotFoundException {
		return ReadFile.read(f, 0, "\t");
	}
	public void run() {
		File f = new File("single_xtal_testQandPhiandI.txt");
		double[][] meas = null;
		
		try {
			meas = readFile(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BraggReflection[] calc = new DiffractionPatternTest(15).getCalc();
		
		double sampleToDetector = 188.672;
		double wavelength = .13702;
		double x0 = 1033.321;
		double y0 = 1020.208;
		double pixelSize = .2;
		JVector rotationAxis = new JVector(1, 0, 0);
		double rotationAmount = 5;
		
		BraggCompare bc = new BraggCompare(calc, meas, 0.05, x0, y0, sampleToDetector, 
				wavelength, pixelSize, rotationAxis, rotationAmount);
		
	}
	public static void main(String[] args) {
		new BraggCompareTest();
	}
}
