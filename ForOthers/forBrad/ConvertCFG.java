/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package forBrad;

import io.MyFileInputStream;
import io.MyPrintStream;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ConvertCFG {

	private int startingLine;
	private File[] files;
	private File output;
	private JFileChooser chooser;
	public ConvertCFG(int startingLine) {
		this.startingLine = startingLine; 
		files = getFiles();
		if(files == null) {
			JOptionPane.showMessageDialog(null, "No folder selected, exiting...");
			System.exit(1);
		}
		run();
	}
	
	public void run() {
		MyFileInputStream mfis;
		Scanner s;
		MyPrintStream mps;
		output = new File(files[0].getParent() + File.separator + "edited");
		if(!(output.mkdir() || output.exists())) {
			JOptionPane.showMessageDialog(null, "Cannot make default output folder, please select a folder to output into");
			output = getOutput();
		}
		String fName;
		for(int i = 0; i < files.length; i++) {
			if(files[i].getName().contains(".cfg")) {
				System.out.println("Working on file: " + files[i].getName());
				mfis = new MyFileInputStream(files[i]);
				s = mfis.getScanner();
				
				fName = files[i].getName().substring(0, files[i].getName().length()-4) + "-edit.cfg";
				mps = new MyPrintStream(new File(output + File.separator + fName));
				System.out.println("\t outputting to: " + mps.getFile().getAbsolutePath());
				for(int j = 1; j < startingLine; j++) {
					mps.println(s.nextLine());
				}
				String line;
				double mass = s.nextDouble();
				s.nextLine();
				while(s.hasNextLine()) {
					String name = s.nextLine();
					line = s.nextLine();
					String[] split = line.split(" ");
					while(line.split(" ").length == 3 && s.hasNextLine()) {
						mps.println(mass + " " + name + " " + line + " 0 0 0");
						line = s.nextLine();
					}
					mass = Double.valueOf(line.split(" ")[0]);
				}
			} else {
				if(files[i].isDirectory()) {
					System.out.print("Skipping folder: "); 
				} else {
					System.out.print("Skipping file: ");
				}
				System.out.println(files[i].getName());
			}
		}
		System.out.println("Conversion complete.  Files are located in: " + output.getAbsolutePath());
	}
	
	private File getOutput() {
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int val = chooser.showOpenDialog(null);
		switch(val) {
		case JFileChooser.APPROVE_OPTION:
			return chooser.getSelectedFile();
		case JFileChooser.CANCEL_OPTION:
			JOptionPane.showMessageDialog(null, "Cancelled file selection. Exiting...");
			System.exit(1);
		}
		return null;
	}
	private File[] getFiles() {
		chooser = new JFileChooser(new File("."));
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int val = chooser.showOpenDialog(null);
		switch(val) {
		case JFileChooser.APPROVE_OPTION:
			File directory = chooser.getSelectedFile();
			if(directory.isDirectory()) {
				return directory.listFiles();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		//int startingLine = Integer.valueOf(args[0]);
//		new ConvertCFG(startingLine);
		int val = 14;
		try  {
			val = Integer.valueOf(JOptionPane.showInputDialog("What line does your data start on?"));
		} catch(NumberFormatException e) {
			System.err.println("Input not an integer. Exiting...");
		}
		new ConvertCFG(val);
	}
}
