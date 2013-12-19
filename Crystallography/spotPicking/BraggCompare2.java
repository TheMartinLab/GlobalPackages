/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package spotPicking;

import geometry.JVector;
import lattice.BraggReflection;

public class BraggCompare2 {
	public static void rotate(BraggReflection[] calculated, BraggReflection br, BraggReflection calc) {
		JVector rotAxis = JVector.cross(br.getQ(), calc.getQ());
		double rotAngle = JVector.angleDegrees(br.getQ(), calc.getQ());
		JVector zero = JVector.zero;
		
		JVector q;
		/*
		q = JVector.rotate(calc.getQ(), rotAxis, zero, -rotAngle);
		calc.setQ(q);
		*/
		for(int i = 0; i < calculated.length; i++) {
			q = calculated[i].getQ();
			q = JVector.rotate(q, rotAxis, zero, -rotAngle);
			calculated[i].setQ(q);
		}

		rotAxis = JVector.cross(br.getQ(), calc.getQ());
		rotAngle = JVector.angleDegrees(br.getQ(), calc.getQ());
	}
}
