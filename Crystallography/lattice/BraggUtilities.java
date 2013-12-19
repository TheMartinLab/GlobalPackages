/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import geometry.JVector;

import java.util.Vector;

public class BraggUtilities {

	public static BraggReflection[] getUnique(BraggReflection[] br) {
		Vector<BraggReflection> unique = new Vector<BraggReflection>();
		unique.add(br[0]);
		BraggReflection diff, check;
		boolean qIsRepresented = true;
		for(int i = 1; i < br.length; i++) {
			check = br[i];
			for(int j = 0; j < unique.size(); j++) {
				diff = unique.get(j);
				if(diff.sameQ(check, 0)) {
					qIsRepresented = true;
					if(!diff.sameFamily(check)) {
						diff.addDegenerateBragg(check);
					}
				} else {
					qIsRepresented = false;
				}
			}
			if(!qIsRepresented) {
				BraggReflection copy = (BraggReflection) check.clone();
				copy.getHkl().abs();
				copy.getQ().abs();
				unique.add(copy);
				qIsRepresented = true;
			}
		}
		BraggReflection[] u = new BraggReflection[unique.size()];
		u = unique.toArray(u);
		for(int i = 0; i < u.length; i++) {
			System.out.println(u[i].getQ().length() + ":\t" + u[i].getName());
		}
		return u;
	}
	
	public static double getCalcI(BraggReflection meas, BraggReflection[] calc) {
		JVector vecMeas = meas.getHkl();
		
		Vector<BraggReflection> matched = new Vector<BraggReflection>();
		
		double measLen, calcLen;
		
		for(int i = 0; i < calc.length; i++) {
			measLen = vecMeas.absComponentSum();
			calcLen = calc[i].getHkl().absComponentSum();
			if(measLen == calcLen) {
				matched.add(calc[i]);
			}
		}
		
		double averageI = 0;
		for(int i = 0; i < matched.size(); i++) {
			averageI += matched.get(i).getMeasI();
		}
		
		averageI /= matched.size();
		
		return averageI;
	}
	
	public static void alignAlong(JVector alignmentAxis, BraggReflection toAlign, BraggReflection[] all) {
//		JVector rotAxis = JVector.cross(toAlign);
	}
}
