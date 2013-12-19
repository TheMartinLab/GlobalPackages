/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
import geometry.JComplex;
import geometry.JVector;

public class JPixel {

	private JComplex[] sf;
	private JVector q;
	private double I;
	public JPixel(int numSf, JVector q) {
		sf = new JComplex[numSf];
		this.q = (JVector) q.clone();
		I = 0;
	}
	public JPixel(int numSf) {
		sf = new JComplex[numSf];
		I = 0;
	}
	public double getI() { return I; }
	public void setQ(JVector q) { this.q = (JVector) q.clone(); }
	public void setSf(int idx, JComplex toSet) { sf[idx] = (JComplex) toSet.clone(); }
	public JVector getq() { return q; }
}
