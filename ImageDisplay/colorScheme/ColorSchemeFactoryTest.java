/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package colorScheme;

import java.awt.Color;
import java.util.Iterator;


public class ColorSchemeFactoryTest {

	public static void test(ColorScheme cs) {
		System.out.println("Number of color ranges: " + cs.getNumSteps());
		Iterator<Step> steps = cs.getSteps();
		int idx = 1;
		Step cur;
		while(steps.hasNext()) {
			cur = steps.next();
			System.out.println("Color Step " + idx + ":\t" + cur.getValue() + "\t" + cur.getColor());
		}
		for(int i = -25; i <= 150; i++) {
			System.out.println(cs.getColor(i));
		}
	}
	public static void main(String[] args) {
		ColorScheme cs = ColorSchemeFactory.getColorScheme(ColorScheme.INVERSE_GRAYSCALE, 0, 100);
		test(cs);
		cs.addStep(new Step(50, Color.red));
		test(cs);
		cs.addStep(new Step(125, Color.cyan));
		test(cs);
	}
}
