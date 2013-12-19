/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package matrix;

import jama.Matrix;
import jama.SingularValueDecomposition;

public class SVD implements Runnable {

	private int xMin, xMax, yMin, yMax;
	private double[][] data, limitedData;
	private double[][] U, V, Sdiag;
	private double[][] SRow;


	public SVD(double[][] data, boolean dataLabels) {
		xMin = 0;
		xMax = data.length;
		yMin = 0;
		yMax = data[0].length;
		this.data = data;
		limitedData = new double[xMax-xMin][yMax-yMin];
		if(dataLabels) {
			xMin = 1;
			yMin = 1;
			limitedData = new double[xMax-xMin][yMax-yMin];
		}
		updatedLimits();
	}
	
	private void updatedLimits() {
		int i = 0;
		for(int x = xMin; x < xMax; x++, i++) {
			int j = 0;
			for(int y = yMin; y < yMax; y++, j++) {
				limitedData[i][j] = data[x][y];
			}
		}
	}
	
	public void run() {
		Matrix matA = new Matrix(limitedData);
		SingularValueDecomposition svd = matA.svd();
		Matrix matU = svd.getU();
		double[][] Udata = matU.getArray();
		U = new double[Udata.length+1][Udata[0].length+1];
		for(int i = 0; i < U.length; i++) {
			for(int j = 0; j < U[i].length; j++) {
				if(i == 0) {
					// then this is the first entry in a column
					if(j == 0) {
						U[0][j] = 0;
					} else {
						U[0][j] = j-1;
					}
				} else if(j == 0) {
					// then this is the first entry in a row
					U[i][0] = data[i][0];
				} else {
					U[i][j] = Udata[i-1][j-1];
				}
			}
		}
		
		double[] S = svd.getSingularValues();
		
		SRow = new double[2][S.length];
		for(int i = 0; i < S.length; i++) {
			SRow[0][i] = i;
			SRow[1][i] = S[i];
		}
		
		Matrix matV = svd.getV();
		double[][] Vdata = matV.getArray();
		V = new double[Vdata.length+1][Vdata[0].length+1];
		for(int i = 0; i < V.length; i++) {
			for(int j = 0; j < V[i].length; j++) {
				if(i == 0) {
					// then this is the first entry in a column
					if(j == 0) {
						V[0][0] = 0;
					} else {
						V[0][j] = j-1;
					}
				} else if(j == 0) {
					// then this is the first entry in a row
					V[i][0] = data[0][i];
				} else {
					V[i][j] = Vdata[i-1][j-1];
				}
			}
		}
	}
	
	public double[][] getV() { return V; }
	public double[][] getSdiag() { return Sdiag; }
	public double[][] getSRow() { return SRow; }
	public double[][] getU() { return U; }
	
	public double[][] getData() { return data; }
	public void setData(double[][] data) { this.data = data; }
	
	public double[][] getLimitedData() { return limitedData; }
	public void setLimitedData(double[][] limitedData) { this.limitedData = limitedData; }
	
	public int getyMax() { return yMax; }
	public int getyMin() { return yMin; }
	public int getxMax() { return xMax; }
	public int getxMin() { return xMin; }
	
	public void setyMax(int yMax) { 
		this.yMax = yMax;
		updatedLimits();
	}
	public void setyMin(int yMin) { 
		this.yMin = yMin; 
		updatedLimits();
	}
	public void setxMax(int xMax) { 
		this.xMax = xMax; 
		updatedLimits();
	}
	public void setxMin(int xMin) { 
		this.xMin = xMin; 
		updatedLimits();
	}
}
