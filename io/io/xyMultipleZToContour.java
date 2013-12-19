/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;
import java.io.FileNotFoundException;

import readFile.ReadFile;

public class xyMultipleZToContour {

	static int multiplier = 2;
	static int first = 0;
	public static double[][] toContourPlot(double[][] xyzzz) {
		double x, y, z;
		MyPrintStream mps = new MyPrintStream(new File("out.txt"));
		for(int i = 0; i < xyzzz.length; i++) {
			x = xyzzz[i][0];
			for(int j = 300; j < 350; j++) {
				y = first + multiplier * (j-1);
				z = xyzzz[i][j];
				mps.println(x + "\t" + y + "\t" + z);
			}
		}
		return null;
	}
	public static void main(String[] args) {
		File f = new File("in.txt");
		double[][] vals = null;
		try {
			vals = ReadFile.read(f, ReadFile.UNKNOWN_NUM_COLUMNS, ReadFile.UNKNOWN_NUM_ROWS, 0, "\t");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		}
		toContourPlot(vals);
	}
}
