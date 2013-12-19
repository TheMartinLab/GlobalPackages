/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package filters;

import java.awt.Color;

import colorScheme.ColorScheme;
import colorScheme.ColorSchemeFactory;
import colorScheme.Step;

public class FilterTest {

	static double[][] makeData() {
		double[][] d = new double[5][5];
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[i].length; j++) {
				d[i][j] = i*d.length + j;
			}
		}
		return d;
	}
	public static void print(double[][] d, ColorScheme cs) {
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[i].length; j++) {
				System.out.println(d[i][j] + "\t" + cs.getColor(d[i][j]));
			}
		}
	}
	public static void main(String[] args) {
		double[][] d = makeData();
		ColorScheme cs = ColorSchemeFactory.getColorScheme(ColorScheme.GRAYSCALE, 0, 1);
		cs.addStep(new Step(.25, Color.red));
		print(d, cs);
		double[][] filtered;
		ImageFilter filter1 = FilterFactory.getImageFilter(ImageFilter.LINEAR);
		filter1 = FilterFactory.getImageFilter(ImageFilter.LOGARITHMIC);
		
		filtered = filter1.apply(d);
		print(filtered, cs);
		filter1 = FilterFactory.getImageFilter(ImageFilter.LOGARITHMIC);
		filtered = filter1.apply(d);
		print(filtered, cs);
		filter1 = FilterFactory.getImageFilter(ImageFilter.NO_FILTER);
		filtered = filter1.apply(d, 5, 10);
		print(filtered, cs);
	}
}
