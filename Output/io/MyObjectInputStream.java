/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.Inflater;

public class MyObjectInputStream {

	private File f;
	private FileInputStream fis;
	private BufferedInputStream bis;
	private ObjectInputStream ois;
	
	public MyObjectInputStream(File f) {
		this.f = f;
		initObjectStream();
	}
	
	private void initObjectStream() {
		try {
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
		} catch (IOException e) {
			fis = null;
			ois = null;
		}
	}

	public boolean wasInitialized() {
		if(fis == null || ois == null) {
			return false;
		}
		return true;
	}
	public Object readObject() throws IOException, ClassNotFoundException {
		return ois.readObject();
	}
	
	public void close() {
		try {
			ois.close();
			bis.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
