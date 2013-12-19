/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import io.MyPrintStream;
import io.StringConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Vector;

import readFile.ReadFile;

public class ReadChiFiles {

	private File inFolder;
	private File outFolder;
	private int firstLine = 5;
	public final static String TAB = "\t";
	public final static String SPACE = " ";
	private String outName = "ReadChiFiles -- out.txt";
	private String delimiter = TAB;
	private String rootContains = "";
	private int charIndexOfFileIndex = 34;
	private int indexOfFileIndexAfterSplit = 2;
	private String header = "";
	public ReadChiFiles() {
		// do nothing
	}
	
	private void rename() {
		File[] files = inFolder.listFiles();
		int maxIndexLength = 0;
		for(File file : files) {
			if(file.isFile() && file.getName().contains(rootContains)) {
//				System.out.println("Checking name of file: " + file.getName());
				String[] split = file.getName().split("\\.");
				int indexLen = split[indexOfFileIndexAfterSplit].length();
				if(indexLen > maxIndexLength)
					maxIndexLength = indexLen;
			}
		}
			
		for(File file: files) {
			if(file.isFile() && file.getName().contains(rootContains)) {
				String[] split = file.getName().split("\\.");
				while(split[indexOfFileIndexAfterSplit].length() < maxIndexLength)
					split[indexOfFileIndexAfterSplit] = "0" + split[indexOfFileIndexAfterSplit];
				header += split[indexOfFileIndexAfterSplit] + "\t";
				String newName = split[0];
				for(int i = 1; i < split.length; i++) {
					newName += "." + split[i];
				}
				file.renameTo(new File(file.getParentFile().getAbsoluteFile() + File.separator + newName));
						
			}	
			
		}
	}
	public void run() {
		rename();
		File[] files = inFolder.listFiles();
		double[] q = null;
		Vector<double[]> data = new Vector<double[]>();
		
		for(int i = 0; i < files.length; i++) {
			if(files[i].getName().contains(rootContains)) {
				System.out.println("Reading file " + files[i].getName() + " (" + (i+1) + " / " + (files.length + 1) + ")");
				double[][] file = null;
				try {
					file = new ReadFile(files[i], " ").read(firstLine);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if(q == null) {
					q = new double[file.length];
					for(int j = 0; j < file.length; j++) {
						q[j] = file[j][0];
					}
				}
				double[] toAdd = new double[file.length];
				for(int j = 0; j < file.length; j++) {
					toAdd[j] = file[j][1];
				}
				data.add(toAdd);
			}
		}
		printToFile(q, data);
	}
	public void printToFile(double[] q, Vector<double[]> data) {
		File out = new File(outFolder + File.separator + outName);
		MyPrintStream mps = new MyPrintStream(out);
		mps.println("x axis\t" + header);
		for(int i = 0; i < q.length; i++) {
			if(i%100 == 0)
				System.out.println("Printing row " + i);
			String line = q[i] + "\t";
			for(int j = 0; j < data.size(); j++) {
				line += data.get(j)[i] + "\t";
			}
			mps.println(line);
		}
		
	}
	
	public static void main(String[] args) {
		String outName = "S(q)Output.txt";
//		outName = "chiOutput.txt";
		
		File inFolder = new File("D:\\$research\\Current Research\\PROJECTS\\CZX-1 Crystal Growth Kinetics\\PDF, S(Q), etc\\aps_jan_09\\czx1-10_230-145-#2_90kev_4s_139f");
		File outFolder = new File(inFolder + File.separator + "ReadSqOutput");
		outFolder.mkdirs();
		
		String rootContains = "sq";
		
		int firstLine = 140;
		
		ReadChiFiles rcf = new ReadChiFiles();
		rcf.rootContains = rootContains;
		rcf.outName = outName;
		rcf.inFolder = inFolder;
		rcf.outFolder = outFolder;
		rcf.delimiter = SPACE;
		rcf.firstLine = firstLine;
//		rcf.rename();
		rcf.run();
	}
}
