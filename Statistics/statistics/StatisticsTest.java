/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package statistics;

import java.util.Random;

public class StatisticsTest {

	
	public static void main(String[] args) {
		System.out.println("Testing the 2D array methods in Statistics.java...");
		Random r = new Random(1);
		int dim1 = r.nextInt(10+1);
		int dim2 = r.nextInt(10+1);
		double[][] arr = new double[dim1][dim2];
		
		int idx = 1;
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[i].length; j++)
				arr[i][j] = idx++; 

				System.out.println("Calling Statistics.numElements(arr) which should return: " + dim1*dim2 + " and actually returns:" +
				Statistics.numElements(arr));
		
		System.out.println("Calling Statistics.sum(arr) which should return: " + 300 + " and actually returns:" +
				Statistics.sum(arr));

		System.out.println("Calling Statistics.avg(arr) which should return: " + 12.5 + " and actually returns:" +
				Statistics.avg(arr));
		
		System.out.println("Calling Statistics.stdev(arr) which should return: ~" + 7.071 + " and actually returns:" +
				Statistics.stdev(arr));
	}
}
