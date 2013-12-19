/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package savitzky_golay;

import jama.Matrix;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Vector;

public class SavGolOperations {

	private double[][] data;
	private SavitzkyGolay sg;
	public SavGolOperations() {}
	public SavGolOperations(double[][] data, SavitzkyGolay sg) {
		this.data = data;
		this.sg = sg;
	}
	private double applyMask(SavitzkyGolay.whichMask which, int startI, int startJ) {
		double[][] mask = sg.getMaskAs2d(which);
		double val = 0;
		for(int i = 0; i < mask.length; i++) {
			for(int j = 0; j < mask.length; j++) {
				val += data[i+startI][j+startJ] * mask[i][j];
			}
		}
		return val;
	}
	
	private double[][] get2ndDers(SavitzkyGolay.whichMask which) {
		int maskDim = sg.getMaskDim();
		int offset = (maskDim-1)/2;
		double[][] _2ndDers = new double[data.length][data[0].length];
		for(int i = 0; i < _2ndDers.length-maskDim; i++) {
			for(int j = 0; j < _2ndDers[i].length-maskDim; j++) {
				_2ndDers[i+offset][j+offset] = applyMask(which, i, j);
			}
		}
		return _2ndDers;
	}
	private double[][] get2ndDerSum() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("Testing_SavitzkyGolayOperations.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Matrix _2ndDersX = new Matrix(get2ndDers(SavitzkyGolay.whichMask.X2));
		pw.println("X^2");
		_2ndDersX.print(pw, NumberFormat.FRACTION_FIELD, 10);
		pw.flush();
		Matrix _2ndDersY = new Matrix(get2ndDers(SavitzkyGolay.whichMask.Y2));
		pw.println("Y^2");
		_2ndDersY.print(pw, NumberFormat.FRACTION_FIELD, 10);
		pw.flush();
		Matrix _2ndDersXY = new Matrix(get2ndDers(SavitzkyGolay.whichMask.XY));
		pw.println("X * Y");
		_2ndDersXY.print(pw, NumberFormat.FRACTION_FIELD, 10);
		pw.flush();
		Matrix sum = _2ndDersX.plus(_2ndDersY).plus(_2ndDersXY);
		pw.println("X^2 + Y^2 + X * Y");
		sum.print(pw, NumberFormat.FRACTION_FIELD, 10);
		pw.flush();
		double[][] d = sum.getArray();
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[i].length; j++) {
				pw.println(i + "\t" + j + "\t" + d[i][j]);
			}
		}
		pw.flush();
		return sum.getArray();
	}
	public int[][] getPeaks(){
		int maskDim = sg.getMaskDim();
		int offset = (maskDim-1)/2;
		Vector<int[]> peaks = new Vector<int[]>();
		double[][] sum = get2ndDerSum();
		double testVal;
		boolean lessThanAll;
		for(int i = offset; i < sum.length-offset; i++) {
			for(int j = offset; j < sum[i].length-offset; j++) {
				testVal = sum[i][j];
				lessThanAll = true;
				if(i == 46 && j == 163) {
					System.out.println("found it");
				}
				for(int a = i-1; a <= i+1 && lessThanAll; a++) {
					for(int b = j-1; b <= j+1 && lessThanAll; b++) {
						if(!(a == i && b == j)) {
							if(testVal >= sum[a][b]) {
								lessThanAll = false;
							}
						}
					}
				}
				if(lessThanAll) {
					peaks.add(new int[]{i, j});
				}
				
			}
		}
		return peaks.toArray(new int[peaks.size()][2]);
	}
}
