/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package interpolate;

import io.MyPrintStream;

import java.io.File;
import java.io.FileNotFoundException;

import readFile.ReadFile;

public class Interpolate {

	Double[] interpolatedX, interpolatedY;
	Double[] originalX, originalY;

	public Interpolate(double[][] xy) {
		originalX = new Double[xy.length];
		originalY = new Double[xy.length];
		for(int i = 0; i < originalX.length; i++) {
			originalX[i] = xy[i][0];
			originalY[i] = xy[i][1];
		}
	}
	public void interpolate(double xMin, double xMax, double newStepSize) {
		double x1=0, y1=0, x2=0, y2=0, xn=0, yn=0, dx=1;
		
		int numSteps = (int) Math.rint((xMax-xMin)/newStepSize);
		
		interpolatedX = new Double[numSteps];
		interpolatedY = new Double[numSteps];
		
		for(int i = 0; i < interpolatedX.length; i++) {
			xn = xMin + i*newStepSize;
			
			interpolatedX[i] = xn;
			
			if(xn <= originalX[0]) {
				interpolatedY[i] = originalY[0];
			} else if(xn >= originalX[originalX.length-2]) {
				interpolatedY[i] = originalY[originalY.length-1];
			} else {
				boolean found = false;
				for(int j = 1; j < originalX.length && !found; j++) {
					x1 = originalX[j-1];
					x2 = originalX[j];
					dx = x2-x1;
					if(xn > x1 && xn < x2) {
						found = true;
						y1 = originalY[j-1];
						y2 = originalY[j];
					}
				}
				
				yn =  y2 * (xn - x1)/dx + y1 * (x2-xn)/dx;
				interpolatedY[i] = yn;
				System.out.println(interpolatedY[i]);
			}
		}
	}
	public Double[] getInterpolatedX() { return interpolatedX; }
	public Double[] getInterpolatedY() { return interpolatedY; }
	public void print() {
		MyPrintStream mps = new MyPrintStream(new File("out.txt"));
		for(int i = 0; i < interpolatedX.length; i++) {
			mps.println(interpolatedX[i] + "\t" + interpolatedY[i] + "\t");
		
		}
	}
	
	public static void main(String[] args) {
		ReadFile rf = null;
		try {
			rf = new ReadFile(new File("data.txt"), "\t");
			double[][] data = rf.read();
			Interpolate i = new Interpolate(data);
			i.interpolate(0, 20, .01);
			i.print();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
