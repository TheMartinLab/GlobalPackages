/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;

public class AppendSuffix {

	public static void appendSuffix(File[] files, String newSuffix) {
		for(int i = 0; i < files.length; i++) {
			if(files[i].isFile()) {
				files[i].renameTo(new File(files[i] + "." + newSuffix));
			}
		}
	}
	public static void main(String[] args) {
		File folder = new File("Z:\\DATA\\BNL 02-07\\CZX1\\250-50-1\\");
		File[] files = folder.listFiles();
		appendSuffix(files, "xray");
	}
}
