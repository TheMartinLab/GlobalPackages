/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import java.io.File;

public class ListFilesInFolder {
	
	private File folder;
	public ListFilesInFolder(File folder) {
		this.folder = folder;
	}
	public void printToConsole() {
		File[] files = folder.listFiles();
		for(int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
		}
	}
	public static void main(String[] args) {
		(new ListFilesInFolder(new File("G:\\$research\\czx-1 xray kinetics - time-data\\all"))).printToConsole();
	}
}
