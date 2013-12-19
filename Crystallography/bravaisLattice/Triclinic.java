/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package bravaisLattice;

import java.util.List;
import chemistry.JAtom;
import geometry.JVector;

public class Triclinic implements BravaisLattice {
	private JAtom[] atoms;
	private JVector A, B, C, invA, invB, invC;

	public Triclinic(JAtom[] atoms, double a, double b, double c, double alpha, double beta, double gamma) {
		this.atoms = atoms;
		initVectors(a, b, c, alpha, beta, gamma);
	}
	public Triclinic(JAtom[] atoms, double[] params) {
		this.atoms = atoms;
		initVectors(params[0], params[1], params[2], params[3], params[4], params[5]);
	}
	private void initVectors(double a, double b, double c, double alpha, double beta, double gamma) {
	}
	/**
	 * Calculate the volume of the bravais lattice
	 * @return the volume
	 */
	@Override
	public double calcV() {
		return JVector.dot(invA, JVector.cross(invB, invC));
	}
	/**
	 * Calculate the d spacing of a given hkl
	 * @param hkl The hkl
	 * @return the d spacing
	 */
	@Override
	public double calcD(JVector hkl) {
		return calcQ(hkl).length()/(2*Math.PI);
	}
	/**
	 * Calculate the scattering vector Q
	 * @param hkl the hkl indices of the scattering vector
	 * @return the scattering vector
	 */
	@Override
	public JVector calcQ(JVector hkl) {
		JVector x = JVector.multiply(invA, hkl.i);
		JVector y = JVector.multiply(invB, hkl.j);
		JVector z = JVector.multiply(invC, hkl.j);
		return JVector.add(x, JVector.add(y, z));
	}
	/**
	 * Calculate the angle between two lattice planes hkl
	 * @param hkl1 The first lattice plane
	 * @param hkl2 The second lattice plane
	 * @return The angle between hkl1 and hkl2
	 */
	@Override
	public double calcAngle(JVector hkl1, JVector hkl2) {
		return JVector.angleDegrees(calcQ(hkl1), calcQ(hkl2));
	}
	
	/**
	 * @return The array of the atoms in the unit cell.
	 */
	@Override
	public JAtom[] getAtoms() { return atoms; }
	/**
	 * Convert fractional atomic coordinates to real space coordinates
	 * @param pos	The fractional position of the atom
	 * @return	The real space coordinates of the atom
	 */
	@Override
	public JVector fractionalToReal(JVector pos) {
		JVector x = JVector.multiply(A, pos.i);
		JVector y = JVector.multiply(B, pos.j);
		JVector z = JVector.multiply(C, pos.k);
		return JVector.add(x, JVector.add(y, z));
	}
}
