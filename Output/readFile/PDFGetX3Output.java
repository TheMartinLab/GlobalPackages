/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package readFile;

import io.MyFileInputStream;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class PDFGetX3Output implements FileReader {

	private final static int DATA_START_LINE = 28;
	private final static int R_MIN_LINE = 19;
	private final static int R_MAX_LINE = 20;
	private final static int R_STEP_LINE = 21;

	private String delimiter = " ";
	private double[] x, y;
	private File f;
	private double xMin, xMax, yMin, yMax;
	private boolean xLimits = false, yLimits = false;
	
	public boolean isyLimits() { return yLimits; }
	public void setyLimits(boolean yLimits) { this.yLimits = yLimits; }
	public boolean isxLimits() { return xLimits; }
	public void setxLimits(boolean xLimits) { this.xLimits = xLimits; }
	public double getyMax() { return yMax; }
	public void setyMax(double yMax) { this.yMax = yMax; }
	public double getyMin() { return yMin; }
	public void setyMin(double yMin) { this.yMin = yMin; }
	public double getxMax() { return xMax; }
	public void setxMax(double xMax) { this.xMax = xMax; }
	public double getxMin() { return xMin; }
	public void setxMin(double xMin) { this.xMin = xMin; }
	public double[] getX() { return x; }
	public double[] getY() { return y; }
	
	private int findNumDataLines() {
		MyFileInputStream mfis = new MyFileInputStream(f);
		Scanner s = mfis.getScanner();
		
		int numLines = 0;
		for(int i = 0; i < DATA_START_LINE; i++) {
			s.nextLine();
		}
		
		while(s.hasNextLine()) {
			s.nextLine();
			numLines++;
		}
		
		mfis.close();
		
		return numLines;
	}
	@Override
	public void run() {
		int numDataLines = findNumDataLines();
		MyFileInputStream mfis = new MyFileInputStream(f);
		Scanner s = mfis.getScanner();
		for(int i = 0; i < DATA_START_LINE; i++) {
			s.nextLine();
		}
		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		// figure out how many lines
		
		double xVal, yVal;
		String line;
		String[] splitLine;
		for(int i = 0; i < numDataLines && s.hasNextLine(); i++) {
			line = s.nextLine();
			splitLine = line.split(delimiter);
			xVal = Double.valueOf(splitLine[0]);
			yVal = Double.valueOf(splitLine[1]);

			if(xLimits && yLimits) {
				if(xVal > xMin && xVal < xMax && yVal > yMin && yVal < yMax) {
					x.add(xVal);
					y.add(yVal);
				}
			} else if(xLimits) {
				if(xVal > xMin && xVal < xMax) {
					x.add(xVal);
					y.add(yVal);
				}
			} else if(yLimits) {
				if(yVal > yMin && yVal < yMax) {
					x.add(xVal);
					y.add(yVal);
				}
			} else {
				x.add(xVal);
				y.add(yVal);
			}
		}
		
		this.x = new double[x.size()];
		this.y = new double[y.size()];
		
		for(int i = 0; i < this.x.length; i++) {
			this.x[i] = Double.valueOf(x.get(i));
		}
		for(int i = 0; i < this.y.length; i++) {
			this.y[i] = Double.valueOf(y.get(i));
		}
		mfis.close();
	}



	@Override
	public void setFile(File f) {
		this.f = f;
	}
}
