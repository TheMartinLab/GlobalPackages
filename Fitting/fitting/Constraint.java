/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import equations_1d.Equation1;

public class Constraint {

	private int paramIdx;
	private Equation1 e;
	private Relation r;
	public Constraint(int paramIdx, Relation r, Equation1 e) {
		this.paramIdx = paramIdx;
		this.r = r;
		this.e = e;
	}
	
	/**
	 * 
	 * @param val2
	 * @return val Relation val2 ex: val = 1, Relation = LESS_THAN, val2 = 2 ... 1 < 2 will return true
	 */
	public boolean isSatisfied(double val2) {
		double val = e.getParam(paramIdx);
		switch(r) {
		case LESS_THAN:
			if(val < val2) { return true; }
			break;
		case LESS_THAN_OR_EQUAL_TO:
			if(val <= val2) { return true; }
			break;
		case EQUAL_TO:
			if(val == val2) { return true; }
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			if(val >= val2) { return true; }
			break;
		case GREATER_THAN:
			if(val > val2) { return true; }
			break;
		}
		return false;
	}
	
	/**
	 * 
	 * @param val2
	 * @return val2 if isSatisfied(val2) would return true. val otherwise
	 */
	public double check(double val2) {
		double val = e.getParam(paramIdx);
		switch(r) {
		case LESS_THAN:
			if(val < val2) { return val2; }
			break;
		case LESS_THAN_OR_EQUAL_TO:
			if(val <= val2) { return val2; }
			break;
		case EQUAL_TO:
			if(val == val2) { return val2; }
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			if(val >= val2) { return val2; }
			break;
		case GREATER_THAN:
			if(val > val2) { return val2; }
			break;
		}
		return val;
	}
}
