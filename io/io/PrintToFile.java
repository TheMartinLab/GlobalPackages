/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import jama.Matrix;

import java.io.File;

public class PrintToFile {

	private File f;
	private String header = "";
	private double[][] data;
	
	public PrintToFile(double[][] col1_2, double[] col3, String fName) {
		f = new File(fName);
		if(col1_2.length < col1_2[0].length) { print2(col1_2, col3); }
		else { print(col1_2, col3); }
	}
	
	public PrintToFile(double[][] data, File f) {
		this.f = f;
		this.data = data;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public void setData(double[][] data) {
		this.data = data;
	}
	
	public void print() {
		MyPrintStream mps = new MyPrintStream(f);
		for(int i = 0; i < data.length; i++) {
			mps.println(StringConverter.arrayToTabString(data[i]));
		}
		mps.close();
	}
	private void print(double[][] col1, double[] col3) {
		MyPrintStream mps = new MyPrintStream(f);
		mps.println(header);
		for(int i = 0; i < col1.length; i++) {
			mps.println(col1[i][0] + "\t" + col1[i][1] + "\t" + col3[i]);
		}
		mps.close();
	}
	private void print2(double[][] col1, double[] col3) {
		MyPrintStream mps = new MyPrintStream(f);
		mps.println(header);
		for(int i = 0; i < col1[0].length; i++) {
			mps.println(col1[0][i] + "\t" + col1[1][i] + "\t" + col3[i]);
		}
		mps.close();
	}
	private void printMatrix(String matrix, Matrix m) {
		MyPrintStream mps = new MyPrintStream(new File(matrix));
		double[][] vals = m.getArray();

		mps.println(header);
		for(int i = 0; i < vals.length; i++) {
			mps.println(StringConverter.arrayToTabString(vals[i]));
		}
	}
}
