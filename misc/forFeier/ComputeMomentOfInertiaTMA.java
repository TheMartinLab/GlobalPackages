/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package forFeier;

import chemistry.JAtom;
import geometry.JVector;

public class ComputeMomentOfInertiaTMA {

	private double N = 14.0067;
	private double C = 12.01;
	private double H = 1.008;
	private double D = 2.0136;
	private double latticeConstant = 10.5881;
	public enum templateType { d0, d1, d9, d10 };
	private JVector[] getFractionalCoords() {
		JVector N = new JVector(0.0726, 0.0726, 0.0726);
		JVector Ha = new JVector(0.125, 0.125, 0.125);
		JVector C1 = new JVector(-0.0739, 0.1028, 0.1028);
		JVector H11 = new JVector(-0.0911, 0.0832, 0.1947);
		JVector H12 = new JVector(-0.0911, 0.1947, 0.0832);
		JVector H13 = new JVector(-0.1290, 0.0477, 0.0477);
		JVector C2 = new JVector(0.1028, 0.1028, -0.0739);
		JVector H21 = new JVector(0.0832, 0.1947, -0.0911);
		JVector H22 = new JVector(0.0477, 0.0477, -0.1290);
		JVector H23 = new JVector(0.1947, 0.0832, -0.0911);
		JVector C3 = new JVector(0.1028, -0.0739, 0.1028);
		JVector H31 = new JVector(0.1947, -0.0911, 0.0832);
		JVector H32 = new JVector(0.0477, -0.1290, 0.0477);
		JVector H33 = new JVector(0.0832, -0.0911, 0.1947);
		
		return new JVector[] {N, Ha, C1, H11, H12, H13, C2, H21, H22, H23, C3, H31, H32, H33};
	}
	private JVector[] getCartesianCoords() {
		JVector[] coords = getFractionalCoords();
		for(int i = 0; i < coords.length; i++) {
			coords[i] = JVector.multiply(coords[i], latticeConstant);
		}
		return coords;
	}
	private double[] getMass(templateType type) {
		switch(type) {
		case d0:
			return new double[] {N, H, C, H, H, H, C, H, H, H, C, H, H, H};
		case d1:
			return new double[] {N, D, C, H, H, H, C, H, H, H, C, H, H, H};
		case d9:
			return new double[] {N, H, C, D, D, D, C, D, D, D, C, D, D, D};
		case d10:
			return new double[] {N, D, C, D, D, D, C, D, D, D, C, D, D, D};
		default:
			throw new RuntimeException("You must select one of the types.");
		}
	}
	private JVector[] getTwoPointsOnRotationAxis() {
		JVector point1 = new JVector();
		JVector point2 = new JVector(1, -1, 1);
		return new JVector[] {point1, point2};
	}
	private String[] getAtomLabels(templateType type) {
		String N = "N";
		String H = "H";
		String C = "C";
		String D = "D";
		switch(type) {
		case d0:
			return new String[] {N, H + "a", 
					C + "1", H + "11", H + "12", H + "13", 
					C + "2", H + "21", H + "22", H + "23", 
					C + "3", H + "31", H + "32", H + "33"};
		case d1:
			return new String[] {N, D + "a", 
					C + "1", H + "11", H + "12", H + "13", 
					C + "2", H + "21", H + "22", H + "23", 
					C + "3", H + "31", H + "32", H + "33"};
		case d9:
			return new String[] {N, H + "a", 
					C + "1", D + "11", D + "12", D + "13", 
					C + "2", D + "21", D + "22", D + "23", 
					C + "3", D + "31", D + "32", D + "33"};
		case d10:
			return new String[] {N, D + "a", 
					C + "1", D + "11", D + "12", D + "13", 
					C + "2", D + "21", D + "22", D + "23", 
					C + "3", D + "31", D + "32", D + "33"};
		default:
			throw new RuntimeException("You must select one of the types.");
		}
	}
	public static void main(String[] args) {
		templateType d0 = templateType.d0;
		templateType d10 = templateType.d10;
		ComputeMomentOfInertiaTMA tma = new ComputeMomentOfInertiaTMA();
		JVector[] coords = tma.getCartesianCoords();
		double[] masses = tma.getMass(d0);
		JVector[] twoPointsOnAxis = tma.getTwoPointsOnRotationAxis();
		String[] labels = tma.getAtomLabels(d0);
		double I = 0, d0I = 0, d10I = 0;
		double d = 0;
		double numerator, denominator;
		JVector shift = JVector.multiply(new JVector(1, 1, 1), -.055);
		shift = new JVector(0, 0, 0);
		System.out.println(d0.toString() + ":");
		for(int i = 0; i < coords.length; i++) {
			coords[i] = JVector.add(coords[i], shift);
			numerator = JVector.cross(JVector.subtract(coords[i], twoPointsOnAxis[0]), JVector.subtract(coords[i], twoPointsOnAxis[1])).length();
			denominator = JVector.subtract(twoPointsOnAxis[1], twoPointsOnAxis[0]).length();
			d = numerator/denominator;
			I = d*d*masses[i];
			d0I += I;
			System.out.println(labels[i] + ": " + I);
		}
		masses = tma.getMass(d10);
		coords = tma.getCartesianCoords();
		System.out.println(d10.toString() + ":");
		for(int i = 0; i < coords.length; i++) {
			coords[i] = JVector.add(coords[i], shift);
			numerator = JVector.cross(JVector.subtract(coords[i], twoPointsOnAxis[0]), JVector.subtract(coords[i], twoPointsOnAxis[1])).length();
			denominator = JVector.subtract(twoPointsOnAxis[1], twoPointsOnAxis[0]).length();
			d = numerator/denominator;
			I = d*d*masses[i];
			d10I += I;
			System.out.println(labels[i] + ": " + I);
		}
		System.out.println("Moment of Inertia for template type: " + d0.toString() + " is: " + d0I);
		System.out.println("Moment of Inertia for template type: " + d10.toString() + " is: " + d10I);
		System.out.println("Moment of Inertia ratio: " + (d10I/d0I));
		System.out.println(JVector.multiply(shift, 10.8).length());
	}
}
