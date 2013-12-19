/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package bravaisLattice;

import java.util.List;

import chemistry.JAtom;
import geometry.JVector;

public class OldTriclinic implements BravaisLattice {
	private double a, b, c;
	private double alpha, gamma, beta;
	private JVector A, B, C;
	private double S11, S22, S33, S12, S23, S13;
	private List<JAtom> theAtoms;
	public OldTriclinic(double a, double b, double c, double al, double be, double ga, List<JAtom> atoms) {
		theAtoms = atoms;
		this.a = a;
		this.b = b;
		this.c = c;
		alpha = al*Math.PI/180;
		beta = be*Math.PI/180;
		gamma = ga*Math.PI/180;
		S11 = b*b*c*c*Math.pow(Math.sin(alpha), 2);
		S22 = a*a*c*c*Math.pow(Math.sin(beta), 2);
		S33 = a*a*b*b*Math.pow(Math.sin(gamma), 2);
		S12 = a*b*c*c*(Math.cos(alpha) * Math.cos(beta) - Math.cos(gamma));
		S23 = a*a*b*c*(Math.cos(beta) * Math.cos(gamma) - Math.cos(alpha));
		S13 = a*b*b*c*(Math.cos(gamma) * Math.cos(alpha) - Math.cos(beta));
		
		
	}
	@Override
	public double calcV() {
		double cosAlpha = Math.cos(alpha);
		double cosBeta = Math.cos(beta);
		double cosGamma = Math.cos(gamma);
		
		return a*b*c*Math.sqrt(1 - Math.pow(cosAlpha, 2) - Math.pow(cosBeta, 2) - 
				Math.pow(cosGamma, 2) + 2*cosAlpha*cosBeta*cosGamma);
	}

	@Override
	public double calcD(JVector v) {
		double h = v.i;
		double k = v.j;
		double l = v.k;
		double d = 1/Math.sqrt(1/Math.pow(calcV(), 2) * (S11*h*h + S22*k*k + S33*l*l + 2*(S12*h*k + S23*k*l + S13*h*l)));
		
		return d;
	}

	@Override
	public JVector calcQ(JVector v) {
		return null;
	}
	
	/**
	 * 
	 * @param h1
	 * @param k1
	 * @param l1
	 * @param h2
	 * @param k2
	 * @param l2
	 * @return	The angle in DEGREES
	 */
	@Override
	public double calcAngle(JVector v1, JVector v2) {
		double h1 = v1.i;
		double k1 = v1.j;
		double l1 = v1.k;
		double h2 = v2.i;
		double k2 = v2.j;
		double l2 = v2.k;
		
		double phi = Math.acos(calcD(v1) * calcD(v2) / Math.pow(calcV(), 2) *
				(S11*h1*h2 + S22*k1*k2 + S33*l1*l2 + S23*(k1*l1+k2*l1) + S13*(l1*h2+l2*h1) + S12*(h1*k2+h2*k1)));
		return phi*180/Math.PI;
	}
	@Override
	public JAtom[] getAtoms() {
		JAtom[] atoms = new JAtom[theAtoms.size()];
		atoms = theAtoms.toArray(atoms);
		return atoms;
	}
	@Override
	public JVector fractionalToReal(JVector pos) {
		// TODO Auto-generated method stub
		return null;
	}
}