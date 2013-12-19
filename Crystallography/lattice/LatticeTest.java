/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import java.util.Arrays;
import java.util.Vector;

import bravaisLattice.BravaisLattice;
import bravaisLattice.BravaisLatticeFactory;

import chemistry.JAtom;
import geometry.JVector;

public class LatticeTest {

	public static void main(String[] args) throws Exception {
		double a = 15;
		JAtom[] atoms = new JAtom[4];
		atoms[0] = new JAtom(6, new JVector(0, 0, 0));
		atoms[1] = new JAtom(6, new JVector(1./2., 1./2., 0));
		atoms[2] = new JAtom(6, new JVector(1./2., 0, 1./2.));
		atoms[3] = new JAtom(6, new JVector(0, 1./2., 1./2.));
		
		BravaisLattice test = BravaisLatticeFactory.getLattice(BravaisLattice.CUBIC, new double[] {a}, atoms);
		
		System.out.println("test calcVolume(): " + test.calcV());
		JVector x = new JVector(1, 0, 0);
		JVector y = new JVector(0, 1, 0);
		
		System.out.println("test calcD(" + x + "): " + test.calcD(x));

		System.out.println("test calcQ(" + x + "): " + test.calcQ(x));
		System.out.println("test calcAngle(" + x + "\t" + y + "): " + test.calcAngle(x, y));
		
		ReciprocalLattice test2 = new ReciprocalLattice(test);
		test2.calculateReciprocalLattice(6);
		Vector<BraggReflection> bragg = test2.getReflections();
		BraggReflection[] reflections = new BraggReflection[bragg.size()];
		reflections = bragg.toArray(reflections);
		Arrays.sort(reflections, new BraggComparator());
		for(int i = 0; i < reflections.length; i++) {
			System.out.println(reflections[i]);
		}
	}
}
