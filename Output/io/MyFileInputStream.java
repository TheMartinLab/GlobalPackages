/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MyFileInputStream {

	private FileInputStream fis;
	private BufferedInputStream bis;
	private Scanner s;
	
	public MyFileInputStream(File fName) {
		try {
			fis = new FileInputStream(fName);
			bis = new BufferedInputStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		s = new Scanner(bis);
	}
	public MyFileInputStream(String fName) {
		this(new File(fName));
	}
	
	public void close() {
		s.close();
		try {
			bis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Scanner getScanner() { return s; }
	public FileInputStream getFileInputStream() { return fis; }
	
}
