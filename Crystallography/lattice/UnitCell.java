/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import chemistry.JAtom;

public class UnitCell {

	private JAtom[] atoms;
	
	public UnitCell(JAtom[] atoms) {
		atoms = new JAtom[atoms.length];
		for(int i = 0; i < atoms.length; i++) {
			this.atoms[i] = (JAtom) atoms[i].clone();
		}
	}
	
	/**
	 * @return a reference to the atoms array 
	 */
	public JAtom[] getAtomsReference() { 
		return atoms; 
	}

	/**
	 * @return a copy of the atoms array.
	 */
	public JAtom[] getAtomsCopy() {
		JAtom[] theAtoms = new JAtom[atoms.length];
		for(int i = 0; i < atoms.length; i++) {
			theAtoms[i] = (JAtom) atoms[i].clone();
		}
		return theAtoms;
	}
}
