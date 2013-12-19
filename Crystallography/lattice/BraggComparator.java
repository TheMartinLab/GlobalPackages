/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import java.util.Comparator;

public class BraggComparator implements Comparator<BraggReflection> {

	@Override
	public int compare(BraggReflection arg0, BraggReflection arg1) {
		double Q0 = arg0.getQ().length();
		double Q1 = arg1.getQ().length();
		if(Q0 < Q1) { return -1; }
		if(Q0 == Q1) { return 0; }
		return 1;
	}

}
