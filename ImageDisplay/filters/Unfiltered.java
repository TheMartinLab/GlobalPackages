/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package filters;

public class Unfiltered implements ImageFilter {

	@Override
	public double[][] apply(double[][] data, double min, double max) {
		double[][] newData = new double[data.length][data[0].length];
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[i].length; j++) {
				if(data[i][j] < min) { newData[i][j] = min; }
				else if(data[i][j] > max) { newData[i][j] = max; }
				else { newData[i][j] = data[i][j]; }
			}
		}
		return newData;
	}

	@Override
	public double[][] apply(double[][] data) {
		return data;
	}

}
