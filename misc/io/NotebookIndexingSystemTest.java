/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

public class NotebookIndexingSystemTest {

	public static void main(String[] args) {
		String bookName = "EDD";
		int bookNum = 4;
		int pageNum = 78;
		NotebookIndexingSystem nis = new NotebookIndexingSystem(bookName, bookNum, pageNum);
		
		for(int i = 0; i < 80; i++) {
			System.out.println(nis.getNextIndex());
		}
	}
}
