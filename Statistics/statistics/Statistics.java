/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package statistics;

public class Statistics {

	// 2D arrays
	public static int numElements(double[][] array) {
		int numElements = 0;
		for(int i = 0; i < array.length; i++)
			numElements += array[i].length;
		
		return numElements;
	}
	public static double sum(double[][] array) {
		if(array == null) {
			System.out.println("Array is null in Statistics.sum(double[][]) method call");
			return 0;
		}
		double sum = 0;
		for(int i = 0; i < array.length; i++)
			sum += sum(array[i]);
		
		return sum;
	}
	public static double avg(double[][] array) {
		if(array == null) {
			System.out.println("Array is null in Statistics.avg(double[][]) method call");
			return 0;
		}
		double sum = sum(array);
		
		int numElements = numElements(array);
		
		return sum / (double) numElements;
	}
	public static double stdev(double[][] array) {
		if(array == null) {
			System.out.println("Array is null in Statistics.stdev(double[][]) method call");
			return 0;
		}
		
		double avg = avg(array);
		double[][] var = new double[array.length][array[0].length];
		
		for(int i = 0; i < array.length; i++)
			for(int j = 0; j < array[i].length; j++)
				var[i][j] = Math.pow(array[i][j] - avg, 2);
		
		double sum = sum(var);
		
		int numElements = numElements(array);
		
		return Math.sqrt(sum/(-1+(double) numElements));
	}
	// 1D arrays
	public static double sum(double[] array) {
		if(array == null) {
			System.out.println("Array is null in Statistics.sum(double[]) method call");
			return 0;
		}
		double sum = array[0];
		for(int i = 1; i < array.length; i++)
			sum += array[i];
		
		return sum;
	}
	public static double avg(double[] array) {
		if(array == null) {
			System.out.println("Array is null in Statistics.avg(double[]) method call");
			return 0;
		}
		
		double sum = sum(array);
		
		return sum / array.length;
	}
	public static double stdev(double[] array) {
		if(array == null) {
			System.out.println("Array is null in Statistics.stdev(double[]) method call");
			return 0;
		}
		
		double avg = avg(array);
		double[] var = new double[array.length];
		
		for(int i = 0; i < array.length; i++)
			var[i] = Math.pow(array[i] - avg, 2);
		
		double sum = sum(var);
		
		return Math.sqrt(sum/(array.length-1));
	}
}
