/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package twoDimensional;

import java.io.File;
import java.util.Observable;

public class HeatMap extends Observable implements Runnable {

	/* FLAGS */
	public final static int XYZ = 100;
	public final static int XZY = 101;
	public final static int YXZ = 102;
	public final static int YZX = 103;
	public final static int ZXY = 104;
	public final static int ZYX = 105;
	public final static String PROCESSING_FINISHED = "processing finished";
	
	private double[][] xyz;
	private double[][] raw_2d, average_2d, average_1d, raw_1d;
	private int[][] numInProcessed;
	
	private double deltaX, deltaY, xMin, yMin, xMax, yMax;
	private int numStepsX = 50, numStepsY = 50;
	private boolean useNumSteps = true;
	private int xIdx, yIdx, zIdx;
	private File f;
	
	public HeatMap(double[][] xyz, File f) {
		this.xyz = xyz;
		this.setF(f);
	}
	
	public final static int X = 10;
	public final static int Y = 11;
	
	private int valToIdx(int xOrY, double val) {
		int idx;
		switch(xOrY) {
		case X:
			idx = (int) ((val - xMin) / deltaX);
			if(idx == numStepsX) {
				System.out.println("Debug line 41" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "in HeatMap.java");
			}
			return idx;
		case Y:
			idx = (int) ((val - yMin) / deltaY);
			if(idx == numStepsY) {
				System.out.println("Debug line 41" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "in HeatMap.java");
			}
			return idx;
		}
		return 0;
	}
	private double idxToVal(int xOrY, int idx) {
		switch(xOrY) {
		case X:
			return idx * deltaX + xMin;
		case Y:
			return idx * deltaY + yMin;
		}
		return 0;
	}
	public void run() {
		raw_2d = new double[numStepsX][numStepsY];
		average_2d = new double[numStepsX][numStepsY];
		average_1d = new double[numStepsX*numStepsY][3];
		raw_1d = new double[numStepsX*numStepsY][3];
		numInProcessed = new int[numStepsX][numStepsY];
		int xBin = 0, yBin = 0;
		double x, y, z;
		boolean continueRunning = true;
		for(int i = 0; i < xyz.length && continueRunning; i++) {
			x = xyz[i][xIdx];
			y = xyz[i][yIdx];
			z = xyz[i][zIdx];
			
			xBin = valToIdx(X, x);
			yBin = valToIdx(Y, y);
			try {
				raw_2d[xBin][yBin] += z;
				numInProcessed[xBin][yBin]++;
				
			} catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Automatically determining min and max values for x and y.  User specified values do not contain all the data.");
				System.out.println("x value: " + x + " xBin: " + xBin + "\ty value: " + y + " yBin: " + yBin);
				raw_2d = new double[numStepsX][numStepsY];
				average_2d = new double[numStepsX][numStepsY];
				average_1d = new double[numStepsX*numStepsY][3];
				raw_1d = new double[numStepsX*numStepsY][3];
				numInProcessed = new int[numStepsX][numStepsY];
				automaticallySetMinAndMax();
				i=0;
			}
		}
		if(continueRunning) {
			// average
			for(int i = 0; i < raw_2d.length; i++) {
				for(int j = 0; j < raw_2d[i].length; j++) {
					if(numInProcessed[i][j] > 0) {
						average_2d[i][j] = raw_2d[i][j] / ((double) numInProcessed[i][j]);
					}
				}
			}
			
			// to 1d
			int rowIdx = 0;
			for(int i = 0; i < raw_2d.length; i++) {
				for(int j = 0; j < raw_2d[i].length; j++, rowIdx++) {
					average_1d[rowIdx][0] = idxToVal(X, i);
					average_1d[rowIdx][1] = idxToVal(Y, j);
					average_1d[rowIdx][2] = average_2d[i][j];
					
					raw_1d[rowIdx][0] = idxToVal(X, i);
					raw_1d[rowIdx][1] = idxToVal(Y, j);
					raw_1d[rowIdx][2] = raw_2d[i][j];
				}
			}
			setChanged();
			notifyObservers(PROCESSING_FINISHED);
		}
	}
	
	public boolean automaticallySetMinAndMax() {
		if(xyz == null) {
			return false;
		}
		
		double x, y;
		xMax = 0; 
		yMax = 0; 
		xMin = Double.MAX_VALUE; 
		yMin = Double.MAX_VALUE;
		for(int i = 0; i < xyz.length; i++) {
			x = xyz[i][xIdx];
			y = xyz[i][yIdx];
			if(xMax < x) {
				xMax = x;
			}
			if(xMin > x) {
				xMin = x;
			}
			if(yMax < y) {
				yMax = y;
			}
			if(yMin > y) {
				yMin = y;
			}
		}
		deltaX = Math.abs((xMax - xMin) / (numStepsX-1));
		deltaY = Math.abs((yMax - yMin) / (numStepsY-1));
		return true;
	}
	
	/**
	 * 
	 * @param xyzOrder use one of the following static ints: XYZ,XZY,YXZ,YZX,ZXY,ZYX
	 */
	public void setOrder(int xyzOrder) {
		switch(xyzOrder) {
		case XYZ:
			xIdx = 0;
			yIdx = 1;
			zIdx = 2;
			break;
		case XZY:
			xIdx = 0;
			yIdx = 2;
			zIdx = 1;
			break;
		case YXZ:
			xIdx = 1;
			yIdx = 0;
			zIdx = 2;
			break;
		case YZX:
			xIdx = 2;
			yIdx = 0;
			zIdx = 1;
			break;
		case ZXY:
			xIdx = 0;
			yIdx = 1;
			zIdx = 2;
			break;
		case ZYX:
			xIdx = 2;
			yIdx = 1;
			zIdx = 0;
			break;
		}
	}
	
	
	
	/**
	 * @return the deltaX
	 */
	public double getDeltaX() {
		return deltaX;
	}

	/**
	 * @param deltaX the deltaX to set
	 */
	public void setDeltaX(double deltaX) {
		this.deltaX = Math.abs(deltaX);
		numStepsX = (int) Math.rint((xMax - xMin) / this.deltaX);
	}

	/**
	 * @return the deltaY
	 */
	public double getDeltaY() {
		return deltaY;
	}

	/**
	 * @param deltaY the deltaY to set
	 */
	public void setDeltaY(double deltaY) {
		this.deltaY = Math.abs(deltaY);
		numStepsY = (int) Math.rint((yMax - yMin) / this.deltaY);
	}

	/**
	 * @return the xMin
	 */
	public double getxMin() {
		return xMin;
	}

	/**
	 * @param xMin the xMin to set
	 */
	public void setxMin(double xMin) {
		this.xMin = xMin;
	}

	/**
	 * @return the yMin
	 */
	public double getyMin() {
		return yMin;
	}

	/**
	 * @param yMin the yMin to set
	 */
	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	/**
	 * @return the xMax
	 */
	public double getxMax() {
		return xMax;
	}

	/**
	 * @param xMax the xMax to set
	 */
	public void setxMax(double xMax) {
		this.xMax = xMax;
	}

	/**
	 * @return the yMax
	 */
	public double getyMax() {
		return yMax;
	}

	/**
	 * @param yMax the yMax to set
	 */
	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	/**
	 * @return the xyz
	 */
	public double[][] getXyz() {
		return xyz;
	}

	/**
	 * @param xyz the xyz to set
	 */
	public void setXyz(double[][] xyz) {
		this.xyz = xyz;
	}

	/**
	 * @return the numStepsX
	 */
	public int getNumStepsX() {
		return numStepsX;
	}

	/**
	 * @param numStepsX the numStepsX to set
	 */
	public void setNumStepsX(int numStepsX) {
		this.numStepsX = numStepsX;
		deltaX = Math.abs((xMax - xMin) / (numStepsX-1));
	}

	/**
	 * @return the numStepsY
	 */
	public int getNumStepsY() {
		return numStepsY;
	}

	/**
	 * @param numStepsY the numStepsY to set
	 */
	public void setNumStepsY(int numStepsY) {
		this.numStepsY = numStepsY;
		deltaY = Math.abs((yMax - yMin) / (numStepsY-1));
	}

	/**
	 * @return the useNumSteps
	 */
	public boolean isUseNumSteps() {
		return useNumSteps;
	}

	/**
	 * @param useNumSteps the useNumSteps to set
	 */
	public void setUseNumSteps(boolean useNumSteps) {
		this.useNumSteps = useNumSteps;
	}

	/**
	 * @return the raw_2d
	 */
	public double[][] getRaw_2d() {
		return raw_2d;
	}

	/**
	 * @return the average_2d
	 */
	public double[][] getAverage_2d() {
		return average_2d;
	}

	/**
	 * @return the average_1d
	 */
	public double[][] getAverage_1d() {
		return average_1d;
	}

	/**
	 * @return the raw_1d
	 */
	public double[][] getRaw_1d() {
		return raw_1d;
	}

	/**
	 * @return the numInProcessed
	 */
	public int[][] getNumInProcessed() {
		return numInProcessed;
	}
	/**
	 * @return the f
	 */
	public File getF() {
		return f;
	}
	/**
	 * @param f the f to set
	 */
	public void setF(File f) {
		this.f = f;
	}
	/**
	 * @return the xIdx
	 */
	public int getxIdx() {
		return xIdx;
	}
	/**
	 * @param xIdx the xIdx to set
	 */
	public void setxIdx(int xIdx) {
		this.xIdx = xIdx;
	}
	/**
	 * @return the yIdx
	 */
	public int getyIdx() {
		return yIdx;
	}
	/**
	 * @param yIdx the yIdx to set
	 */
	public void setyIdx(int yIdx) {
		this.yIdx = yIdx;
	}
	/**
	 * @return the zIdx
	 */
	public int getzIdx() {
		return zIdx;
	}
	/**
	 * @param zIdx the zIdx to set
	 */
	public void setzIdx(int zIdx) {
		this.zIdx = zIdx;
	}
}
