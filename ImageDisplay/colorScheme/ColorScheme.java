/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package colorScheme;

import java.awt.Color;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class ColorScheme {

	public final static int GRAYSCALE = 0;
	public final static int INVERSE_GRAYSCALE = 1;
	public final static int BLANK = 2;
	
	private Vector<Step> steps;
	public ColorScheme() {
		steps = new Vector<Step>();
	}
	public Color getColor(double I) {
		// find the bounding colors and values
		double min=0;
		double max = 0;
		Color minColor = null;
		Color maxColor = null;
		Step cur;
		
		// test for below the range
		cur = steps.firstElement();
		if(I <= cur.getValue()) { return cur.getColor(); }
		
		// test for above the range
		cur = steps.lastElement();
		if(I >= cur.getValue()) { return cur.getColor(); }
		
		// figure out the bounding steps
		for(int i = 0; i < steps.size(); i++) {
			cur = steps.get(i);
			if(I < cur.getValue()) {
				min = steps.get(i-1).getValue();
				minColor = steps.get(i-1).getColor();
				max = cur.getValue();
				maxColor = cur.getColor();
				return getColor(I, min, max, minColor, maxColor);
			}
		}
		// this should never execute
		return null;
	}
	public Color getColor(double val, double min, double max, Color minColor, Color maxColor) {
		double percentOfMax = (val-min) / (max-min);
		
		int r = (int) ((1-percentOfMax) * minColor.getRed() + percentOfMax * maxColor.getRed());
		int g = (int) ((1-percentOfMax) * minColor.getGreen() + percentOfMax * maxColor.getGreen());
		int b = (int) ((1-percentOfMax) * minColor.getBlue() + percentOfMax * maxColor.getBlue());
		int a = (int) ((1-percentOfMax) * minColor.getAlpha() + percentOfMax * maxColor.getAlpha());
		
		return new Color(r, g, b, a);
		
	}
	public void addStep(Step r) {
		steps.add(r);
		Collections.sort(steps);
	}
	
	public void removeStep(int idx) {
		steps.remove(idx);
		Collections.sort(steps);
	}
	
	public int getNumSteps() { return steps.size(); }
	public Iterator<Step> getSteps() {
		Collections.sort(steps);
		return steps.iterator();
	}
}
