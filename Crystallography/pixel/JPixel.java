/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package pixel;

import geometry.JComplex;
import geometry.JVector;

public class JPixel implements Comparable<JPixel> {

	JComplex[] sf;
	JVector q;
	double I;
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
	@Override
	public int compareTo(JPixel o) {
		double len1 = q.length();
		double len2 = o.q.length();
		if(len1 < len2) { return -1; }
		else if(len1 == len2) { return 0; }
		else { return 1; }
	}
}

