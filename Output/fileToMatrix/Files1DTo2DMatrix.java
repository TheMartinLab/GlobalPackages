/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */

package fileToMatrix;

import io.MyFileInputStream;
import io.MyPrintStream;
import io.StringConverter;

import java.io.File;
import java.util.Scanner;

import readFile.FileReader;
import readFile.PDFGetX3Output;

public class Files1DTo2DMatrix extends FileToMatrix {

	private File[] files;
	private double[][] data;
	private FileReader fr;
	private double[] rowLabels, colLabels;

	public Files1DTo2DMatrix() {}
	public Files1DTo2DMatrix(File[] files) {
		this.files = files;
		fr = new PDFGetX3Output();
	}
	
	public void run() {
		fr.setFile(files[0]);
		fr.run();
		rowLabels = fr.getX();
		double[] y = fr.getY();
		colLabels = new double[files.length];
		data = new double[rowLabels.length][files.length];
		
		for(int i = 0; i < files.length; i++) {
			fr.setFile(files[i]);
			fr.run();
			y = fr.getY();
			for(int j = 0; j < y.length; j++) {
				if(j == 0) {
					// then this is a column header and I need to input the labels
					colLabels[i] = i;
				} else {
					data[j][i] = y[j];
				}
			}
		}
	}
	
	@Override
	public Object getData() { return data; }
	public void setFileReader(FileReader fr) { this.fr = fr; }
	public double[] getRowLabels() { return rowLabels; }
	public File[] getFiles() { return files; }
	public void setFiles(File[] files) { this.files = files; }
	public double[] getColLabels() { return colLabels; }
}
