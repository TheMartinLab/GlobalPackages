/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;
import java.io.Serializable;
import java.util.Arrays;


public class AlphabeticIndexingSystem implements Serializable, Cloneable {

	private static final long serialVersionUID = 5749002865707907648L;
	private char[] label;
	public AlphabeticIndexingSystem() {
		label = new char[] {'a'};
	}
	public AlphabeticIndexingSystem(char c) {
		label = new char[] {c};
	}
	public AlphabeticIndexingSystem(char[] arr) {
		label = arr;
	}
	public AlphabeticIndexingSystem(String s) {
		label = s.toCharArray();
	}
	public void update() {
		int currentIndex = label.length-1;
		if(label[currentIndex] == 'z') {
			isZ(currentIndex);
		} else { label[currentIndex]++; }
	}
	private void isZ(int idx) {
		if(idx == 0) {
			if(label[idx] == 'z') { extend(); }
		} else if(label[idx] == 'z') {
			isZ(idx-1);
			label[idx] = 'a';
			label[idx-1]++;
		} 
	}
	private void extend() {
		label = new char[label.length+1];
		Arrays.fill(label, 'a');
	}
	public String getName() { return String.valueOf(label); }
	public String toString() { return getName(); }
	public Object clone() {
		AlphabeticIndexingSystem ais = new AlphabeticIndexingSystem(Arrays.copyOf(label, label.length));
		return ais;
	}
}
