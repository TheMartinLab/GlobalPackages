/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class XRDParser {

	private File file;
	
	private String[] header;
	private String unSplitHeader;
	
	public XRDParser(File f) {
		file = f;
	}
	public Vector<XRD_Info> parse() {
		Vector<XRD_Info> allInfo = new Vector<XRD_Info>();
		
		MyFileInputStream mfis = new MyFileInputStream(file);
		Scanner s = mfis.getScanner();
		
		// parse header
		unSplitHeader = s.nextLine();
		header = unSplitHeader.split("\t");
		
		String[] line;
		String unSplitLine;
		
		XRD_Info.tags tag;
		
		XRD_Info info;
		// parse line by line
		while(s.hasNextLine()) {
			info = new XRD_Info();
			unSplitLine = s.nextLine();
			info.setOriginalLine(unSplitLine);
			
			line = unSplitLine.split("\t");

			for(int i = 0; i < line.length; i++) {
				tag = XRD_Info.tags.valueOf(header[i]);
				info.setTag(tag, line[i]);
			}
			allInfo.add(info);
		}
		
		
		
		return allInfo;
	}
	
	public String getHeader() { return unSplitHeader; }
}
