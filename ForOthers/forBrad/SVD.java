/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package forBrad;

import io.MyPrintStream;
import io.StringConverter;

import jama.Matrix;
import jama.SingularValueDecomposition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import readFile.ReadFile;

import GUI.MessageWindow;

public class SVD extends JFrame implements Observer {

	private File open, openFolder, saveRoot, saveFolder;
	
	private MessageWindow mw;
	public SVD() {
		setupGUI();
	}

	private void setupGUI() {
		mw = new MessageWindow();
		JButton btnLoad = new JButton("Load files: ");
		JButton btnSave =  new JButton("Save to: ");
		JButton btnRun = new JButton("Run SVD");
		Font f = new Font(btnRun.getFont().getName(), Font.BOLD, 20);
		btnRun.setFont(f);
		btnRun.setForeground(Color.BLUE);
		
		final JTextField txtLoad = new JTextField(10);
		final JTextField txtSave = new JTextField(10);

		final JFileChooser chooser = new JFileChooser(new File("."));
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = chooser.showOpenDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					open = chooser.getSelectedFile();
					openFolder = chooser.getCurrentDirectory();
					txtLoad.setText(openFolder.getName() + File.separator + open.getName());
					saveRoot = (new File(openFolder + File.separator + 
							open.getName().substring(0, open.getName().lastIndexOf("."))));
					txtSave.setText(saveRoot.getName() + " -- (U/S/V).txt");
					saveFolder = openFolder;
				}
				
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = chooser.showSaveDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					saveRoot = (chooser.getSelectedFile());
					saveFolder = (chooser.getCurrentDirectory());
					txtSave.setText(saveRoot.getName());
				}
				
			}
		});
		
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(open == null) {
					mw.message("SVD requires data to run. Please select an input file.\n");
				} else {
					run();
				}
			}
		});
		
		Box boxMain = Box.createVerticalBox();
		Box boxLoad = Box.createHorizontalBox();
		Box boxSave = Box.createHorizontalBox();
		Box boxRun = Box.createHorizontalBox();
		
		boxLoad.add(btnLoad);
		boxLoad.add(Box.createHorizontalStrut(5));
		boxLoad.add(txtLoad);
		boxLoad.add(Box.createHorizontalGlue());
		
		boxSave.add(btnSave);
		boxSave.add(Box.createHorizontalStrut(5));
		boxSave.add(txtSave);
		boxSave.add(Box.createHorizontalGlue());
		
		boxRun.add(btnRun);
		boxRun.add(Box.createHorizontalGlue());
		
		boxMain.add(boxLoad);
		boxMain.add(Box.createVerticalStrut(5));
		boxMain.add(boxSave);
		boxMain.add(Box.createVerticalStrut(5));
		boxMain.add(boxRun);
		boxMain.add(Box.createVerticalStrut(5));
		boxMain.add(mw.getComponent());
		
		add(boxMain, BorderLayout.CENTER);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
		pack();
		setVisible(true);
	}
	
	public void run() {
		double[][] data;
		try {
			data = new ReadFile(open, "\t").read();
		} catch (FileNotFoundException e) {
			return;
		}
		double[][] matrixData = new double[data.length][data[0].length-1];
		double val;
		for(int i = 0; i < matrixData.length; i++) {
			for(int j = 0; j < matrixData[0].length; j++) {
				val = data[i][j+1];
				matrixData[i][j] = val;
			}
		}
		Matrix m = new Matrix(matrixData);
		SingularValueDecomposition svd = m.svd();
		
		File outputU = new File(saveFolder + File.separator + saveRoot.getName() + " -- U.txt");
		File outputS1 = new File(saveFolder + File.separator + saveRoot.getName() + " -- S (row).txt");
		File outputS2 = new File(saveFolder + File.separator + saveRoot.getName() + " -- S (diagonal).txt");
		File outputV = new File(saveFolder + File.separator + saveRoot.getName() + " -- V.txt");
		
		double[][] dataU = svd.getU().getArray();
		double[][] dataV = svd.getV().getArray();
		double[] dataS1 = svd.getSingularValues();
		double[][] dataS2 = svd.getS().getArray();
		
		MyPrintStream mpsU = new MyPrintStream(outputU);
		MyPrintStream mpsS1 = new MyPrintStream(outputS1);
		MyPrintStream mpsS2 = new MyPrintStream(outputS2);
		MyPrintStream mpsV = new MyPrintStream(outputV);

		for(int i = 0; i < dataU.length; i++) {
			mpsU.println(StringConverter.arrayToTabString(dataU[i]));
		}
		mpsS1.println(StringConverter.arrayToTabString(dataS1));
		
		for(int i = 0; i < dataS2.length; i++) {
			mpsS2.println(StringConverter.arrayToTabString(dataS2[i]));
		}
		
		for(int i = 0; i < dataV.length; i++) {
			mpsV.println(StringConverter.arrayToTabString(dataV[i]));
		}
		mw.message("Matrices printed to:\n");
		mw.message("left matrix: " + outputU.getAbsolutePath() + "\n");
		mw.message("right matrix: " + outputV.getAbsolutePath() + "\n");
		mw.message("singular values (diagonal matrix): " + outputS2.getAbsolutePath() + "\n");
		mw.message("singular values (row): " + outputS1.getAbsolutePath() + "\n");
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SVD();
            }
        });
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof String) {
			mw.message((String) arg1);
			repaint();
		}
	}
}
