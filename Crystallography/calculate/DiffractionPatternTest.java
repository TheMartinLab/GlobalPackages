/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package calculate;

import geometry.JVector;

import io.ReadATD;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import lattice.BraggComparator;
import lattice.BraggReflection;
import lattice.ReciprocalLattice;

import bravaisLattice.BravaisLattice;
import bravaisLattice.BravaisLatticeFactory;
import chemistry.JAtom;

public class DiffractionPatternTest {

	private BraggReflection[] calc;
	private double maxQ;
	public DiffractionPatternTest() {
		maxQ = 10;
		run();
	}
	public DiffractionPatternTest(double maxQ) {
		this.maxQ = maxQ;
		run();
	}
	private void writeToFile(double[][] image) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream("DiffractionPatternTest.txt");
		PrintStream ps = new PrintStream(fos);
		String s = "";
		for(int i = 0; i < image.length; i++) {
			s = "";
			for(int j = 0; j < image[i].length; j++) {
				s += image[i][j] + "\t";
			}
			ps.println(s);
		}
			
	}
	public void distance(int numTimes, JVector v1, JVector v2) {
		for(int i = 0; i < numTimes; i++) {
			JVector.distance(v1, v2);
		}
	}

	public void run() {
		double a = 10.85;
		double alpha = 90;
		
		Vector<JAtom> atoms = new Vector<JAtom>(4);
		atoms.add(new JAtom(6, new JVector(0, 0, 0)));
		atoms.add(new JAtom(6, new JVector(1./2., 1./2., 0)));
		atoms.add(new JAtom(6, new JVector(1./2., 0, 1./2.)));
		atoms.add(new JAtom(6, new JVector(0, 1./2., 1./2.)));
		
		JAtom[] someAtoms = (new ReadATD(new File("C:\\Users\\eric\\Downloads\\CZX1-cat.atd"))).getAtoms();
		
		BravaisLattice test = BravaisLatticeFactory.getLattice(BravaisLattice.LatticeType.CUBIC_P, 
				new double[] {a}, someAtoms);
		
		System.out.println("test calcVolume(): " + test.calcV());
		JVector x = new JVector(1, 1, 2);
		JVector y = new JVector(1, 1, 0);
		
		System.out.println("test calcD(" + x + "): " + test.calcD(x));

		System.out.println("test calcQ(" + x + "): " + test.calcQ(x));
		System.out.println("test calcAngle(" + x + "\t" + y + "): " + test.calcAngle(x, y));
		

		
		double sampleToDetector = 188.672;
		double wavelength = .13702;
		int min = 900;
		int max = 1100;
		double midX = 1033.321;
		double midY = 1020.208;
		Point p1 = new Point(min, min);
		Point p2 = new Point(max, max);
		Point2D.Double detectorCenter = new Point2D.Double(midX, midY);
		double pixelSize = .2;
		
		ReciprocalLattice test2 = new ReciprocalLattice(test);
		test2.setCalib(midX, midY, pixelSize, sampleToDetector);
		
		
		try {
			//test2.calculateReciprocalLattice(maxQ);
			// TODO figure out why this is an error...
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DiffractionPattern dp = new DiffractionPattern(sampleToDetector, wavelength, test2, detectorCenter,
				p1, p2, pixelSize);
		long timeBefore = System.currentTimeMillis();
		Random r = new Random();
		
		int xRot = r.nextInt(90);
		int yRot = r.nextInt(90);
		int zRot = r.nextInt(90);
		
		int rotVal = 0;
		
		xRot = rotVal;
		yRot = rotVal;
		zRot = rotVal;
		
		
		try {
			writeToFile(dp.calculateImage(xRot, yRot, zRot));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(xRot + " " + yRot + " " + zRot);
		int numCalcs = (int) Math.pow(max-min, 2);
		System.out.println("millis for " + numCalcs + ": " + (System.currentTimeMillis() - timeBefore));
		
		timeBefore = System.currentTimeMillis();
		distance(numCalcs, new JVector(1, 1, 1), new JVector(1, 1, 0));
		System.out.println("millis for " + numCalcs + ": " + (System.currentTimeMillis() - timeBefore));
		
		Vector<BraggReflection> bragg = test2.getReflections();
		calc = new BraggReflection[bragg.size()];
		calc = bragg.toArray(calc);
		Arrays.sort(calc, new BraggComparator());
	}
	public BraggReflection[] getCalc() {
		return calc;
	}
	public static void main(String[] args) throws Exception {
		new DiffractionPatternTest();
	}
}
