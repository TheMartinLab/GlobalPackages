/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import io.StringConverter;

import jama.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import savitzky_golay.SavGolOperations;
import savitzky_golay.SavitzkyGolay;

import equations_1d.Avrami;
import equations_1d.Equation1;

public class NonLinearFittingExecutable_n_t0_grid implements Runnable {

	private Params_n_t0_grid p;
	private PrintStream ps_folderWide;
	private String analysisFolderName = "nlf_analysis",
			outputFolderName = "nlf_output",
			fitFolderName = "nlf_fitted";
	private File analysisFolderPath, fitFolderPath, outputFolderPath;
	private String fileSeparator = System.getProperty("file.separator");
	private String header;
	private double approx_kA = 0.01;
	private static PrintStream ps_converged;
	private double[][] t0_vol;
	private File file;
	public NonLinearFittingExecutable_n_t0_grid(File file, Params_n_t0_grid p) {
		this.file = file;
		this.p = p;
	}
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
	private double[] matrixToLinear(Matrix m) {
		double[][] d = m.getArray();
		double[] linear = new double[d.length*d[0].length];
		int idx = 0;
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[i].length; j++) {
				linear[idx++] += d[i][j];
			}
		}
		return linear;
	}
	public double[][] readTimeAndAllData(File fName, int startLine, int colTime, int colData, String delimiter) throws IOException {
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

		fis = new FileInputStream(fName);
		s = new Scanner(fis);		
		String line;
		String[] splitLine;
		int numXtals = s.nextInt();
		t0_vol = new double[numXtals][2];
		for(int i = 0; i < numXtals; i++) {
			t0_vol[i][0] = s.nextDouble();
			t0_vol[i][1] = s.nextDouble();
		}
		numLines -= numXtals+2;
		s.nextLine();
		int numData = s.nextLine().split("\t").length;
		for(int i = 0; i < startLine-1; i++) {
			line = s.nextLine();
		}
		
		double[][] data = new double[numLines][numData];
		int idx = 0;
		while(s.hasNextLine()) {
			line = s.nextLine();
			splitLine = line.split(delimiter);
			for(int i = 0; i < splitLine.length; i++) {
				data[idx][i] = Double.valueOf(splitLine[i]);
			}
			idx++;
		}
		return data;
	}
	public static File[] getDataFiles() {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
		chooser.setDialogTitle("Open files to analyze");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
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
	
	private double[][] timeAndData(double[][] timeAndAllData, int timeIdx, int dataIdx) {
		double[][] timeAndData = new double[timeAndAllData.length][2];
		for(int i = 0; i < timeAndAllData.length; i++) {
			timeAndData[i][0] = timeAndAllData[i][timeIdx];
			timeAndData[i][1] = timeAndAllData[i][dataIdx];
		}
		return timeAndData;
	}
	public void run() {
		try {
			analyzeFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void analyzeFile(File file) throws IOException {
		double[][] timeAndAllData = readTimeAndAllData(file, 0, 0, 1, "\t");
		double[][] timeAndData;
		int numData = timeAndAllData[0].length-1;
		//0.268853017
		//3.095198765

		Equation1 e;
		boolean[] isFittable;
		Constraint[] c = null;
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


		if(numData > 2) {
			psAll.print(header);
		}
		for(int i = 0; i < numData; i++) {
			double init_t0, init_kA, vol = 0;
			if(i < t0_vol.length) {
				init_t0 = t0_vol[i][0];
				vol = t0_vol[i][1];
				init_kA = 0.5 * Math.pow(vol, -1./3.);
			} else {
				init_t0 = 10;
				init_kA = .01;
				vol = 10;
			}
			if(vol < 10) {
				continue;
			}
			fosIndividual = new FileOutputStream(analysisFolderPath + fileSeparator + outName + "_" + i + ".analyzed");
			psIndividual = new PrintStream(fosIndividual);
			

			timeAndData = timeAndData(timeAndAllData, 0, i+1);
			
			// fit with kA and t0 vals
			e = new Avrami(new double[] {init_kA, init_t0, p.n_approx});
			isFittable = new boolean[] {p.fit_kA, p.fit_t0, p.fit_n};
			nlf = new NonLinearFitting(timeAndData, e, c, isFittable, p.alphaMin, p.alphaMax);
			double[] t0_n_params = get_t0Params(nlf);

			// get min and max values for t0 and n
			String t0_kA_fit = StringConverter.arrayToTabString(nlf.getParams()) + StringConverter.arrayToTabString(nlf.getStdDev());
			
			// fit with kA, t0, n as parameters
			isFittable = new boolean[] {true, true, true};
			e = new Avrami(new double[] {approx_kA, init_t0, p.n_approx});
			nlf = new NonLinearFitting(timeAndData, e, c, isFittable, p.alphaMin, p.alphaMax);
			int problems = nlf.run();
			String t0_kA_n_fit = "";
			switch(problems) {
			case 0:
				t0_kA_n_fit = StringConverter.arrayToTabString(nlf.getParams()) + StringConverter.arrayToTabString(nlf.getStdDev());
				break;
			case 1:
				t0_kA_n_fit = "\t\t\t\t\t\t";
				break;
			}
			
			// set up the non linear fitting to use given values of n and t0
			isFittable = new boolean[] {true, false, false};
			nlf = new NonLinearFitting(timeAndData, e, c, isFittable, p.alphaMin, p.alphaMax);
			int num_t0, num_n;
			double[][] bfv;
			int t0_idx = 0;
			int n_idx = 0;
			double[] nlfParams, nlfStdDev, nlfCorr, nlfCovar, nlfAll;
			double nlfSumSq, nlfNumDataPoints;
			double n_val, t0_val, kA_val;
			int paramsLength, idx, invIdx;
			boolean converged = false;
			double prev_bfv = 0, cur_bfv=0;
			int iter_idx = 0;
			String bfv_string = "";
			double difference_param = 1.0e-4;
			if(t0_n_params != null) {
				do {
					num_t0 = (int) Math.round(( t0_n_params[1] - t0_n_params[0] ) / t0_n_params[2]);
					num_n = (int) Math.round(( t0_n_params[4] - t0_n_params[3] ) / t0_n_params[5]);
					bfv = new double[num_t0+1][num_n+1];
					t0_idx = 0;
					n_idx = 0;
					for(double t0 = t0_n_params[0]; t0 < t0_n_params[1]; t0+= t0_n_params[2], t0_idx++) {
						for(double n = t0_n_params[3]; n < t0_n_params[4]; n += t0_n_params[5], n_idx++) {
							e.setParams(new double[] {approx_kA, t0, n});
							nlf.setIsFittable(false, 1);
							int fittingProblems = nlf.run();
							switch(fittingProblems) {
							case 0:
								nlfParams = nlf.getParams();
								nlfStdDev = nlf.getStdDev();
								nlfSumSq = nlf.getSumSq();
								nlfNumDataPoints = nlf.getNumDataPoints();
								nlfCorr = matrixToLinear(nlf.getCorr());
								nlfCovar = matrixToLinear(nlf.getCovar());
								/*String params = file.toString() + "\t" ;
								if(i == (numData-1)) {
									params += "bulk\t";
								} else {
									params += i + "\t";
								}
								params += StringConverter.arrayToTabString(nlfParams);
								params += StringConverter.arrayToTabString(nlfStdDev);
								params += nlfSumSq + "\t";
								params += nlfNumDataPoints + "\t";
								if(numData > 1) {
									psAll.println(params);
								}*/
								paramsLength = nlfParams.length + nlfStdDev.length + 1 + 1 + 2 + nlfCorr.length + nlfCovar.length;
								nlfAll = new double[paramsLength];
								idx = 0;
								for(int a = 0; a < nlfParams.length; a++) {
									nlfAll[idx++] = nlfParams[a];
								}
								for(int a = 0; a < nlfStdDev.length; a++) {
									nlfAll[idx++] = nlfStdDev[a];
								}
								nlfAll[idx++] = nlfSumSq;
								nlfAll[idx++] = nlfNumDataPoints;
								nlfAll[idx++] = 1./nlfSumSq;
								//params += nlfAll[idx-1] + "\t";
								invIdx = idx;
								bfv[t0_idx][n_idx]=nlfAll[idx-1];
								for(int a = 0; a < nlfCorr.length; a++) {
									nlfAll[idx++] = nlfCorr[a];
								}
								for(int a = 0; a < nlfCovar.length; a++) {
									nlfAll[idx++] = nlfCovar[a];
								}
	
//								params += StringConverter.arrayToTabString(nlfCorr);
//								params += StringConverter.arrayToTabString(nlfCovar);
//								ps_folderWide.println(params);
//								psIndividual.println(nlf.getParameters());
//								psIndividual.println(nlf.getError());
//								System.out.println("\tColumn " + (i+1) + " of " + numData + " complete  with the following parameters.");
//								System.out.println("\t\tkA: " + e.getParam(0) + " t0: " + e.getParam(1) + " n: " + e.getParam(2));
//								printFit(outName, nlf.getTimeDataFit());
								break;
							case 1:
								//System.out.println("\tColumn " + (i+1) + " of " + numData + " fitting failed with the following parameters.");
								//System.out.println("\t\tkA: " + e.getParam(0) + " t0: " + e.getParam(1) + " n: " + e.getParam(2));
							}
						}
						n_idx = 0;
					}
					t0_idx = 0;
					int[] peakIdx = getMax(bfv);
					kA_val = nlf.getParams()[0];
					n_val = peakIdx[1] * t0_n_params[5] + t0_n_params[3];
					t0_val = peakIdx[0] * t0_n_params[2] + t0_n_params[0];
					cur_bfv = bfv[peakIdx[0]][peakIdx[1]];
					String out = "kA: " + kA_val + "\tn: " + n_val + "\tt0: " + t0_val + "\tbfv: " + cur_bfv;
					System.out.println(out);
					bfv_string += iter_idx + ":\t" + out + "\n";
					double difference = check_bfv(cur_bfv, prev_bfv);
					int minMax = 3;
					int divideBy = 5;
					if(difference < difference_param) { converged = true; }
					else {
						prev_bfv = cur_bfv;
						t0_n_params[0] = t0_val - minMax*t0_n_params[2];
						t0_n_params[1] = t0_val + minMax*t0_n_params[2];
						t0_n_params[2] /= divideBy;
						
						t0_n_params[3] = n_val - minMax*t0_n_params[5];
						t0_n_params[4] = n_val + minMax*t0_n_params[5];
						t0_n_params[5] /= divideBy;
					}
					iter_idx++;
				} while(!converged);
				System.out.println(bfv_string);

				e.setParams(new double[] {kA_val, t0_val, n_val});
				int fittingProblems = nlf.run();
				nlfParams = nlf.getParams();
				nlfStdDev = nlf.getStdDev();
				nlfSumSq = nlf.getSumSq();
				nlfNumDataPoints = nlf.getNumDataPoints();
				nlfCorr = matrixToLinear(nlf.getCorr());
				nlfCovar = matrixToLinear(nlf.getCovar());
				String params = file.toString() + "\t" ;
				if(i == (numData-1)) {
					params += "bulk\t";
				} else {
					params += i + "\t";
				}
				params += StringConverter.arrayToTabString(nlfParams);
				params += StringConverter.arrayToTabString(nlfStdDev);
				params += nlfSumSq + "\t";
				params += nlfNumDataPoints + "\t";
				printToConvergedStream(params);
				printToConvergedStream(t0_kA_fit);
				printToConvergedStream(t0_kA_n_fit);
				if(i < t0_vol.length) {
					printlnToConvergedStream(StringConverter.arrayToTabString(t0_vol[i]));
				} else {
					printlnToConvergedStream("");
				}
			}
			
		}
	}
	private double check_bfv(double cur, double prev) {
		return (cur-prev)/cur;
	}
	private int[] getMax(double[][] bfv) {
		double max = 0;
		int[] coords = new int[2];
		for(int i = 0; i < bfv.length; i++) {
			for(int j = 0; j < bfv[i].length; j++) {
				if(max < bfv[i][j]) {
					coords = new int[] {i, j};
					max = bfv[i][j]; 
				}
			}
		}
		return coords;
	}
	private double[] get_t0Params(NonLinearFitting nlf) {
		int fittingProblems = nlf.run();
		switch(fittingProblems) {
		case 0:
			double[] params = new double[6];
			double[] nlf_params = nlf.getParams();
			double[] nlf_error = nlf.getStdDev();
			double kA = nlf_params[0];
			approx_kA = kA;
			double t0 = nlf_params[1];
			double n = nlf_params[2];
			double t0_half = t0 + (1./kA) * Math.pow(-Math.log(0.5), 1./n);
			double deltaT = t0_half - t0;
			params[0] = t0 - deltaT*p.t0_min_percent;
			if(params[0] < 0) { params[0] = 0; }
			params[1] = t0 + deltaT*p.t0_max_percent;
			if(params[1] > nlf.getTime()[nlf.getTime().length-2]) {
				params[1] = nlf.getTime()[nlf.getTime().length-2];
			}
			params[2] = (params[1] - params[0])/p.t0_num_steps;
			params[3] = p.n_min;
			params[4] = p.n_max;
			params[5] = p.n_step;
			return params;
		}
		return null;
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
	}
	private void initFolderWidePrintStream() throws FileNotFoundException {
		FileOutputStream fos_folderWide = new FileOutputStream(analysisFolderPath + fileSeparator + "folderWide.analyzed");
		ps_folderWide = new PrintStream(fos_folderWide);
		ps_folderWide.print(header);
	}
	private void setHeader() {
		int numFittable = 0;
		String[] vals = {"kA", "t0", "n"};
		header = "file\tcolumn\tkA\tt0\tn\t";
		String err = "";
		if(p.fit_kA) { 
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
		for(int doTwice = 0; doTwice < 2; doTwice++) {
			if(doTwice == 0) { header += "corr "; }
			else if(doTwice == 1) { header += "covar "; }
			for(int i = 0; i < numFittable; i++) {
				for(int j = 0; j < numFittable; j++) {
					header += vals[i] + "-" + vals[j] + "\t";
				}	
			}
		}
		header += "\n";
	}
	public static void main(String[] args) throws IOException {
		NonLinearFittingExecutable_n_t0_grid nlfe = new NonLinearFittingExecutable_n_t0_grid(null, null);

		int nThreads = 6;
		Executor e = Executors.newFixedThreadPool(nThreads);
		
		Params_n_t0_grid p = new Params_n_t0_grid();
		nlfe.p = p;
		nlfe.setHeader();
		
		File[] files = getDataFiles();
		if(files == null) {
			System.exit(0);
		}
		System.out.println("ctrl + c to stop execution.");
		nlfe.createFoldersForOutput(files[0]);
		nlfe.initFolderWidePrintStream();

		FileOutputStream fos_converged = new FileOutputStream("converged.txt");
		NonLinearFittingExecutable_n_t0_grid.ps_converged = new PrintStream(fos_converged);
		
		for(int i = 0; i < files.length; i++) {
			System.out.println("\n\nFile: " + files[i].toString() + " opened.");
			if(files[i].toString().compareTo("C:\\Users\\DHD\\Desktop\\$research\\czx-1 xray kinetics - time-data\\all\\250-60-2.txt") == 0) {
				System.out.println("found it");
			}
			nlfe = new NonLinearFittingExecutable_n_t0_grid(files[i], p);
			nlfe.createFoldersForOutput(files[i]);
			e.execute(nlfe);
			System.out.println("File: " + files[i].toString() + " complete.");
		}
		JOptionPane.showMessageDialog(null, "Complete.", "Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0); 
	}
	private synchronized void printToConvergedStream(String s) {
		ps_converged.print(s);
	}
	private synchronized void printlnToConvergedStream(String s) {
		ps_converged.println(s);
	}
}
class Params_n_t0_grid {
	public boolean fit_n = false;
	public boolean fit_t0 = true;
	public boolean fit_kA = true;
	public double alphaMin = 0;
	public double alphaMax = 0.5;
	public double init_kA = 0.001;
	public double t0_num_steps = 1;
	public double t0_min_percent = .15;
	public double t0_max_percent = .15;
	public double n_min = 1;
	public double n_max = 4;
	public double n_step = .1;
	public double n_approx = 3;
	enum parameters {
		fit_n,
		fit_t0,
		fit_kA,
		alphaMin,
		alphaMax,
		init_kA,
		t0_num_steps,
		t0_min_percent,
		t0_max_percent,
		n_min,
		n_max,
		n_step,
		n_approx
		;
	}
	public Params_n_t0_grid() throws FileNotFoundException {
		File params = new File("params_ n_t0_grid.txt");
		if(!params.exists()) {
			params = getParameterFile();
		} 
		while(!parseFile(params)) { 
			params = getParameterFile();
		}
	}
	
	public File getParameterFile() {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
		chooser.setDialogTitle("Open parameter file");
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
	private boolean parseFile(File file) throws FileNotFoundException {
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
			if(!setParam(key, val, lineIdx)) {
				return false;
			}
			lineIdx++;
		}
		return true;
	}
	private boolean setParam(String key, String val, int lineIdx) {
		switch(parameters.valueOf(key)) {
		case fit_n:
			fit_n = Boolean.valueOf(val);
			return true;
		case fit_t0:
			fit_t0 = Boolean.valueOf(val);
			return true;
		case fit_kA:
			fit_kA = Boolean.valueOf(val);
			return true;
		case alphaMin:
			alphaMin = Double.valueOf(val);
			return true;
		case alphaMax:
			alphaMax = Double.valueOf(val);
			return true;
		case t0_num_steps:
			t0_num_steps = Double.valueOf(val);
			return true;
		case t0_min_percent:
			t0_min_percent = Double.valueOf(val);
			return true;
		case t0_max_percent:
			t0_max_percent = Double.valueOf(val);
			return true;
		case n_min:
			n_min = Double.valueOf(val);
			return true;
		case n_max:
			n_max = Double.valueOf(val);
			return true;
		case n_step:
			n_step = Double.valueOf(val);
			return true;
		case init_kA:
			init_kA = Double.valueOf(val);
			return true;
		case n_approx:
			n_approx = Double.valueOf(val);
			return true;
		default:
			String msg = "The key: " + key + " or option: " + val + " did not parse correctly\n";
			String options = StringConverter.arrayToTabString(parameters.values());
			String line = "\nOn line: " + lineIdx + " of the input file.";
			JOptionPane.showMessageDialog(null, msg + options + line, "Parse error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}
