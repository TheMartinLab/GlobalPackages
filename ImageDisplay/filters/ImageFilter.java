/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package filters;

public interface ImageFilter {
	public final static int LINEAR = 0;
	public final static int LOGARITHMIC = 1;
	public final static int NO_FILTER = 2;
	//public final static int MEDIAN_FILTER = 3;
	/**
	 * Method to return the data set after it has been normalized to the specified min and max values.
	 * This method creates a new array of doubles to return.
	 * @param data The dataset to filter
	 * @param min 	The min value for the filter. Values < min are set to 0
	 * @param max	The max value for the filter. Values > max are set to 1
	 * @return	A new array of data that has been normalized to 1 with the given min and max parameters
	 */
	public double[][] apply(double[][] data, double min, double max);
	/**
	 * Method to return the data set after it has been normalized to the min and max values of the dataset.
	 * This method does not edit the data parameter
	 * @param data The dataset to filter
	 * @return	A new array of data that has been normalized to 1.
	 */
	public double[][] apply(double[][] data);
}
