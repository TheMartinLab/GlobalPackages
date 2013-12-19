/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package indexing;

public class TestNumericIndexingSystem {

	public static void main(String[] args) {
		NumericIndexingSystem nis = new NumericIndexingSystem(-2);
		for(int i = 0; i < 15; i++) {
			nis.update();
			System.out.println(nis.getName());
		}
		
		nis = new NumericIndexingSystem(-5);
		nis.setFixedIndexLengthEnabled();
		nis.setIndexLength(10);
		for(int i = 0; i < 15; i++) {
			nis.update();
			System.out.println(nis.getName());
		}
	}
}
