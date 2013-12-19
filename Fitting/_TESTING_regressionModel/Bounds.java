/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

/**
 * Basic usage: userValue <<BoundingCondition>> boundingValue 
 * @author DHD
 *
 */
public class Bounds {

	protected Bound lower;
	protected Bound upper;
	protected boolean DEBUG = false;
	public Bounds() {
		lower = new Bound(0, BoundingCondition.UNBOUNDED);
		upper = new Bound(0, BoundingCondition.UNBOUNDED);
	}
	public double withinBounds(double valueToCheck)  {
		boolean aboveLower = aboveLower(valueToCheck);
		boolean belowUpper = belowUpper(valueToCheck);
		if(DEBUG) {
			System.out.println("aboveLower(" + valueToCheck + ") = " + aboveLower);
			System.out.println("belowUpper(" + valueToCheck + ") = " + belowUpper);
		}
		if(!aboveLower) { return lower.val; }
		if(!belowUpper) { return upper.val; }
		return valueToCheck;
	}
	public boolean checkWithinBounds(double valueToCheck) {
		boolean aboveLower = aboveLower(valueToCheck);
		boolean belowUpper = belowUpper(valueToCheck);
		if(DEBUG) {
			System.out.println("aboveLower(" + valueToCheck + ") = " + aboveLower);
			System.out.println("belowUpper(" + valueToCheck + ") = " + belowUpper);
		}
		return aboveLower && belowUpper;
	}
	
	protected boolean belowUpper(double valueToCheck) {
		boolean belowUpper = false;
		switch(upper.condition) {
		case LESS_THAN_OR_EQUAL_TO:
			belowUpper = (valueToCheck <= upper.val);
			break;
		case LESS_THAN:
			belowUpper = (valueToCheck < upper.val);
			break;
		case UNBOUNDED:
			belowUpper = true;
			break;
		}
		return belowUpper;
	}
	protected boolean aboveLower(double valueToCheck) {
		boolean aboveLower = false;
		switch(lower.condition) {
		case GREATER_THAN_OR_EQUAL_TO:
			aboveLower = (valueToCheck >= lower.val);
			break;
		case GREATER_THAN:
			aboveLower = (valueToCheck > lower.val);
			break;
		case UNBOUNDED:
			aboveLower = true;
			break;
		}
		return aboveLower;
	}
	private void areBoundsLogical() throws BoundingLogicException {
		if(lower == null || upper == null)
			return;
		if(lower.condition == BoundingCondition.UNBOUNDED || upper.condition == BoundingCondition.UNBOUNDED) 
			return;
		if(lower.val >= upper.val)
			throw new BoundingLogicException("The lower value cannot exceed the upper value");
	}
	/* SETTERS */
	public void addBound(double value, BoundingCondition condition) 
		throws BoundingLogicException {
		Bound temp = new Bound(value, condition);
		if(condition == BoundingCondition.LESS_THAN || condition == BoundingCondition.LESS_THAN_OR_EQUAL_TO) {
			upper = temp;
			if(DEBUG)
				System.out.println("Upper bound set to (value, condition): (" + value + ", " + condition.toString() + ")");
		}
		if(condition == BoundingCondition.GREATER_THAN || condition == BoundingCondition.GREATER_THAN_OR_EQUAL_TO) {
			lower = temp;
			if(DEBUG)
				System.out.println("Lower bound set to (value, condition): (" + value + ", " + condition.toString() + ")");
		}
		areBoundsLogical();
	}
	/**
	 * Enables println statements throughout this class to aid in debugging.
	 */
	public void setDebugModeEnabled() { DEBUG = true; }
	/**
	 * Debug mode is disabled by default and will only be enabled by a call to setDebugModeEnabled()
	 */
	public void setDebugModeDisabled() { DEBUG = false; }
	/* GETTERS */
	public double getLowerBoundValue() { return lower.val; }
	public double getUpperBoundValue() { return upper.val; }
	public BoundingCondition getLowerBoundCondition() { return lower.condition; }
	public BoundingCondition getUpperBoundCondition() { return upper.condition; }
	
	public enum BoundingCondition {
		LESS_THAN, LESS_THAN_OR_EQUAL_TO, GREATER_THAN_OR_EQUAL_TO, GREATER_THAN, UNBOUNDED
	}
	class Bound {
		public double val;
		public BoundingCondition condition;
		public Bound(double val, BoundingCondition condition) {
			this.val = val;
			this.condition = condition;
		}
	}
	
	public String toString() {
		String lowerCondition = "";
		switch(lower.condition) {
		case GREATER_THAN_OR_EQUAL_TO:
			lowerCondition = ">= " + lower.val;
			break;
		case GREATER_THAN:
			lowerCondition = "> " + lower.val;
			break;
		case UNBOUNDED:
			lowerCondition = "unbounded";
			break;
		}
		
		String upperCondition = "";
		switch(upper.condition) {
		case LESS_THAN_OR_EQUAL_TO:
			upperCondition = "<= " + upper.val;
			break;
		case LESS_THAN:
			upperCondition = "< " + upper.val;
			break;
		case UNBOUNDED:
			upperCondition = "unbounded";
			break;
		}
		
		return "lower: " + lowerCondition + ", upper: " + upperCondition;
	}
}