/*******************************************************************************
 * Copyright (c) 2013 Eric Dill -- eddill@ncsu.edu. North Carolina State University. All rights reserved.
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Eric Dill -- eddill@ncsu.edu - initial API and implementation
 ******************************************************************************/
package forBrad;

import io.MyPrintStream;
import io.ReadFile;
import io.StringConverter;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import readFile.MatrixToXYZ;

public class ConvertMatrixToXYZ {

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File f = chooser.getSelectedFile();
		ReadFile rf;
		try {
			rf = new ReadFile(f, "\t");
			double[][] data = rf.read();
			data = MatrixToXYZ.ConvertMatrixToXYZ(data);
			MyPrintStream mps = new MyPrintStream(new File("converted.txt"));
			for(int i = 0; i < data.length; i++) {
				mps.println(StringConverter.arrayToTabString(data[i]));
			}
			mps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
