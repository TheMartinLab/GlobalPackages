/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
/**
 * 
 */
package fitting;

import io.MyPrintStream;
import io.StringConverter;
import io.XRD_Info;
import io.XRD_InfoManager;

import jama.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import _TESTING_regression.RegressionException;

import equations.Equation;
import equations_1d.Avrami;
import equations_1d.Equation1;
import fitting.XRDRenormParamsFile.XRDXtalRunParameters;

public class NonLinearFittingExecutable {

	private Params2 p;
	private PrintStream ps_folderWide;
	private String analysisFolderName = "nlf_analysis",
			outputFolderName = "nlf_output",
			fitFolderName = "nlf_fitted";
	private File analysisFolderPath, fitFolderPath, outputFolderPath, logFile, curFile;
	private String fileSeparator = System.getProperty("file.separator");
	private String header;
	private MyPrintStream mpsLog;
	private double[] minIntensities = null, maxIntensities = null, phi = null, Q = null;
	private XRDRenormParamsFile renormParams;
	private XRD_InfoManager infoManager;
	
	private String matrixToString(Matrix m) {
		double[][] d = m.getArray();
		String s  = "";
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[i].length; j++) {
				s += d[i][j] + "\t";
			}
		}
		return s;
		
	}
	public double[][] renormalize(double[][] vals) {
		renormParams.setActiveParamSet(curFile);
		int[] lowerBounds = renormParams.getLowerBounds();
		int[] upperBounds = renormParams.getUpperBounds();
		p.init_k = renormParams.getK();
		p.init_t0 = renormParams.getTau();
		
		
		double[] avgMin = new double[vals[0].length-1];
		double[] avgMax = new double[vals[0].length-1];
		
		System.out.println("vals[" + vals.length + "][" + vals[0].length + "]");
		System.out.println("avgMin[" + avgMin.length + "]");
		System.out.println("avgMax[" + avgMax.length + "]");
		
		// tabulate average min and max values
		for(int col = 1; col < vals[0].length; col++) {
			
			// get average minimum value
			for(int i = lowerBounds[0]; i < lowerBounds[1]; i++) {
				avgMin[col-1] += vals[i][col];
			}
			
			// get average maximum value
			for(int i = upperBounds[0]; i < upperBounds[1]; i++) {
				avgMax[col-1] += vals[i][col];
			}
			
			avgMin[col-1] /= (double) (lowerBounds[1] - lowerBounds[0]);
			avgMax[col-1] /= (double) (upperBounds[1] - upperBounds[0]);
		}
		
		// renormalize the data
		for(int row = p.first_row; row < vals.length; row++) {
			for(int col = 1; col < vals[row].length; col++) {
				vals[row][col] -= avgMin[col-1];
				vals[row][col] /= (avgMax[col-1] - avgMin[col-1]);
			}
		}
		
		return vals;
	}
	public void readRenormalizationParamsFile() {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		
		int returnVal = chooser.showOpenDialog(null);
		switch(returnVal) {
		case JFileChooser.CANCEL_OPTION:
			JOptionPane.showMessageDialog(null, "Open dialog cancelled by user.", "File open cancel message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		File renormFile = chooser.getSelectedFile();
		
		renormParams = new XRDRenormParamsFile(renormFile);
		
		return;
	}
	public double[][] readTimeAndAllData(File fName, int startLine, int colTime, int colData, 
			String delimiter) throws IOException {
		curFile = fName;
		FileInputStream fis = new FileInputStream(fName);
		Scanner s = new Scanner(fis);
		for(int i = 0; i < startLine; i++) {
			s.nextLine();
		}
		int numLines = 0;
		while(s.hasNextLine()) {
			numLines++;
			s.nextLine();
		}
		
		int minIntensityRow = p.min_intensity_line;
		int maxIntensityRow = p.max_intensity_line;
		int qRow = p.q_line;
		int phiRow = p.phi_line;
		fis = new FileInputStream(fName);
		s = new Scanner(fis);		
		String line;
		String[] splitLine;
		String firstLine = s.nextLine();
		int numData = firstLine.split("\t").length;
		for(int i = 1; i < startLine; i++) {
			line = s.nextLine();
			if(i == minIntensityRow) {
				splitLine = line.split(delimiter);
				minIntensities = new double[splitLine.length-1];
				for(int j = 1; j < splitLine.length; j++) {
					minIntensities[j-1] = Double.valueOf(splitLine[j]);
				}
			}
			if(i == maxIntensityRow) {
				splitLine = line.split(delimiter);
				maxIntensities = new double[splitLine.length-1];
				for(int j = 1; j < splitLine.length; j++) {
					maxIntensities[j-1] = Double.valueOf(splitLine[j]);
				}
			}
			if(i == qRow) {
				splitLine = line.split(delimiter);
				Q = new double[splitLine.length-1];
				for(int j = 1; j < splitLine.length; j++) {
					Q[j-1] = Double.valueOf(splitLine[j]);
				}
			}
			if(i == phiRow) {
				splitLine = line.split(delimiter);
				phi = new double[splitLine.length-1];
				for(int j = 1; j < splitLine.length; j++) {
					phi[j-1] = Double.valueOf(splitLine[j]);
				}
			}
		}
		
		double[][] data = new double[numLines][numData];
		int idx = 0;
		if(startLine == 0) {
			splitLine = firstLine.split(delimiter);
			for(int i = 0; i < splitLine.length; i++) {
				data[idx][i] = Double.valueOf(splitLine[i]);
			}
			idx++;
		}
		while(s.hasNextLine()) {
			line = s.nextLine();
			splitLine = line.split(delimiter);
			for(int i = 0; i < splitLine.length; i++) {
				if(splitLine[i].compareTo("#DIV/0!") == 0) {
					data[idx][i] = 0;
				} else {
					data[idx][i] = Double.valueOf(splitLine[i]);
				}
			}
			idx++;
		}
		
		return data;
	}
	public File[] getDataFiles() {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
		chooser.setDialogTitle("Open files to analyze");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			return files;
		} else {
			System.exit(0);
		}
		return null;
	}
	public File openFile(String dialogTitle) {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			System.exit(0);
		}
		return null;
	}
	private double[][] addTimeAndDataIdx(double[][] timeAndAllData, int timeIdx, int dataIdx) {
		double[][] timeAndData = new double[timeAndAllData.length][2];
		for(int i = p.first_row; i < timeAndAllData.length; i++) {
			timeAndData[i][0] = timeAndAllData[i][timeIdx];
			timeAndData[i][1] = timeAndAllData[i][dataIdx];
		}
		return timeAndData;
	}
	private RegressionException doFit(NonLinearFitting nlf, double factor, 
			int iterations, double t0_estimate, Equation1 e) {
		int fittingProblems = 1;
		RegressionException regressionException = null;
		for(int i = 0; i < iterations && fittingProblems != 0; i++) {
			t0_estimate *= factor;
			e.setParam(t0_estimate, 1);
			e.setParam(p.init_k, 0);
			try {
				nlf.doFit();
				fittingProblems = 0;
			} catch(RegressionException re) {
				regressionException = re;
				fittingProblems = 1;
			}
		}
		return regressionException;
	}
	private void analyzeFile(File file) throws IOException {
		double[][] timeAndAllData = readTimeAndAllData(file, p.first_row, 0, 1, "\t");
		double[][] timeAndData;
		int numData = timeAndAllData[0].length-1;
		
		if(renormParams != null) {
			renormalize(timeAndAllData);
		}
		//0.268853017
		//3.095198765

		Equation1 e;
		boolean[] isFittable;
		Constraint[] c = null;
		XRD_Info info = infoManager.getInfo(file.getName());
		if(info == null) {
			return;
		}
		String t0_estimate_string = info.getTimeCrystallizationEstimate();
		double t0_estimate = 0;
		if(t0_estimate_string != null && t0_estimate_string.compareTo("") != 0) {
			t0_estimate = Double.valueOf(t0_estimate_string);
		}
		
		String k_estimate_string = info.getKEst();
		double k_estimate = 0;
		if(k_estimate_string != null && k_estimate_string.compareTo("") != 0) {
			k_estimate = Double.valueOf(k_estimate_string);
			p.init_k = k_estimate;
		}
		
		NonLinearFitting nlf;
		String outName = file.toString();
		outName = outName.substring(outName.lastIndexOf(fileSeparator), outName.lastIndexOf("."));
		FileOutputStream fosAll = null;
		FileOutputStream fosIndividual = null;
		PrintStream psAll = null;
		PrintStream psIndividual = null;
		if(numData > 1) {
			fosAll = new FileOutputStream(analysisFolderPath + fileSeparator + outName + ".analyzed");
			psAll = new PrintStream(fosAll);
		}


		int numFittable = 0;
		if(p.fit_k) { numFittable++; }
		if(p.fit_t0) { numFittable++; }
		if(p.fit_n) { numFittable++; } 
		if(numData > 2) {
			psAll.print(header);
		}
		RegressionException regressionException = null;
		for(int i = 0; i < numData; i++) {
			fosIndividual = new FileOutputStream(analysisFolderPath + fileSeparator + outName + "_" + i + ".analyzed");
			psIndividual = new PrintStream(fosIndividual);
			timeAndData = addTimeAndDataIdx(timeAndAllData, 0, i+1);
			e = new Avrami(new double[] {p.init_k, p.init_t0, p.init_n});
			if(t0_estimate != 0) {
				e.setParam(t0_estimate, 1);
			}
			if(k_estimate != 0) {
				e.setParam(k_estimate, 0);
			}
			isFittable = new boolean[] {p.fit_k, p.fit_t0, p.fit_n};
			nlf = new NonLinearFitting(timeAndData, e, c, isFittable, p.alphaMin, p.alphaMax);
			int fittingProblems;
			try {
				nlf.doFit();
				fittingProblems = 0;
			} catch(RegressionException re) {
				double factor = 0.95;
				int iterations = 50;
				if(t0_estimate != 0) {
					e.setParam(t0_estimate, 1);
				}
				if(k_estimate != 0) {
					e.setParam(k_estimate, 0);
				}
				regressionException = doFit(nlf, factor, iterations, t0_estimate, e);
				if(regressionException == null) {
					fittingProblems = 0;
				} else {
					fittingProblems = 1;
				}
			}
			switch(fittingProblems) {
			case 0:
				regressionException = null;
				System.out.println("\tColumn " + (i+1) + " of " + numData + " complete  with the following parameters.");
				mpsLog.println("\tColumn " + (i+1) + " of " + numData + " complete  with the following parameters.");
				System.out.println("\t\tkA: " + e.getParam(0) + " t0: " + e.getParam(1) + " n: " + e.getParam(2));
				mpsLog.println("\t\tkA: " + e.getParam(0) + " t0: " + e.getParam(1) + " n: " + e.getParam(2));
				printFit(outName + "_" + i, nlf.getTimeDataFit());
				break;
			case 1:
				System.out.println("\tColumn " + (i+1) + " of " + numData + " fitting failed with the following parameters.");
				mpsLog.println("\tColumn " + (i+1) + " of " + numData + " fitting failed with the following parameters.");
				System.out.println("\t\tkA: " + e.getParam(0) + " t0: " + e.getParam(1) + " n: " + e.getParam(2));
				mpsLog.println("\t\tkA: " + e.getParam(0) + " t0: " + e.getParam(1) + " n: " + e.getParam(2));
				break;
			}
			String params = infoManager.getInfoString(file.getName()) + "\t"; 
			params += file.toString() + "\t" + i + "\t";
			params += StringConverter.arrayToTabString(nlf.getParams());
			params += StringConverter.arrayToTabString(nlf.getStdDev());
			params += nlf.getSumSq() + "\t";
			params += nlf.getNumDataPoints() + "\t";
			params += nlf.getR2() + "\t";
			params += nlf.getFisherZ() + "\t";
			if(nlf.getCorr() == null) {
				for(int k = 0; k < numFittable; k++) {
					for(int j = 0; j < numFittable; j++) {
						params += "\t";
					}
				}
			} else {
				params += matrixToString(nlf.getCorr());
			}
			if(nlf.getCovar() == null) {
				for(int k = 0; k < numFittable; k++) {
					for(int j = 0; j < numFittable; j++) {
						params += "\t";
					}
				}
			} else {
				params += matrixToString(nlf.getCovar());
			}
			if(minIntensities != null) { params += minIntensities[i] + "\t"; }
			else { params += "\t"; }
			if(maxIntensities != null) { params += maxIntensities[i] + "\t"; }
			else { params += "\t"; }
			if(Q != null) { params += Q[i] + "\t"; }
			else { params += "\t"; }
			if(phi != null) { params += phi[i] + "\t"; }
			else { params += "\t"; }
			if(regressionException != null) {
				params += regressionException.getMessage() + "\t";
			}
			if(numData > 1) {
				psAll.println(params);
			}
			ps_folderWide.println(params);
			psIndividual.println(nlf.getParameters());
			psIndividual.println(nlf.getError());
		}
	}
	private void printFit(String outName, double[][] timeDataFit) throws IOException {
		File fit = new File(fitFolderPath + fileSeparator + outName);
		FileOutputStream fos = new FileOutputStream(fit);
		PrintStream ps = new PrintStream(fos);
		ps.println("\tData\tNLF Fit");
		for(int i = 0; i < timeDataFit.length-1; i++) {
			ps.println(StringConverter.arrayToTabString(timeDataFit[i]));
		}
		ps.close();
		fos.close();
	}
	private void createFoldersForOutput(File aFile) {
		String folderRoot = aFile.getParent();
		outputFolderPath = new File(folderRoot + fileSeparator + outputFolderName);
		analysisFolderPath = new File(folderRoot + fileSeparator + outputFolderName + fileSeparator + analysisFolderName);
		fitFolderPath = new File(folderRoot + fileSeparator + outputFolderName + fileSeparator + fitFolderName);
		if(!outputFolderPath.exists() && !outputFolderPath.mkdir()) {
			JOptionPane.showMessageDialog(null, "Could not create output file:" + outputFolderPath, "File Access Error", JOptionPane.ERROR_MESSAGE);
		}
		if(!analysisFolderPath.exists() && !analysisFolderPath.mkdir()) {
			JOptionPane.showMessageDialog(null, "Could not create output file:" + analysisFolderPath, "File Access Error", JOptionPane.ERROR_MESSAGE);
		}
		if(!fitFolderPath.exists() && !fitFolderPath.mkdir()) {
			JOptionPane.showMessageDialog(null, "Could not create output file:" + fitFolderPath, "File Access Error", JOptionPane.ERROR_MESSAGE);
		}
		logFile = new File(analysisFolderPath + fileSeparator + "log.txt");
		mpsLog = new MyPrintStream(logFile);
	}
	private void initFolderWidePrintStream() throws FileNotFoundException {
		FileOutputStream fos_folderWide = new FileOutputStream(analysisFolderPath + fileSeparator + "folderWide.analyzed");
		ps_folderWide = new PrintStream(fos_folderWide);
		ps_folderWide.print(header);
	}
	private void setHeader() {
		int numFittable = 0;
		String[] vals = {"kA", "t0", "n"};
		header = infoManager.getHeader();
		header += "\tfile\tcolumn\tkA\tt0\tn\t";
		String err = "";
		if(p.fit_k) { 
			numFittable++; 
			err += "kA err\t";
		}
		if(p.fit_t0) { 
			numFittable++;
			err += "t0 err\t";
		}
		if(p.fit_n) { 
			numFittable++;
			err += "n err\t";
		} 
		header += err + "sum of squares\tnumber of data points in fit\t";
		header += "R^2\tFisher Z\t";
		for(int doTwice = 0; doTwice < 2; doTwice++) {
			if(doTwice == 0) { header += "corr "; }
			else if(doTwice == 1) { header += "covar "; }
			for(int i = 0; i < numFittable; i++) {
				for(int j = 0; j < numFittable; j++) {
					header += vals[i] + "-" + vals[j] + "\t";
				}	
			}
		}
		header += "min intensity\tmax intensity\tQ\tphi";
		header += "\tfitting problems";
		header += "\n";
	}
	public static void main(String[] args) throws IOException {
		NonLinearFittingExecutable nlfe = new NonLinearFittingExecutable();
		File params = new File("params.txt");
		if(!params.exists()) {
			params = nlfe.openFile("Open Parameter File");
		} else { 
			System.out.println("Parameter file loaded: " + params.getAbsolutePath());
		}
		File runInfo = nlfe.openFile("Open XRD Run Info File");
		
		nlfe.infoManager = new XRD_InfoManager(runInfo);
		
		nlfe.p = new Params2(params);
		System.out.println(nlfe.p);
		nlfe.setHeader();
		
		File[] files = nlfe.getDataFiles();
		if(files == null) {
			System.exit(0);
		}
		
		int returnVal = JOptionPane.showConfirmDialog(null, 
				"Renormalize transformation data?", 
				"Renormalize Dialog", 
				JOptionPane.YES_NO_OPTION);
		
		switch(returnVal) {
		case JOptionPane.YES_OPTION:
			nlfe.readRenormalizationParamsFile();
		}
		
		System.out.println("ctrl + c to stop execution.");
		nlfe.createFoldersForOutput(files[0]);
		nlfe.initFolderWidePrintStream();
		for(int i = 0; i < files.length; i++) {
			nlfe.mpsLog.println("\n\nFile: " + files[i].toString() + " opened.");
			System.out.println("\n\nFile: " + files[i].toString() + " opened.");
			if(files[i].toString().compareTo("C:\\Users\\DHD\\Desktop\\$research\\czx-1 xray kinetics - time-data\\all\\250-60-2.txt") == 0) {
				System.out.println("found it");
			}
			nlfe.analyzeFile(files[i]);
			System.out.println("File: " + files[i].toString() + " complete.");
		}
		JOptionPane.showMessageDialog(null, "Complete.", "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0); 
	}
}
class Params2 {
	public boolean fit_n = false;
	public boolean fit_t0 = true;
	public boolean fit_k = true;
	public double alphaMin = 0;
	public double alphaMax = 0.5;
	public double init_k = 0.001;
	public double init_t0 = 0;
	public double init_n = 3;
	public int min_intensity_line = 1, 
			max_intensity_line = 2,
			q_line = 3,
			phi_line = 4,
			x0_line = 5,
			y0_line = 6;
	public int first_row = 7;
	
