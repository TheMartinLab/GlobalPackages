/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.*;
public class RemoveEveryOtherLine implements Runnable {
	private File file;
	public RemoveEveryOtherLine(File aFile) {
		file = aFile;
	}
	@Override
	public void run() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file.toString() + ".removed");
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner s = new Scanner(fis);
		PrintStream ps = new PrintStream(fos);
		String line = "";
		while(s.hasNextLine()) {
			line = s.nextLine();
			s.nextLine();
			ps.println(line);
		}
	}
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		File file = null;
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		} else {
			System.out.println("Open dialog cancelled.\nExiting...");
			System.exit(1);
		}
		RemoveEveryOtherLine reol = new RemoveEveryOtherLine(file);
		reol.run();
	}
}