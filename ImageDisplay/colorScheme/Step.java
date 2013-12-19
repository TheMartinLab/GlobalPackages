/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package colorScheme;

import java.awt.Color;

public class Step implements Comparable<Step> {

	private double value;
	private Color color;
	
	public Step(double value, Color color) {
		this.value = value;
		this.color = color;
	}
	
	public double getValue() { return value; }
	public Color getColor() { return color; }

	@Override
	public int compareTo(Step o) {
		if(value > o.value) { return 1; }
		if(value == o.value) { return 0; }
		return -1;
	}

}
