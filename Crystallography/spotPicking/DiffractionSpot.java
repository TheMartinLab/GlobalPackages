/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package spotPicking;

import geometry.JVector;

public class DiffractionSpot {

	private double I;
	private JVector hkl, q, coords;
	
	public DiffractionSpot(JVector coords, double I) {
		this.coords = (JVector) coords.clone();
		this.I = I;
	}
	
	public void setCoords(JVector COORDS) {
		coords = (JVector) COORDS.clone();
	}
	
	public void setQ(JVector Q) {
		q = (JVector) Q.clone();
	}
	
	public void setHKL(JVector HKL) {
		hkl = (JVector) HKL.clone();
	}
	
	public void setI(double i) { I = i; }
	
	public double getI() { return I; }
	public JVector getHKL() { return hkl; }
	public JVector getQ() { return q; }
	public JVector getCoords() { return coords; }
	
}
