/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import indexing.AlphabeticIndexingSystem;

public class NotebookIndexingSystem {

	private String notebookName;
	private int notebookNumber;
	private int pageNumber;
	private AlphabeticIndexingSystem pageIdx;
	public NotebookIndexingSystem(String notebookName, int notebookNumber, int pageNumber) {
		this.notebookName = notebookName;
		this.notebookNumber = notebookNumber;
		this.pageNumber = pageNumber;
		pageIdx = new AlphabeticIndexingSystem();
	}
	
	public String getCurrentIndex() {
		return notebookName + "_" + notebookNumber + "-" + pageNumber + pageIdx.getName();
	}
	public AlphabeticIndexingSystem getAIS() { return pageIdx; }
	/**
	 * Updates the pageIdx after the string is created.
	 * @return
	 */
	public String getNextIndex() {
		String index = notebookName + "_" + notebookNumber + "-" + pageNumber + pageIdx.getName();
		pageIdx.update();
		return index;
	}
}
