/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package bravaisLattice;

import chemistry.JAtom;
import geometry.JVector;

public interface BravaisLattice {
	public final static int CUBIC = 0;
	public final static int TRICLINIC = 7;
	/**
	 * Calculate the volume of the bravais lattice
	 * @return the volume
	 */
	public double calcV();
	/**
	 * Calculate the d spacing of a given hkl
	 * @param hkl The hkl
	 * @return the d spacing
	 */
	public double calcD(JVector hkl);
	/**
	 * Calculate the scattering vector Q
	 * @param hkl the hkl indices of the scattering vector
	 * @return the scattering vector
	 */
	public JVector calcQ(JVector hkl);
	/**
	 * Calculate the angle between two lattice planes hkl
	 * @param hkl1 The first lattice plane
	 * @param hkl2 The second lattice plane
	 * @return The angle between hkl1 and hkl2
	 */
	public double calcAngle(JVector hkl1, JVector hkl2);
	
	/**
	 * @return An array of the atoms in the unit cell
	 */
	public JAtom[] getAtoms();
	/**
	 * Convert fractional atomic coordinates to real space coordinates
	 * @param pos	The fractional position of the atom
	 * @return	The real space coordinates of the atom
	 */
	public JVector fractionalToReal(JVector pos);
	
	enum LatticeType {
		/** Primitive Cubic */
		CUBIC_P,
		/** Body-Centered Cubic */
		//Cubic_B,
		/** Face-Centered Cubic */
		//Cubic_F,
		/** Primitive Tetragonal */
		//Tetragonal_P,
		/** Body-Centered Tetragonal */
		//Tetragonal_B,
		/** Primitive Orthorhombic */
		//Orthorhombic_P,
		/** Body-Centered Orthorhombic */
		//Orthorhombic_B,
		/** C-Centered Orthorhombic */
		//Orthorhombic_C,
		/** Face-Centered Orthorhombic */
		//Orthorhombic_F,
		/** Rhombohedral */
		//Rhombohedral,
		/** Hexagonal */
		//Hexagonal,
		/** Primitive Monoclinic */
		//Monoclinic_P,
		/** C-Centered Monoclinic */
		//Monoclinic_C,
		/** Triclinic */
		TRICLINIC;
		public static String[] getLatticeTypes() {
			LatticeType[] latticeTypes = values();
			String[] latticeTypeLabels = new String[values().length];
			for(int i = 0; i < latticeTypeLabels.length; i++) {
				latticeTypeLabels[i] = latticeTypes[i].toString();
			}
			
			return latticeTypeLabels;
		}
		
		public static boolean[] isNeeded(LatticeType type) {
			switch(type) {
			case CUBIC_P:
				return new boolean[] {true, false, false, false, false, false};
			case TRICLINIC:
				return new boolean[] {true, true, true, true, true, true};
			}
			return new boolean[] {false, false, false, false, false, false};
		}
	}
}
