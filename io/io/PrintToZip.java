/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class PrintToZip implements Serializable {

	private static final long serialVersionUID = -3763899856162767220L;
	private ZipOutputStream zos;
	private FileOutputStream fos;
	public PrintToZip(ZipOutputStream zos, FileOutputStream fos) {
		this.zos = zos;
		this.fos = fos;
	}
	/**
	 * 
	 * @param toPrint - prints to the ZipFile
	 * @throws ZipException - if a ZIP format error has occurred 
	 * @throws IOException - if an I/O error has occurred
	 */
	public void print(String toPrint) throws ZipException, IOException  {
		byte[] arr = toPrint.getBytes();
		zos.write(arr, 0, arr.length);
	}
	/**
	 * 
	 * @param entry - title of the log
	 * @throws ZipException - if a ZIP format error has occurred 
	 * @throws IOException - if an I/O error has occurred
	 */
	public void newEntry(String entry) throws ZipException, IOException {
		try {
			zos.putNextEntry(new ZipEntry(entry));
		} catch(ZipException e) {
			if(entry.contains(".")) {
				String updated = entry.substring(entry.lastIndexOf("."), entry.length());
				int val = Integer.valueOf(updated);
				entry = entry.substring(0, entry.lastIndexOf(".") + ++val);
				zos.putNextEntry(new ZipEntry(entry));
			}
		}
	}
	/**
	 * 
	 * @throws ZipException - if a ZIP format error has occurred 
	 * @throws IOException - if an I/O error has occurred
	 */
	public void closeEntry() throws ZipException, IOException {
		zos.flush();
		zos.closeEntry();
	}
	/**
	 * 
	 * @throws ZipException - if a ZIP format error has occurred 
	 * @throws IOException - if an I/O error has occurred
	 */
	public void closeStream() throws ZipException, IOException {
		zos.close();
		fos.flush();
		fos.close();
	}
}
