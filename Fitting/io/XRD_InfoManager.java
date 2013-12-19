/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;
import java.util.Vector;

public class XRD_InfoManager {

	private File file;
	
	private Vector<XRD_Info> info;
	
	private String header;
	private String emptyString;
	
	public XRD_InfoManager(File f) {
		file = f;
		XRDParser parser = new XRDParser(file);
		info = parser.parse();
		header = parser.getHeader();
		int numHeadings = header.split("\t").length;
		for(int i = 0; i < numHeadings; i++) {
			emptyString += "\t";
		}
	}
	
	public XRD_Info getInfo(String fileName) {
		XRD_Info testAgainst;
		
		for(int i = 0; i < info.size(); i++) {
			testAgainst = info.get(i);
			if(testAgainst.getFileName().compareTo(fileName) == 0)
				return testAgainst;
		}
		
		return null;
	}
	public String getInfoString(String fileName) {
		XRD_Info testAgainst;
		
		for(int i = 0; i < info.size(); i++) {
			testAgainst = info.get(i);
			if(testAgainst.getFileName().compareTo(fileName) == 0)
				return testAgainst.toString();
		}
		return emptyString;
	}
	public Vector<XRD_Info> getInfo() { return info; }
	public String getHeader() { return header; }
}
