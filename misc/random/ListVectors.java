/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import java.util.Vector;

import geometry.JVector;

public class ListVectors {

	private int maxLen;
	private Vector<JVector> all;
	public ListVectors(int maxLen) {
		this.maxLen = maxLen;
	}
	public void calcAllLessThan() {
		JVector vec = new JVector();
		all = new Vector<JVector>();
		for(int i = -maxLen; i <= maxLen; i++) {
			vec.i = i;
			for(int j = -maxLen; j <= maxLen; j++) {
				vec.j = j;
				for(int k = -maxLen; k <= maxLen; k++) {
					vec.k = k;
					if(vec.length() <= maxLen) {
						all.add((JVector) vec.clone());
					}
				}
			}
		}
	}
	public void addToAll(JVector add) {
		JVector cur1, cur2;
		int allSize = all.size();
		for(int i = 0; i < allSize; i++) {
			cur1 = all.get(i);
			cur2 = JVector.add(cur1, add);
			if(cur2.length() < maxLen) {
				all.add(cur2);
			}
		}
	}
	public void multiplyBy(double val) {
		Vector<JVector> temp = new Vector<JVector>();
		JVector cur;
		int allSize = all.size();
		for(int i = 0; i < allSize; i++) {
			cur = all.remove(0);
			cur = JVector.multiply(cur, 2);
			temp.add(cur);
		}
		all = temp;
	}
	public JVector sortHKK(int a, int b, int c) {
		if(a == b) {
			return new JVector(c, a, b);
		} else if(a == c) {
			return new JVector(b, a, c);
		}
		return new JVector(a, b, c);
	}
	public JVector sortHKL(int a, int b, int c) {
		if(a > b) {
			if(b > c) {
				return new JVector(a, b, c);
			} else {
				if(a > c) {
				return new JVector(a, c, b);
				} else {
					return new JVector(c, a, b);
				}
			}
		} else{
			if(b > c) {
				if(a > c) {
					return new JVector(b, a, c);
				} else {
					return new JVector(b, c, a);
				}
			} else {
				return new JVector(c, b, a);
			}
		}
	}
	public JVector[] roundAbsAndSort() {
		JVector[] arr = new JVector[all.size()];
		int a, b, c;
		JVector cur;
		for(int i = 0; i  < arr.length; i++) {
			cur = all.get(i);
			a = (int) Math.round(Math.abs(cur.i));
			b= (int) Math.round(Math.abs(cur.j));
			c = (int) Math.round(Math.abs(cur.k));
			
			if(a == b && b == c && a == c) {
				arr[i] = new JVector(a, b, c);
			} else if(a == b || b == c || a == c) {
				arr[i] = sortHKK(a, b, c);
			} else {
				arr[i] = sortHKL(a, b, c);
			}
		}
		return arr;
	}
	public Object[] consolidate(JVector[] raw) {
		Vector<Object[]> vec = new Vector<Object[]>();
		int[] ints = new int[1000];
		Vector<JVector> vecs = new Vector<JVector>();
		
		JVector cur, compare;
		int idx;
		vecs.add(raw[0]);
		ints[0]++;
		boolean added;
		for(int i = 1; i < raw.length; i++) {
			cur = raw[i];
			added = false;
			for(int j = 0; j < vecs.size() && !added; j++) {
				idx = j;
				compare = vecs.get(j);
				if(compare.i == cur.i && compare.j == cur.j && compare.k == cur.k) {
					ints[idx]++;
					added = true;
				}
			}
			if(!added) {
				ints[vecs.size()]++;
				vecs.add(cur);
			}
		}
		JVector[] vectors = new JVector[vecs.size()];
		vectors = vecs.toArray(vectors);
		return new Object[] {vectors, ints};
	}
	public void run() {
		calcAllLessThan();
		addToAll(new JVector(0.5, 0.5, 0.5));
		multiplyBy(2);
		JVector[] roundAbsAndSort = roundAbsAndSort();
		Object[] output = consolidate(roundAbsAndSort);
		JVector[] vecs = (JVector[]) output[0];
		int[] ints = (int[]) output[1];
		JVector vec;
		for(int i = 0; i < vecs.length; i++) {
			vec = JVector.multiply(vecs[i], 0.5);
			System.out.println(vec.i + "\t" + vec.j + "\t" + vec.k + "\t" + vec.length() + "\t" + ints[i]);
		}
	}
	public static void main(String[] args) {
		int maxLen = 10;
		new ListVectors(maxLen).run();
		
	}
}
