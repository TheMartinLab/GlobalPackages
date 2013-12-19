/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package readFile;

import java.io.File;

public interface FileReader extends Runnable {

	
	public double[] getX();
	public double[] getY();
	public void setFile(File f);
}
