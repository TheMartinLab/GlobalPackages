/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package bravaisLattice;

import java.util.List;

import bravaisLattice.BravaisLattice.LatticeType;
import chemistry.JAtom;

public final class BravaisLatticeFactory {


	/**
	 * 
	 * @param LATTICE_TYPE use the final static int's in the BravaisLattice class (i.e. BravaisLattice.Cubic)
	 * @param params<br>
	 * params[0] = a<br>
	 * params[1] = b<br>
	 * params[2] = c<br>
	 * params[3] = alpha<br>
	 * params[4] = beta<br>
	 * params[5] = gamma<br>
	 * @param atoms must be a list of atoms in REDUCED coordinates (i.e. coordinates go from (0,0,0) to (1,1,1))
	 * @return 
	 */
	public static BravaisLattice getLattice(BravaisLattice.LatticeType type, double[] params, JAtom[] atoms) {
		switch(type) {
		case CUBIC_P:
			return new Cubic(atoms, params[0]);
		case TRICLINIC:
			return new Triclinic(atoms, params);
		default: 
			return new Cubic(atoms, params[0]);
		}
	}
}
