/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package linearAlgebra;

public class MatrixTest {

	public static void main(String[] args) {
		double[][] vals1 = { {2, -1}, {0, 3}, {1, 0}};
		MyMatrix m1 = new MyMatrix(vals1);

		double[][] vals2 = { {0, 1, 4, -1}, {-2, 0, 0, 2}};
		MyMatrix m2 = new MyMatrix(vals2);
		MyMatrix mult = null;
		try {
			mult = MyMatrix.mult(m1, m2);
		} catch (MatrixDimensionMismatchException e) {
			e.printStackTrace();
		}
		double[][] valsAns = { {2, 2, 8, -4}, {-6, 0, 0, 6}, {0, 1, 4, -1}};
		MyMatrix ans = new MyMatrix(valsAns);
		System.out.println("\n\nMatrix multiplication test: ");
		System.out.println("Matrix 1: \n" + m1);
		System.out.println("Matrix 2: \n" + m2);
		System.out.println("m1 x m2: \n" + mult);
		try {
			System.out.println("If the value here isn't true, there's a problem with the matrix " 
					+ "multiplication or subtraction routine: " + (MyMatrix.subtract(mult, ans)).isZero());
		} catch (MatrixDimensionMismatchException e1) {
			e1.printStackTrace();
		}
		
		int val1 = 1;
		int val2 = 2;
		m1 = new MyMatrix(val1, 4, 4);
		m2 = new MyMatrix(val2, 4, 4);
		MyMatrix add = null;
		try {
			add = MyMatrix.add(m1, m2);
		} catch (MatrixDimensionMismatchException e) {
			e.printStackTrace();
		}
		ans = new MyMatrix(val1+val2, 4, 4);
		System.out.println("Matrix addition test: " );
		System.out.println("Matrix 1: \n" + m1);
		System.out.println("Matrix 2: \n" + m2);
		System.out.println("m1 + m2: \n" + add);
		try {
			System.out.println("If the value here isn't true, there's a problem with the matrix " 
					+ "addition or subtraction routine: " + (MyMatrix.subtract(add, ans)).isZero());
		} catch (MatrixDimensionMismatchException e) {
			e.printStackTrace();
		}
		
		double[][] vals3 = { {1, 3, 3}, {1, 4, 3}, {1, 3, 4} };
		m1 = new MyMatrix(vals3);
		valsAns = new double[][] { {7, -3, -3}, {-1, 1, 0}, {-1, 0, 1} };
		ans = new MyMatrix(valsAns);
		MyMatrix inv = null;
		try {
			inv = MyMatrix.invert(m1);
		} catch (MatrixDimensionMismatchException e1) {
			e1.printStackTrace();
		}
		System.out.println("Matrix inversion test: " );
		System.out.println("Matrix 1: \n" + m1);
		System.out.println("m1^-1: \n" + inv);
		try {
			System.out.println("If the value here isn't true, there's a problem with the matrix " 
					+ "inversion or subtraction routine: " + (MyMatrix.subtract(inv, ans)).isZero());
		} catch (MatrixDimensionMismatchException e) {
			e.printStackTrace();
		}
	}
}