	enum parameters {
		fit_n,
		fit_t0,
		fit_k,
		alphaMin,
		alphaMax,
		init_k,
		init_t0,
		init_n,
		min_intensity_line,
		max_intensity_line,
		first_row,
		q_line,
		phi_line,
		x0_line,
		y0_line
		;
	}
	public String toString() {
		String s = "";
		s += parameters.fit_n.toString() + "\t\t\t" + fit_n + "\n";
		s += parameters.fit_t0.toString() + "\t\t\t" + fit_t0 + "\n";
		s += parameters.fit_k.toString() + "\t\t\t" + fit_k + "\n";
		s += parameters.alphaMin.toString() + "\t\t" + alphaMin + "\n";
		s += parameters.alphaMax.toString() + "\t\t" + alphaMax + "\n";
		s += parameters.init_k.toString() + "\t\t\t" + init_k + "\n";
		s += parameters.init_t0.toString() + "\t\t\t" + init_t0 + "\n";
		s += parameters.init_n.toString() + "\t\t\t" + init_n + "\n";
		s += parameters.min_intensity_line.toString() + "\t" + min_intensity_line + "\n";
		s += parameters.max_intensity_line.toString() + "\t" + max_intensity_line + "\n";
		s += parameters.first_row.toString() + "\t\t" + first_row + "\n";
		s += parameters.q_line.toString() + "\t\t\t" + q_line + "\n";
		s += parameters.phi_line.toString() + "\t\t" + phi_line + "\n";
		s += parameters.x0_line.toString() + "\t\t" + x0_line + "\n";
		s += parameters.y0_line.toString() + "\t\t" + y0_line + "\n";
		return s;
	}
	public Params2(File file) throws FileNotFoundException {
		parseFile(file);
	}
	
