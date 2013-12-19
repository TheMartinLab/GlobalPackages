/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MyFile {
	public static File[] getFiles() {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
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
	public static File getOutputFolder() {
		File f = new File(".");
		JFileChooser chooser = new JFileChooser(f);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			File folder = chooser.getCurrentDirectory();
			return folder;
		} else if(result == JFileChooser.CANCEL_OPTION){
			JOptionPane.showMessageDialog(null, 
					"Open dialog cancelled by user", 
					"Open Dialog Cancelled", 
					JOptionPane.ERROR_MESSAGE
					);
		}
		return null;
	}

	public static boolean makeFile(File f) {
		if(f.exists())
			return true;
		if(f.mkdir())
			return true;
		else
			 makeFile(f.getParentFile());
		
		return makeFile(f);
	}
}
