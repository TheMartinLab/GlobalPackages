/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package pdfgetx3;

import fileToMatrix.Files1DTo2DMatrix;
import io.MyPrintStream;
import io.StringConverter;

import java.io.File;
import java.util.Vector;

import matrix.SVD;

import readFile.PDFGetX3Output;

/**
 * @author Eric
 *
 */
public class ParseX3Output_v2 {

	private String dataExtension = ".dat",
			infoExtension = ".info",
			summaryFileRoot,
			inputExtension;

	private File[] inputFolders;
	
	private File verboseOutputFolder, outputFolder;

	private Vector<double[][]> A;

	private Vector<String[][]> output;
	
	private double xMin, xMax, yMin, yMax;
	
	private double[] x;
	
	public void ParseX3Output_v2() {
		
	}

	
	public void run() {
		constructA();
		runSVD();
		formatOutput();
		printOutput();
	}
	
	private void constructA() {
		A = new Vector<double[][]>();
		File[] inputFiles;

		Files1DTo2DMatrix toMat = new Files1DTo2DMatrix();
		PDFGetX3Output x3 = new PDFGetX3Output();
		if(xLimited) {
			x3.setxLimits(xLimited);
			x3.setxMin(xMin);
			x3.setxMax(xMax);
		}
		if(yLimited) {
			x3.setyLimits(yLimited);
			x3.setyMin(yMin);
			x3.setyMax(yMax);
		}
		toMat.setFileReader(x3);
		
		for(int i = 0; i < inputFolders.length; i++) {
			Vector<File> in = new Vector<File>();
			inputFiles = inputFolders[i].listFiles();
			for(int j = 0; j < inputFiles.length; j++) {
				if(inputFiles[j].getName().contains(inputExtension)) {
					in.add(inputFiles[j]);
				}
			}
			inputFiles = new File[in.size()];
			inputFiles = in.toArray(inputFiles);
			if(inputFiles.length > 0) {
				toMat.setFiles(inputFiles);
				toMat.run();
				
				double[][] data = (double[][]) toMat.getData();
				A.add(data);
				x = toMat.getRowLabels();
			}
		}
	}
	
	/**
	 * Output U, S, V, individually to "verboseOutputFolder"
	 */
	private void runSVD() {
		SVD svd;
		double[][] a, U, V;
		double[] S;
		
		for(int i = 0; i < A.size(); i++) {
			a = A.get(i);
			svd = new SVD(a, false);
			svd.run();
			S = svd.getSRow();
			U = svd.getU();
			V = svd.getV();
			
		}
	}
	
	private void formatOutput() {
		
	}
	
	/**
	 * Output to "summaryFile"
	 */
	private void printOutput() {
		MyPrintStream mps;
		String[][] out;
		
		for(int i = 0; i < output.size(); i++) {
			mps = new MyPrintStream(new File(outputFolder + File.separator + summaryFileRoot + " -- " + i + dataExtension));
			out = output.get(i);
			for(int j = 0; j < out.length; j++) {
				mps.println(StringConverter.arrayToTabString(out[j]));
			}
			mps.close();
		}
	}
	
	public String getDataExtension() { return dataExtension; }
	public void setDataExtension(String dataExtension) { this.dataExtension = dataExtension; }
	public String getInfoExtension() { return infoExtension; }
	public void setInfoExtension(String infoExtension) { this.infoExtension = infoExtension; }
	public File getVerboseOutputFolder() { return verboseOutputFolder; }
	public void setVerboseOutputFolder(File verboseOutputFolder) { this.verboseOutputFolder = verboseOutputFolder; }
	public File getOutputFolder() { return outputFolder; }
	public void setOutputFolder(File outputFolder) { this.outputFolder = outputFolder; }
	public String getSummaryFileRoot() { return summaryFileRoot; }
	public void setSummaryFileRoot(String summaryFileRoot) { this.summaryFileRoot = summaryFileRoot; }
	private boolean xLimited, yLimited;
	public boolean isxLimited() { return xLimited; }
	public void setxLimited(boolean xLimited) { this.xLimited = xLimited; }
	public boolean isyLimited() { return yLimited; }
	public void setyLimited(boolean yLimited) { this.yLimited = yLimited; }
	public double getxMin() { return xMin; }
	public void setxMin(double xMin) { this.xMin = xMin; }
 	public double getxMax() { return xMax; }
	public void setxMax(double xMax) { this.xMax = xMax; }
	public double getyMin() { return yMin; }
	public void setyMin(double yMin) { this.yMin = yMin; }
	public double getyMax() { return yMax; }
	public void setyMax(double yMax) { this.yMax = yMax; }
	public String getInputExtension() { return inputExtension; }
	public void setInputExtension(String inputExtension) { this.inputExtension = inputExtension; }
	public File[] getInputFolders() { return inputFolders; }
	public void setInputFolders(File[] inputFolders) { this.inputFolders = inputFolders; }
	
}
