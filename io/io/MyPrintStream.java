/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MyPrintStream {

	private FileOutputStream fos;
	private BufferedOutputStream bos;
	private PrintStream ps;
	private File file;
	private String commentString = "#";
	private int linesPerPrint = 1000;
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * Defaults to appending.
	 * @param fName
	 */
	public MyPrintStream(File fName) {
		this(fName, false);
	}
	/**
	 * 
	 * @param fName
	 * @param append false - replace current file<br>true - append to end of existing file
	 */
	public MyPrintStream(File fName, boolean append) {
		file = fName;
		try {
			fos = new FileOutputStream(fName, append);
			bos = new BufferedOutputStream(fos);
		} catch (FileNotFoundException e) {
			fos = null;
			return;
		}
		ps = new PrintStream(bos);
	}
	public synchronized void printCommentLine() { println(commentString); }
	public synchronized void printCommentLine(int len) {
		String msg = "";
		for(int i = 0; i < len; i++)
			msg += commentString;
		
		println(msg);
	}
	public synchronized void printCommentLine(Object[] msg) {
		for(Object cur : msg)
			printCommentLine(cur);
	}
	public synchronized void printCommentLine(Object msg) { println(commentString + msg.toString()); }
	public synchronized void println() { println(""); }
	public synchronized void println(Object msg) {
		ps.println(msg.toString());
		try {
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void print(String msg) {
		ps.print(msg);
	}
	
	public void close() {
		try {
			ps.flush();
			bos.flush();
			fos.flush();

			ps.close();
			bos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void flush() { ps.flush(); }
	public PrintStream getPrintStream() { return ps; }
	public FileOutputStream getFileOutputStream() { return fos; }
	public String getCommentString() { return commentString; }
	public void setCommentString(String commentString) { this.commentString = commentString; }
	public int getLinesPerPrint() { return linesPerPrint; }
	public void setLinesPerPrint(int linesPerPrint) { this.linesPerPrint = linesPerPrint; }
	
	public void printAll(Iterable<?> iter) {
		int lineIdx = 0;
		String line = "";
		for(Object obj : iter) {
			line += obj.toString() + "\n";
			if(++lineIdx % linesPerPrint == 0) {
				print(line);
				line = "";
			}
		}
		print(line);
	}
	
}
