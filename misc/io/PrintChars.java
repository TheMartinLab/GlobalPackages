/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

public class PrintChars {

	public static void main(String[] args) {
		char a = 0;
		for(int i = 0; i < 255; i++) {
			System.out.println(i + ": " + a++);
		}
	}
}
