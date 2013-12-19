/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

import _TESTING_regressionModel.Bounds.BoundingCondition;

public class BoundsTest {

	public static void main(String[] args) {
		Bounds kABounds = new Bounds();
		kABounds.setDebugModeEnabled();
		kABounds.addBound(0.005, BoundingCondition.GREATER_THAN_OR_EQUAL_TO);
		kABounds.addBound(0.009, BoundingCondition.LESS_THAN);
		double val = 0.01;
		System.out.println("Is " + val + " within the bounds?: " + kABounds.withinBounds(val));
	}
}
