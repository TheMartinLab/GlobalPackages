/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package histogram;

import io.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

public class Histogram_2d {

	private File aFile; 
	private double bin_min_x = 10;
	private double bin_max_x = 0;
	private double bin_min_y = 0;
	private double bin_max_y = -10;
	private int bin_num_x = 30;
	private int bin_num_y = 30;
	private double bin_step_x = 0.01;
	private double bin_step_y = 0.01;
	private double[][] theData;
	private double[][] histogram;
	
	public void set_bin_min_x(double val) { bin_min_x = val; }
	public void set_bin_max_x(double val) { bin_max_x = val; }
	public void set_bin_min_y(double val) { bin_min_y = val; }
	public void set_bin_max_y(double val) { bin_max_y = val; }
	public void set_bin_num_x(int val) { bin_num_x = val; }
	public void set_bin_num_y(int val) { bin_num_y = val; }
	public void set_bin_step_x(double val) { bin_step_x = val; }
	public void set_bin_step_y(double val) { bin_step_y = val; }
	
	public double get_bin_min_x() { return bin_min_x; }
	public double get_bin_max_x() { return bin_max_x; }
	public double get_bin_min_y() { return bin_min_y; }
	public double get_bin_max_y() { return bin_max_y; }
	public int get_bin_num_x() { return bin_num_x; }
	public int get_bin_num_y() { return bin_num_y; }
	public double get_bin_step_x() { return bin_step_x; }
	public double get_bin_step_y() { return bin_step_y; }
	public double[][] getHisto() { return histogram; }
	
	public double[] get_xyI(int x, int y) {
		double x_val = x * bin_step_x + bin_min_x;
		double y_val = y * bin_step_y + bin_min_y;
		double I = histogram[x][histogram[x].length-y-1];
		return new double[] {x_val, y_val, I};
	}
	public Histogram_2d(File file) throws FileNotFoundException {
		aFile = file;
		readFile();
	}
	public void readFile() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(aFile);
		Scanner s = new Scanner(fis);
		
		Vector<double[]> data = new Vector<double[]>();
		String[] line;
		double x, y;
		while(s.hasNext()) {
			line = s.nextLine().split("\t");
			x = Double.valueOf(line[0]);
			y = Double.valueOf(line[1]);
			data.add(new double[] {x, y});
		}
		theData = new double[data.size()][2];
		for(int i = 0; i < theData.length; i++) {
			theData[i] = data.remove(0);
			if(bin_min_x > theData[i][0]) { bin_min_x = theData[i][0]; }
			if(bin_max_x < theData[i][0]) { bin_max_x = theData[i][0]; }
			if(bin_min_y > theData[i][1]) { bin_min_y = theData[i][1]; }
			if(bin_max_y < theData[i][1]) { bin_max_y = theData[i][1]; }
		}
		try {
			fis.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		// set up the histogram
	}
	
	public void createHistogram() {		
		// set up the histogram
		bin_step_x = (bin_max_x - bin_min_x) / (double) bin_num_x;
		bin_step_y = (bin_max_y - bin_min_y) / (double) bin_num_y;
		histogram = new double[bin_num_x+1][bin_num_y+1];
		// set the histogram values to 0
		for(int i = 0; i < histogram.length; i++) {
			for(int j = 0; j < histogram[0].length; j++) {
				histogram[i][j] = 0;
			}
		}
		// fill the histogram
		int v, kA;
		for(int i = 0; i < theData.length; i++) {
			v = (int) Math.round((theData[i][0]-bin_min_x) / bin_step_x);
			kA = (int) Math.round((theData[i][1]-bin_min_y) / bin_step_y);
			histogram[v][kA] += 1;
		}
		// convert the 2d histogram to 1d
		int idx = 0;
		double[][] histogram2 = new double[(bin_num_x+1) * (bin_num_y+1)][3];
		for(int i = 0; i < histogram.length; i++) {
			for(int j = 0; j < histogram[0].length; j++) {
				histogram2[idx][0] = bin_min_x + i * bin_step_x;
				histogram2[idx][1] = bin_min_y + j * bin_step_y;
				histogram2[idx][2] = histogram[i][j];
				idx++;
			}
		}
	}
	public void printFile(File outFile) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintStream ps = new PrintStream(fos);
		
		for(int i = 0; i < histogram.length; i++) {
			for(int j = 0; j < histogram[i].length; j++) {
				ps.print(StringConverter.arrayToTabString(ijToVals(i, j)) + histogram[i][j] + "\n");
			}
		}
	}
	public double[] ijToVals(int i, int j) {
		double iVal = i*bin_step_x + bin_min_x;
		double jVal = j*bin_step_y + bin_min_y;
		return new double[] {iVal, jVal};
	}
}
