/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package colorScheme;

import java.awt.Color;

public class ColorSchemeFactory {

	public static ColorScheme getColorScheme(int COLOR_SCHEME, double min, double max) {
		
		ColorScheme theScheme = new ColorScheme();
		switch(COLOR_SCHEME) {
		case ColorScheme.GRAYSCALE:
			theScheme.addStep(new Step(min, new Color(0, 0, 0)));
			theScheme.addStep(new Step(max, new Color(255, 255, 255)));
			return theScheme;
		case ColorScheme.INVERSE_GRAYSCALE:
			theScheme.addStep(new Step(min, new Color(255, 255, 255)));
			theScheme.addStep(new Step(max, new Color(0, 0, 0)));
			return theScheme;
		case ColorScheme.BLANK:
			return theScheme;
		default:
			return getColorScheme(ColorScheme.GRAYSCALE, min, max);
		}
		
	}
}
