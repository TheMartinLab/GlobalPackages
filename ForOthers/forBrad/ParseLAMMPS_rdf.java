/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package forBrad;

import io.MyFileInputStream;
import io.MyPrintStream;
import io.StringConverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import GUI.MessageWindow;

import uiComponents.MyJTextField;

public class ParseLAMMPS_rdf extends JFrame implements Runnable {
	private static final long serialVersionUID = -6794283650792773520L;

	private JFileChooser opener;
	
	private double[][] outputData;
	
	
	private MessageWindow mw;
	
	private File[] files;
	
	private Integer firstRow, numSteps, column, rowsPerStep;
	
	private Double verticalOffset;
	
	private String delimiter = "\t";
	
	public ParseLAMMPS_rdf() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(500, 500));
		setupAndShowGUI();
		pack();
		setVisible(true);
	}
	
	
	private void setupAndShowGUI() {
		initChooser();
		initUI();
	}
	
	private void initUI() {
		Box boxMain = Box.createVerticalBox();
		
		Box box1 = Box.createHorizontalBox(),
				box2 = Box.createHorizontalBox(),
				box3 = Box.createHorizontalBox(),
				box4 = Box.createHorizontalBox(),
				box5 = Box.createHorizontalBox(),
				box6 = Box.createHorizontalBox(),
				box7 = Box.createHorizontalBox();
		
		JButton btnOpen, btnRun;
		final MyJTextField txtFirstRow = new MyJTextField(MyJTextField.txtFieldType.Integer, 10),
				txtNumSteps = new MyJTextField(MyJTextField.txtFieldType.Integer, 10),
				txtColumn = new MyJTextField(MyJTextField.txtFieldType.Integer, 10),
				txtDelimiter = new MyJTextField(MyJTextField.txtFieldType.String, 10),
				txtRowsPerStep = new MyJTextField(MyJTextField.txtFieldType.Integer, 10),
				txtVerticalOffset = new MyJTextField(MyJTextField.txtFieldType.Double, 10);
		
		JLabel lblRow = new JLabel("First row of data: ");
		JLabel lblSteps = new JLabel("Number of time steps: ");
		JLabel lblColumn = new JLabel("Column to parse: ");
		JLabel lblDelimiter = new JLabel("Delimiter: ");
		JLabel lblRowsPerStep = new JLabel("Rows per time step: ");
		JLabel lblVerticalOffset = new JLabel("Amount to offset each PDF by: ");

		int horizontal = 10;
		int vertical = 10;
		
		btnOpen = new JButton("Open file(s)");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int val = opener.showOpenDialog(null);
				switch(val) {
				case JFileChooser.APPROVE_OPTION:
					files = opener.getSelectedFiles();
					mw.message("Selected Files:\n");
					for(int i = 0; i < files.length; i++) {
						mw.message(files[0].getName() + "\n");
					}
					break;
				case JFileChooser.CANCEL_OPTION:
					mw.message("Open dialog cancelled.\n");
					break;
				}
			}
		});
		
		btnRun = new JButton("Parse");
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstRow = (Integer) txtFirstRow.parse()-1;
				numSteps = (Integer) txtNumSteps.parse();
				column = (Integer) txtColumn.parse()-1;
				delimiter = (String) txtDelimiter.parse();
				rowsPerStep = ((Integer) txtRowsPerStep.parse());
				verticalOffset = (Double) txtVerticalOffset.parse();
				run();
			}
		});
		
		mw = new MessageWindow();
		
		box2.add(btnOpen);
		box2.add(Box.createHorizontalStrut(horizontal));
		box2.add(btnRun);
		box2.add(Box.createHorizontalGlue());
		
		box3.add(lblRow);
		box3.add(Box.createHorizontalStrut(horizontal));
		box3.add(txtFirstRow);
		box3.add(Box.createHorizontalGlue());
		
		box4.add(lblSteps);
		box4.add(Box.createHorizontalStrut(horizontal));
		box4.add(txtNumSteps);
		box4.add(Box.createHorizontalGlue());
		
		box5.add(lblColumn);
		box5.add(Box.createHorizontalStrut(horizontal));
		box5.add(txtColumn);
		box5.add(Box.createHorizontalGlue());
		
		box6.add(lblDelimiter);
		box6.add(Box.createHorizontalStrut(horizontal));
		box6.add(txtDelimiter);
		box6.add(Box.createHorizontalGlue());	
		
		box7.add(lblRowsPerStep);
		box7.add(Box.createHorizontalStrut(horizontal));
		box7.add(txtRowsPerStep);
		box7.add(Box.createHorizontalGlue());
		
		box1.add(lblVerticalOffset);
		box1.add(Box.createHorizontalStrut(horizontal));
		box1.add(txtVerticalOffset);
		box1.add(Box.createHorizontalGlue());
		
		boxMain.add(box2);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box3);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box4);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box5);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box6);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box7);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box1);
		boxMain.add(Box.createVerticalStrut(vertical));
		
		add(boxMain, BorderLayout.CENTER);
		add(mw.getComponent(), BorderLayout.SOUTH);
	}
	
	public void run() {
		String curOutputFile, extension;
		for(int i = 0; i < files.length; i++) {
			curOutputFile = files[i].getAbsolutePath();
			int lastPeriod = curOutputFile.lastIndexOf(".");
			extension = curOutputFile.substring(lastPeriod);
			curOutputFile = curOutputFile.substring(0, lastPeriod) + "-parsed-" + (column+1) + extension;
					
			MyPrintStream mps = new MyPrintStream(new File(curOutputFile), false);
			
			MyFileInputStream mfis = new MyFileInputStream(files[i]);
			Scanner s = mfis.getScanner();
			mw.message("Opened file: " + files[i].getName() + "\n");
			String line;
			String[] vals;
		
			outputData = new double[rowsPerStep][numSteps+1];
			String timeSteps = "";
			
			for(int j = 0; j < firstRow; j++) {
				s.nextLine();
			}
			
			int curStep, numRows;
			
			for(int j = 0; j < numSteps && s.hasNextLine(); j++) {
				curStep = s.nextInt();
				numRows = s.nextInt();
				s.nextLine();
				
				for(int k = 0; k < rowsPerStep; k++) {
					line = s.nextLine();
//					mw.message(line + "\n");
					vals = line.split(delimiter);

					if(j == 0) {
						outputData[k][0] = Double.valueOf(vals[1]);
					}
					if(k == 0) {
						timeSteps += "\t" + curStep;
					}
					outputData[k][j+1] = Double.valueOf(vals[column]) + j * verticalOffset;
				}
			}
			mfis.close();
			
			mps.println(timeSteps);
			
			for(int j = 0; j < outputData.length; j++ ) {
				mps.println(StringConverter.arrayToTabString(outputData[j]));
			}
			mw.message("\tOutput to: " + curOutputFile + "\n");
			mps.close();
		}
		
		
	}
	
	private void initChooser() {
		opener = new JFileChooser(new File("."));
		opener.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		opener.setMultiSelectionEnabled(true);
	}
	
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ParseLAMMPS_rdf();
            }
        });
	}
}