	private void parseFile(File file) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		Scanner s = new Scanner(fis);
		//String line;
		String key;
		String val;
		int lineIdx = 0;
		while(s.hasNextLine()) {
		//	line = s.nextLine();
			key = s.next();
			val = s.next();
			setParam(key, val, lineIdx);
			lineIdx++;
		}
	}
	private void setParam(String key, String val, int lineIdx) {
		switch(parameters.valueOf(key)) {
		case fit_n:
			fit_n = Boolean.valueOf(val);
			break;
		case fit_t0:
			fit_t0 = Boolean.valueOf(val);
			break;
		case fit_k:
			fit_k = Boolean.valueOf(val);
			break;
		case alphaMin:
			alphaMin = Double.valueOf(val);
			break;
		case alphaMax:
			alphaMax = Double.valueOf(val);
			break;
		case init_n:
			init_n = Double.valueOf(val);
			break;
		case init_t0:
			init_t0 = Double.valueOf(val);
			break;
		case init_k:
			init_k = Double.valueOf(val);
			break;
		case min_intensity_line:
			min_intensity_line = Integer.valueOf(val);
			break;
		case max_intensity_line:
			max_intensity_line = Integer.valueOf(val);
			break;
		case first_row:
			first_row = Integer.valueOf(val);
			break;
		case q_line:
			q_line = Integer.valueOf(val);
			break;
		case phi_line:
			phi_line = Integer.valueOf(val);
			break;
		case x0_line:
			x0_line = Integer.valueOf(val);
			break;
		case y0_line:
			y0_line = Integer.valueOf(val);
			break;
		default:
			String msg = "The key: " + key + " or option: " + val + " did not parse correctly\n";
			String options = StringConverter.arrayToTabString(parameters.values());
			String line = "\nOn line: " + lineIdx + " of the input file.";
			JOptionPane.showMessageDialog(null, msg + options + line, "Parse error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
