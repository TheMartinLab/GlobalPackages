/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package savitzky_golay;

import jama.Matrix;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

public class SavitzkyGolay {

	public final static int THREE_POINT = 3;
	public final static int FIVE_POINT = 5;
	public final static int SEVEN_POINT = 7;
	public final static int NINE_POINT = 9;
	public final static int ELEVEN_POINT = 11;
	public final static int THIRTEEN_POINT = 13;
	public final static int FIFTEEN_POINT = 15;
	
	public final static int ZERO_ORDER = 100;
	public final static int FIRST_ORDER = 101;
	public final static int SECOND_ORDER = 102;
	public final static int THIRD_ORDER = 103;
	
	public enum ORDER {
		ZERO_ORDER,
		FIRST_ORDER,
		SECOND_ORDER,
		THIRD_ORDER
		;
	}
	// instance
	private final ORDER order;
	private final int points;
	private Matrix matrix;
	public SavitzkyGolay(ORDER polynomialOrder, int points) {
		order = polynomialOrder;
		this.points = points;
		createMasks();
	}
	
	private void createMasks() {
		Matrix start = createStartingMatrix();
		matrix = start.transpose().times(start).inverse().times(start.transpose());
	}
	private int getRows() { return points * points; }
	private int getCols() {
		int cols = 0;
		switch(order) {
		case THIRD_ORDER:
			cols += 4;
		case SECOND_ORDER:
			cols += 3;
		case FIRST_ORDER:
			cols += 2;
		case ZERO_ORDER:
			cols += 1;
		}
		return cols;
	}
	private Matrix createStartingMatrix() {
		int rows = getRows();
		int cols = getCols();
		Stack<double[]> stack = new Stack<double[]>();
		switch(order) {
		case THIRD_ORDER:
			push2d(stack, thirdOrderVals());
		case SECOND_ORDER:
			push2d(stack, secondOrderVals());
		case FIRST_ORDER:
			push2d(stack, firstOrderVals());
		case ZERO_ORDER:
			stack.push(zeroOrderVals());
		}
		double[][] transposedMatrixVals = new double[stack.size()][rows];
		int stackSize = stack.size();
		for(int i = 0; i < stackSize; i++) {
			transposedMatrixVals[i] = stack.pop();
		}
		double[][] matrixVals = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				matrixVals[i][j] = transposedMatrixVals[j][i];
			}
		}
		return new Matrix(matrixVals);
	}
	private void push2d(Stack<double[]> stack, double[][] vals) {
		for(int i = vals.length-1; i >= 0; i--) {
			stack.push(vals[i]);
		}
	}
	private double[][] thirdOrderVals() {
		int rows = getRows();
		double[][] vals = new double[4][rows];
		double[][] firstOrder = firstOrderVals();
		for(int i = 0; i < vals[0].length; i++) {
			// x^3
			vals[3][i] = firstOrder[0][i] * firstOrder[0][i] * firstOrder[0][i];
			// x^2 * y
			vals[2][i] = firstOrder[0][i] * firstOrder[0][i] * firstOrder[1][i];
			// x * y^2
			vals[1][i] = firstOrder[0][i] * firstOrder[1][i] * firstOrder[1][i];
			// y^3
			vals[0][i] = firstOrder[1][i] * firstOrder[1][i] * firstOrder[1][i];
		}
		return vals;
	}
	private double[][] secondOrderVals() {
		int rows = getRows();
		double[][] vals = new double[3][rows];
		double[][] firstOrder = firstOrderVals();
		for(int i = 0; i < vals[0].length; i++) {
			// x^2
			vals[2][i] = firstOrder[0][i] * firstOrder[0][i];
			// x*y
			vals[1][i] = firstOrder[0][i] * firstOrder[1][i];
			// y^2
			vals[0][i] = firstOrder[1][i] * firstOrder[1][i];
		}
		return vals;
	}
	private double[][] firstOrderVals() {
		int rows = getRows();
		int offset = (points-1)/2;
		double[][] vals = new double[2][rows];
		int idx = 0;
		for(int y = -offset; y <= offset; y++) {
			for(int x = -offset; x <= offset; x++, idx++) {
				vals[1][idx] = x;
				vals[0][idx] = y;
			}
		}
		return vals;
	}
	private double[] zeroOrderVals() {
		double[] vals = new double[getRows()];
		for(int i = 0; i < vals.length; i++) {
			vals[i] = 1;
		}
		return vals;
	}
	enum whichMask {
		SMOOTH,
		X,
		Y,
		X2,
		XY,
		Y2,
		X3,
		X2Y,
		XY2,
		Y3
		;
	}
	public double[] getMaskAs1d(whichMask which) {
		double[] mask = matrix.getRowPackedCopy();
		int offset = which.ordinal();
		int rowsPerCol = getRows();
		return Arrays.copyOfRange(mask, offset*rowsPerCol, (offset+1)*rowsPerCol);
	}
	public double[][] getMaskAs2d(whichMask which) {
		double[] vals = getMaskAs1d(which);
		int idx = 0;
		int dim = (int) Math.round(Math.sqrt(vals.length));
		double[][] mask = new double[dim][dim];
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				mask[i][j] = vals[idx++];
			}
		}
		return mask;
	}
	public Matrix getMaskAsMatrix(whichMask which) {
		double[][] vals = getMaskAs2d(which);
		return new Matrix(vals);
	}
	public int getMaskDim() { return points; }

}
