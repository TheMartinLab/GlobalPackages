/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package bravaisLattice;

import geometry.JVector;

import java.util.List;

import chemistry.JAtom;

public class Cubic implements BravaisLattice {
	/** Lattice parameter a */
	private double a;
	/** Reciprocal lattice parameter a* */
	private double aI;
	private JAtom[] theAtoms;
	
	/**
	 * @param atoms List of the atoms
	 * @param a Size of the unit cell
	 */
	public Cubic(List<JAtom> atoms, double a) {
		this.a = a;
		aI = 1./a;
		theAtoms = new JAtom[atoms.size()];
		theAtoms = atoms.toArray(theAtoms);
	}
	
	public Cubic(JAtom[] atoms, double a) {
		this. a= a;
		aI = 1/a;
		theAtoms = atoms;
	}
	@Override
	public double calcV() {
		return Math.pow(a, 3);
	}
	@Override
	public double calcD(JVector hkl) {
		double len = hkl.length();
		if(len > 0) { return a/len; }
		return len;
	}
	@Override
	public JVector calcQ(JVector hkl) {
		double qLen = Math.PI*2/calcD(hkl);
		JVector Q = JVector.multiply(hkl.unit(), qLen);
		return Q;
	}
	@Override
	public double calcAngle(JVector hkl1, JVector hkl2) {
		return Math.acos(JVector.dot(hkl1, hkl2) / (hkl1.length() * hkl2.length()));
	}

	@Override
	public JAtom[] getAtoms() { return theAtoms; }

	@Override
	public JVector fractionalToReal(JVector pos) {
		return JVector.multiply(pos, a);
	}
}