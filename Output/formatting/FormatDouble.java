/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package formatting;

import java.text.DecimalFormat;

/**
 * @author Eric
 *
 */
public class FormatDouble {

	private String pattern;
	public enum DecimalPlaces { ONE, TWO, THREE, FOUR };
	private DecimalPlaces dp;
	public FormatDouble(DecimalPlaces numPlaces) {
		dp = numPlaces;
	}
	
	public FormatDouble(String pattern) {
		this.pattern = pattern;
	}
	
	public double format(double in) {
		if(pattern != null) {
			return stringBasedFormat(in);
		} else {
			return enumBasedFormat(in);
		}
		
	}
	
	private double enumBasedFormat(double in) {
		double out = 0;
		double factor = 1;
		switch(dp) {
		case ONE:
			factor = 10;
			break;
		case TWO:
			factor = 100;
			break;
		case THREE:
			factor = 1000;
			break;
		case FOUR:
			factor = 10000;
			break;
		default:
			factor = 1;
		}
		out = Math.rint(in * factor) / factor;
		return out;
	}
	
	private double stringBasedFormat(double in) {
		double out = 0;
		
		DecimalFormat format = new DecimalFormat(pattern);
		
		return Double.valueOf(format.format(in));
	}
	
	public static void main(String[] args) {
		String format = ".####";
		
		FormatDouble fd = new FormatDouble(DecimalPlaces.THREE);
		
		
		System.out.println(fd.format(100.1123001092834));
	}
}
