/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;
import java.io.File;
public class ListFiles {
	public static void main(String[] args) {
		File folder = new File("D:\\Documents referenced in lab notebooks\\Dill-12\\34\\a\\in");
		File[] files = folder.listFiles();
		for(int i = 0; i < files.length; i++) {
			System.out.println("inputfile\t=\tin/" + files[i].getName());
		}
	}
}