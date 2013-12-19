/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

public class StringConverter {

	public synchronized static String arrayToTabString(Object[] arr) {
		if(arr == null)
			return null;
		String s = arr[0].toString();
		for(int i = 1; i < arr.length; i++) {
			s += "\t" + arr[i].toString();
		}
		return s;
	}
	public synchronized static String arrayToNewLineString(Object[] arr) {
		if(arr == null)
			return null;
		String s = arr[0].toString();
		for(int i = 1; i < arr.length; i++) {
			s += "\n" + arr[i].toString();
		}
		return s;
	}
	/**
	 * 
	 * @param arr
	 * @return arr[0] + "\t" + arr[1] + "\t" + ... arr[arr.length-1] + "\t"
	 */
	public synchronized static String arrayToTabString(double[] arr) {
		if(arr == null) {
			return null;
		}
		String s = arr[0] + "";
		for(int i = 1; i < arr.length; i++) {
			s += "\t" + arr[i];
		}
		return s;
	}
	public synchronized static String arrayToTabString(int[] arr) {
		if(arr == null) {
			return null;
		}
		String s = arr[0] + "";
		for(int i = 1; i < arr.length; i++) {
			s += "\t" + arr[i];
		}
		return s;
	}
	public synchronized static String arrayToTabString(long[] arr) {
		if(arr == null) {
			return null;
		}
		String s = arr[0] + "";
		for(int i = 1; i < arr.length; i++) {
			s += "\t" + arr[i];
		}
		return s;
	}
	public synchronized static String arrayToTabString(float[] arr) {
		if(arr == null) {
			return null;
		}
		String s = arr[0] + "";
		for(int i = 1; i < arr.length; i++) {
			s += "\t" + arr[i];
		}
		return s;
	}
	public synchronized static String getSystemFileSeparator() {
		return System.getProperty("file.separator");
	}
}
