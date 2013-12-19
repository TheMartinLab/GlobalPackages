/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package calculate;

import io.ReadATD;

import java.io.File;

import lattice.BraggReflection;

import chemistry.JAtom;

public class ComputeDiffractionPattern implements Runnable {

	private File atomsFile;
	
	private BraggReflection[] calc;
	
	private double qMax;
	
	public ComputeDiffractionPattern() {
		atomsFile = new File("Test.atd");
		qMax = 10;
	}
	
	public ComputeDiffractionPattern(File atomsFile, double qMax) {
		this.qMax = qMax;
		this.atomsFile = atomsFile;
	}
	
	private JAtom[] readAtomsFile() {
		return new ReadATD(atomsFile).getAtoms();
	}
	
	public void run() {
		JAtom[] someAtoms = readAtomsFile();
		
	}
}
