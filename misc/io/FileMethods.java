/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;

public class FileMethods {

	public static void main(String[] args) {
		File f = new File("D:\\Documents referenced in lab notebooks\\Dill-10\\189\\189i\\makeXYZoutput\\0.875 -- final -- 16.xyz");
		System.out.println("File.toString(): " + f.toString());
		System.out.println("File.getPath(): " + f.getPath());
		System.out.println("File.getName(): " + f.getName());
	}
}
