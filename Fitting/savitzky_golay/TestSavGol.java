/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package savitzky_golay;

import savitzky_golay.SavitzkyGolay.whichMask;

public class TestSavGol {

	private static void printArray(double[] arr) {
		for(int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
		System.out.println();
	}
	private static void printArray(double[][] arr) {
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[i].length; j++) {
				System.out.print(arr[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void main(String[] args) {
		SavitzkyGolay sg = new SavitzkyGolay(SavitzkyGolay.ORDER.THIRD_ORDER, SavitzkyGolay.SEVEN_POINT);
		whichMask[] values = whichMask.values();
		for(int i = 0; i < values.length; i++) {
			System.out.println(values[i].toString());
			printArray(sg.getMaskAs2d(values[i]));
		}
		
	}
}
