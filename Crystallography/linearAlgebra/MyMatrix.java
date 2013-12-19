/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package linearAlgebra;

public class MyMatrix {


	private double[][] vals;
	private int rows, cols;
	public MyMatrix(double[][] vals) {
		this.vals = new double[vals.length][vals[0].length];
		for(int i = 0; i < vals.length; i++) {
			for(int j = 0; j < vals[i].length; j++) {
				this.vals[i][j]=vals[i][j];
			}
		}
		
		rows = vals.length;
		cols = vals[0].length;
	}
	
	public MyMatrix(double fill, int rows, int cols) {
		vals = new double[rows][cols];
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				vals[row][col] = fill;
			}
		}
		this.rows = rows;
		this.cols = cols;
	}

	/******************************
 	 ******************************
	 **** STATIC CLASS METHODS ****
	 ******************************
	 ******************************/

	/**
	 * 
	 * @param m1
	 * @param m2
	 * @return m1 + m2
	 * @throws MatrixDimensionMismatchException  if (m1.rows != m2.rows && m1.columns != m2.columns)
	 */
	public static MyMatrix add(MyMatrix m1, MyMatrix m2) throws MatrixDimensionMismatchException {
		if(m1.rows != m2.rows && m1.cols != m2.cols) {
			throw new MatrixDimensionMismatchException("Matrix m1 has dimensions: (" + m1.rows + ", " + m1.cols + ") and " + 
					"Matrix m2 has dimensions: (" + m2.rows + ", " + m2.cols + ").  These dimensions are not compatible for " + 
					"matrix addition.");
		}
		
		double[][] vals = new double[m1.vals.length][m1.vals[0].length];
		
		for(int i = 0; i < vals.length; i++) {
			for(int j = 0; j < vals[0].length; j++) {
				vals[i][j] = m1.vals[i][j] + m2.vals[i][j];
			}
		}
		
		return new MyMatrix(vals);
	}
	/**
	 * 
	 * @param m1
	 * @param m2
	 * @return m1 - m2
	 * @throws MatrixDimensionMismatchException if (m1.rows != m2.rows && m1.columns != m2.columns)
	 */
	public static MyMatrix subtract(MyMatrix m1, MyMatrix m2) throws MatrixDimensionMismatchException {
		if(m1.rows != m2.rows && m1.cols != m2.cols) {
			throw new MatrixDimensionMismatchException("Matrix m1 has dimensions: (" + m1.rows + ", " + m1.cols + ") and " + 
					"Matrix m2 has dimensions: (" + m2.rows + ", " + m2.cols + ").  These dimensions are not compatible for " + 
					"matrix subtraction.");
		}
		
		double[][] vals = new double[m1.vals.length][m1.vals[0].length];
		
		for(int i = 0; i < vals.length; i++) {
			for(int j = 0; j < vals[0].length; j++) {
				vals[i][j] = m1.vals[i][j] - m2.vals[i][j];
			}
		}
		
		return new MyMatrix(vals);
	}
	/**
	 * 
	 * @param m
	 * @return m^T
	 */
	public static MyMatrix transpose(MyMatrix m) {
		double[][] vals = m.vals;
		double[][] valsT = new double[m.vals[0].length][m.vals.length];

		for(int row = 0; row < vals.length; row++) {
			for(int col = 0; col < vals[0].length; col++) {
				valsT[col][row] = vals[row][col];
			}
		}
		vals=valsT;
		return new MyMatrix(valsT);
	}


	/**
	 * 
	 * @param m1
	 * @param m2
	 * @return m1 x m2
	 * @throws MatrixDimensionMismatchException  if (m1.rows != m2.columns)
	 */
	public static MyMatrix mult(MyMatrix m1, MyMatrix m2) throws MatrixDimensionMismatchException {
		if(m1.cols != m2.rows) {
			throw new MatrixDimensionMismatchException("Matrix m1 has dimensions: (" + m1.rows + ", " + m1.cols + ") and " + 
					"Matrix m2 has dimensions: (" + m2.rows + ", " + m2.cols + ").  These dimensions are not compatible for " + 
					"matrix multiplication.");
		}
		
		double[][] vals = new double[m1.rows][m2.cols];
		
		MyMatrix m2T = MyMatrix.transpose(m2);
		
		double[] row, col;
		for(int rows = 0; rows < m1.vals.length; rows++) {
			row = m1.vals[rows];
			for(int cols = 0; cols < m2T.vals.length; cols++) {
				col = m2T.vals[cols];
				vals[rows][cols] = MyMatrix.mult(row, col);
			}
		}
		
		return new MyMatrix(vals);
	}
	
	private static double mult(double[] row, double[] col) throws MatrixDimensionMismatchException {
		if(row.length != col.length) {
			throw new MatrixDimensionMismatchException("Row has dimensions: (" + row.length + ") and " + 
					"Col has dimensions: (" + col.length + ").  These dimensions are not compatible for " + 
					"matrix multiplication in method private static double mult(double[] row, double[] col).");
		}
		double val = 0;
		
		for(int i = 0; i < row.length; i++) {
			val += row[i] * col[i];
		}
		
		return val;
	}
	public static MyMatrix invert(MyMatrix matrix) throws MatrixDimensionMismatchException {
		MyMatrix m = (MyMatrix) matrix.clone();
		if(m.rows != m.cols) {
			throw new MatrixDimensionMismatchException("Matrix m has " + m.rows + " rows and " + m.cols + " columns." + 
					" These dimensions are not compatible with the Gaussian elimination method for calculating the " + 
					"matrix inversion");
		}
		double[][] inverse = new double[m.vals.length][m.vals[0].length];
		int dim = m.vals.length;
		
		for (int i = 0; i < dim; i++) {
	        for (int j = 0; j < dim; j++) {
	            inverse[i][j] = 0;
			}
		}
	 
	    for (int i = 0; i < dim; i++) {
	        inverse[i][i] = 1;
		}
	 
	    for (int k = 0; k < dim; k++) {
	        for (int i = k; i < dim; i++) {
	            double val = m.vals[i][k];
	            for (int j = k; j < dim; j++) {
	            	if(val == 0) {
	            		m.vals[i][j] = 0;
	            	} else {
	            		m.vals[i][j] /= val;
	            	}
	            }
	            for (int j = 0; j < dim; j++) {
	            	if(val == 0) {
	            		inverse[i][j] = 0;
	            	} else {
	            		inverse[i][j] /= val;
	            	}
	            }
	        }
	        for (int i = k + 1; i < dim; i++) {
	            for (int j = k; j < dim; j++) {
	                m.vals[i][j] -= m.vals[k][j];
	            }
	            for (int j = 0; j < dim; j++) {
	                inverse[i][j] -= inverse[k][j];
	            }
	        }
	    }
	 
	    for (int i = dim - 2; i >= 0; i--) {
	        for (int j = dim - 1; j > i; j--) {
	            for (int k = 0; k < dim; k++) {
	                inverse[i][k] -= m.vals[i][j] * inverse[j][k];
	            }
	            for (int k = 0; k < dim; k++) {
	                m.vals[i][k] -= m.vals[i][j] * m.vals[j][k];
	            }
	        }
	    }
	    return new MyMatrix(inverse);
	}

	/******************************
 	 ******************************
	 ****** INSTANCE METHODS ******
	 ******************************
	 ******************************/

	public void transpose() {
		double[][] valsT = new double[vals[0].length][vals.length];

		for(int row = 0; row < vals.length; row++) {
			for(int col = 0; col < vals[0].length; col++) {
				valsT[col][row] = vals[row][col];
			}
		}
		vals=valsT;
	}

	public boolean isZero() {
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				if(vals[row][col] != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isUnity() {
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				if(vals[row][col] != 1) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isVal(double val) {
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				if(vals[row][col] != val) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public String toString() {
		String s = "";
		for(int row = 0; row < vals.length; row++) {
			s += "[ ";
			for(int col = 0; col < vals[0].length; col++) {
				s += vals[row][col] + ", ";
			}
			s += "]\n";
		}
		return s;
	}
	
	@Override
	public Object clone() {
		double[][] valsCopy = new double[vals.length][vals[0].length];
		
		for(int i = 0; i < vals.length; i++) {
			for(int j = 0; j < vals[0].length; j++) {
				valsCopy[i][j] = vals[i][j];
			}
		}
		
		MyMatrix newMatrix = new MyMatrix(valsCopy);
		
		return newMatrix;
	}
	
}