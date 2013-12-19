/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import io.StringConverter;

import java.awt.geom.CubicCurve2D;

public class SolveCubic {

	public static void main(String[] args) {
		double a = 10;
		double b = 2;
		double c = 0;
		double d = 0;
		double[] arr = new double[] {d, c, b, a};
		CubicCurve2D.solveCubic(arr);
		System.out.println(StringConverter.arrayToTabString(arr));
	}
}
