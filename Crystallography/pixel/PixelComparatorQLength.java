/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package pixel;

import java.util.Comparator;

public class PixelComparatorQLength implements Comparator<JPixel> {
	@Override
	public int compare(JPixel o1, JPixel o2) {
		double q1 = o1.q.length();
		double q2 = o2.q.length();
		if(q1 < q2) { return -1; }
		else if(q1 == q2) { return 0; }
		else return 1;
	}
	
}