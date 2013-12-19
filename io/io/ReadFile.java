/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;

public class ReadFile {

	private String delimiter = "\t", 
			header;	
	public final static int UNKNOWN_NUM_ROWS = -1;
	public final static int UNKNOWN_NUM_COLUMNS = -2;
	private Scanner s;
	private File f;
	public ReadFile(File f, String delimiter) throws FileNotFoundException {
		setDelimiter(delimiter);
		setFile(f);
	}
	public ReadFile() { }
	public ReadFile(String delimiter) {
		setDelimiter(delimiter);
	}
	private void initInput() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		s = new Scanner(fis);
	}
	public synchronized double[][] read(int numColumns, int numRows, int firstLine) throws FileNotFoundException {
		boolean unknownNumRows = numRows == UNKNOWN_NUM_ROWS;
		boolean unknownNumColumns = numColumns == UNKNOWN_NUM_COLUMNS;
		
		// initialize the file input stream and scanner
		initInput();
		// figure out the number of rows in the file
		if(unknownNumRows) {
			numRows = 0;
			for(int i = 0; i < firstLine; i++) {
				header = s.nextLine();
			}
			while(s.hasNextLine()) {
				s.nextLine();
				numRows++;
			}
		}
		// reset the file input stream and scanner
		initInput();
		// figure out the number of columns in the file
		Integer[] columnIndices = null;
		Vector<Integer> colIdxs = new Vector<Integer>();
		if(unknownNumColumns) {
			numColumns = 0;
			for(int i = 0; i < firstLine; i++) {
				s.nextLine();
			}
			String line = s.nextLine();
			String[] splitLine = line.split(delimiter);
			double val;
			for(int i = 0; i < splitLine.length; i++) {
				if(splitLine[i].compareTo("") != 0) {
					try {
						val = Double.parseDouble(splitLine[i]);
					} catch(ClassCastException cce) {
						continue;
					}
					numColumns++;
					colIdxs.add(i);
				}
			}
		}
		columnIndices = new Integer[colIdxs.size()];
		columnIndices = colIdxs.toArray(columnIndices);
		
		// reset the file input stream and scanner
		initInput();
		
		// create the array of doubles to store the data
		double[][] data = new double[numRows][numColumns];
		
		// skip the first lines
		for(int i = 0; i < firstLine; i++) {
			s.nextLine();
		}
		// loop through the file and read in the data
		//String[] splitLine;
		/*for(int i = 0; i < numRows-1; i++) {
			//splitLine = s.nextLine().split(delimiter);
			String line = s.nextLine();
			String[] splitLine = line.split(delimiter);
			for(int j = 0; j < 2; j++) {
				int cycle = 0;
				boolean successfulRead = false;
				do {
					try {
						double val = Double.valueOf(splitLine[columnIndices[j] + cycle++]);
						data[i][j] = val;
						successfulRead = true;
					} catch(NumberFormatException nfe) {
						successfulRead = false;
					}
				} while(!successfulRead);
			}
		}*/
		for(int i = 0; i < numRows; i++) {
			//splitLine = s.nextLine().split(delimiter);
			for(int j = 0; j < numColumns; j++) {
				data[i][j] = s.nextDouble();
			}
			try {
				s.nextLine();
			} catch(NoSuchElementException nsee) {
				
			}
		}
		return data;
	}
	public synchronized double[][] read(int numColumns, int firstLine) throws FileNotFoundException {
		return read(numColumns, UNKNOWN_NUM_ROWS, firstLine);
	}
	public synchronized double[][] read(int firstLine) throws FileNotFoundException {
		return read(UNKNOWN_NUM_COLUMNS, UNKNOWN_NUM_ROWS, firstLine);
	}
	public synchronized double[][] read() throws FileNotFoundException {
		return read(UNKNOWN_NUM_COLUMNS, UNKNOWN_NUM_ROWS, 0);
	}
	
	/* GETTERS & SETTERS */

	public String getHeader() {
		return header;
	}
	public File getFile() {
		return f;
	}

	public void setFile(File f) {
		this.f = f;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}
