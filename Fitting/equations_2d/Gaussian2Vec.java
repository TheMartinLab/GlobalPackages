/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package equations_2d;

import io.MyPrintStream;
import io.StringConverter;

import java.io.File;

import geometry.JVector;
/**
 * EDD_10-168a
 * @author eric
 *
 */
public class Gaussian2Vec implements Runnable {

	private double A, sig1, sig2, phi;
	private final static double MIN = 1e-5;
	private int gridDim;
	private double[][] grid;
	private JVector v1 = JVector.x, v2 = JVector.y, mu;
	/**
	 * EDD_10-168a
	 * <br>Default axes are v1 = (1, 0, 0) & v2 = (0, 1, 0)<br>
	 * @param phi is the amount to rotate the two orthogonal vectors by (clockwise)
	 * @param sig1 is the standard deviation in the v1 direction
	 * @param sig2 is the standard deviation in the v2 direction
	 * @param mu1 is the x-coordinate of the gaussian center
	 * @param mu2 is the y-coordinate of the gaussian center
	 * @param A is the pre-exponential of the gaussian
	 * @param gridDim is the size of the grid to use
	 */
	public Gaussian2Vec(double phi, double sig1, double sig2, double mu1, double mu2, double A, int gridDim) {
		mu = new JVector(mu1, mu2, 0);
		this.sig1 = sig1;
		this.sig2 = sig2;
		this.A = A;
		this.gridDim = gridDim;
		this.phi = phi;
	}
	/**
	 * EDD_10-168a
	 * <br><br>Default axes are v1 = (1, 0, 0) & v2 = (0, 1, 0)
	 * <br><br>*** Same as the other Constructor except the gaussian center is always in the middle of the grid. ***<br>
	 * @param phi is the amount to rotate the two orthogonal vectors by (clockwise)
	 * @param sig1 is the standard deviation in the v1 direction
	 * @param sig2 is the standard deviation in the v2 direction
	 * @param A is the pre-exponential of the gaussian
	 * @param gridDim is the size of the grid to use
	 */
	public Gaussian2Vec(double phi, double sig1, double sig2, double A, int gridDim) {
		mu = new JVector(gridDim/2, gridDim/2, 0);
		this.sig1 = sig1;
		this.sig2 = sig2;
		this.A = A;
		this.gridDim = gridDim;
		this.phi = phi;
	}

	public void run() {
		grid = new double[gridDim][gridDim];
		v1 = v1.unit();
		v2 = v2.unit();
		checkSigma();
		rotateAxes();
		for(int x = 0; x < gridDim; x++) {
			for(int y = 0; y < gridDim; y++) {
				grid[x][y] = evaluateVectorGaussian(x, y);
			}
		}
	}
	private void rotateAxes() {
		v1 = JVector.rotate(v1, JVector.z, JVector.zero, phi);
		v2 = JVector.rotate(v2, JVector.z, JVector.zero, phi);
	}
	private void checkSigma() {
		if(sig1 == 0) 
			sig1 = MIN;
		if(sig2 == 0)
			sig2 = MIN;
	}
	private double evaluateVectorGaussian(int x, int y) {
		double val = 0;
		JVector pos = new JVector(x, y, 0);
		val = Math.pow(JVector.dot(v1, JVector.subtract(pos, mu)), 2)/Math.pow(2*JVector.multiply(v1, sig1).length(), 2) +
				Math.pow(JVector.dot(v2, JVector.subtract(pos, mu)), 2)/Math.pow(2*JVector.multiply(v2, sig2).length(), 2);
		val = A * Math.exp(-val);
		
		return val;
	}
	
	public double[][] getGrid() { return grid; }
}
