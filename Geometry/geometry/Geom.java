/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package geometry;

import java.util.Random;

public class Geom {

	
	
	public static JVector[] randomlyRotateOrthogonalAxes(JVector[] axes) {
		Random r = new Random();
		
		double phi;
		// rotate around axis 0
		phi = r.nextDouble() * 90;
		axes[1] = JVector.rotate(axes[1], axes[0], JVector.zero, phi);
		axes[2] = JVector.rotate(axes[2], axes[0], JVector.zero, phi);
		
		// rotate around axis 1
		phi = r.nextDouble() * 90;
		axes[0] = JVector.rotate(axes[0], axes[1], JVector.zero, phi);
		axes[2] = JVector.rotate(axes[2], axes[1], JVector.zero, phi);
		
		// rotate around axis 2
		phi = r.nextDouble() * 90;
		axes[1] = JVector.rotate(axes[1], axes[2], JVector.zero, phi);
		axes[0] = JVector.rotate(axes[0], axes[2], JVector.zero, phi);
		
		return axes;
	}
	

}
