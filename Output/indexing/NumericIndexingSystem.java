/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package indexing;

import java.io.Serializable;

public class NumericIndexingSystem implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 293306630431681451L;
	private int index = 1;
	private String label = index + "";
	private boolean fixedIndexLength = false;
	private int indexLength = 0;
	public NumericIndexingSystem() {
	}
	public NumericIndexingSystem(int index) {
		this.index = index;
	}
	public void update() {
		index++;
		label = index + "";
		while(fixedIndexLength && label.length() < indexLength) {
			if(index < 0) {
				label = "-0" + label.substring(1);
			} else {
				label = "0" + label;
			}
		}
	}
	public void setIndexLength(int indexLength) {
		this.indexLength = indexLength;
		update();
	}
	public void setFixedIndexLengthEnabled() {
		fixedIndexLength = true;
	}
	public void setFixedIndexLengthDisabled() {
		fixedIndexLength = false;
	}
	
	public String getName() { return label; }
	public String toString() { return getName(); }
	public Object clone() {
		NumericIndexingSystem nis = new NumericIndexingSystem(index);
		nis.label = String.copyValueOf(label.toCharArray());
		nis.fixedIndexLength = fixedIndexLength;
		nis.indexLength = indexLength;
		return nis;
	}
}
