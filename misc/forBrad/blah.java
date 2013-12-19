/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package forBrad;

public class blah {

	public Matrix getD(int which) {
		if(which == 0) {
			return new Matrix() {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		}
	}
}
