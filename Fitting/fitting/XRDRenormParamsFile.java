/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package fitting;

import fitting.Params2.parameters;
import io.MyFileInputStream;
import io.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

public class XRDRenormParamsFile {
	
	enum parameters {
		run_file,
		lower_start,
		lower_finish,
		upper_start,
		upper_finish,
		tau,
		k,
		;
	}
	private String[] colLabels;
	private Vector<XRDXtalRunParameters> params;
	
	private XRDXtalRunParameters activeParams;
	
	public XRDRenormParamsFile(File file){
		parseFile(file);
		printToConsole();
	}
	private String[] getColLabels(File file) {
		MyFileInputStream fis = new MyFileInputStream(file);
		Scanner s = new Scanner(fis.getFileInputStream());
		String[] labels = s.nextLine().split("\t");
		s.close();
		fis.close();
		return labels;
	}
	private void parseFile(File file){
		colLabels = getColLabels(file);
		
		MyFileInputStream fis = new MyFileInputStream(file);
		Scanner s = new Scanner(fis.getFileInputStream());

		// skip the column names line
		s.nextLine();
		
		params = new Vector<XRDXtalRunParameters>();
		XRDXtalRunParameters run;
		
		String[] row;
		
		int lineIdx = 0;
		
		while(s.hasNextLine()) {
			lineIdx++;
			row = s.nextLine().split("\t");
			run = new XRDXtalRunParameters();
			for(int i = 0; i < row.length; i++) {
				setParam(run, colLabels[i], row[i], lineIdx);
			}
			
			params.add(run);
		}
		
		s.close();
		fis.close();
		
	}
	private void setParam(XRDXtalRunParameters run, String key, String val, int lineIdx) {
		switch(parameters.valueOf(key)) {
		case run_file:
			run.run_file = val;
			break;
		case lower_start:
			run.lower_start = Integer.valueOf(val);
			break;
		case lower_finish:
			run.lower_finish = Integer.valueOf(val);
			break;
		case upper_start:
			run.upper_start = Integer.valueOf(val);
			break;
		case upper_finish:
			run.upper_finish = Integer.valueOf(val);
			break;
		case k:
			run.k = Double.valueOf(val);
			break;
		case tau:
			run.tau = Double.valueOf(val);
			break;
		default:
			String msg = "The key: " + key + " or option: " + val + " did not parse correctly\n";
			String options = StringConverter.arrayToTabString(parameters.values());
			String line = "\nOn line: " + lineIdx + " of the input file.";
			JOptionPane.showMessageDialog(null, msg + options + line, "Parse error", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void printToConsole() {
		System.out.println(StringConverter.arrayToTabString(colLabels));
		for(int i = 0; i < params.size(); i++) {
			System.out.println(StringConverter.arrayToTabString(params.elementAt(i).getArr()));	
		}
	}
	

	public void setActiveParamSet(File name) {
		String fName = name.getName();
		for(int i = 0; i < params.size(); i++) {
			activeParams = params.get(i);
			if(activeParams.run_file.compareTo(fName) == 0)
				break;
		}
	}
	
	public String getRunName() { return activeParams.run_file; }
	public int[] getLowerBounds() { return new int[] {activeParams.lower_start, activeParams.lower_finish}; }
	public int[] getUpperBounds() { return new int[] {activeParams.upper_start, activeParams.upper_finish};}
	public double getTau() { return activeParams.tau; }
	public double getK() { return activeParams.k; }	
	
	
	class XRDXtalRunParameters {
	
		private String run_file;
		private int lower_start, lower_finish, upper_start, upper_finish;
		private double tau, k;
		
		public XRDXtalRunParameters() {
			
		}
		
		public String toString() {
			String s = "";
			s += parameters.run_file.toString() + "\t\t\t" + run_file + "\n";
			s += parameters.lower_start.toString() + "\t\t\t" + lower_start + "\n";
			s += parameters.lower_finish.toString() + "\t\t\t" + lower_finish + "\n";
			s += parameters.upper_start.toString() + "\t\t" + upper_start + "\n";
			s += parameters.upper_finish.toString() + "\t\t" + upper_finish + "\n";
			s += parameters.tau.toString() + "\t\t\t" + tau + "\n";
			s += parameters.k.toString() + "\t\t\t" + k + "\n";
			return s;
		}
		public String[] getArr() {
			return new String[] {
				run_file,
				lower_start + "",
				lower_finish + "",
				upper_start + "",
				upper_finish + "",
				tau + "",
				k + "",
			};
		}
	}
}