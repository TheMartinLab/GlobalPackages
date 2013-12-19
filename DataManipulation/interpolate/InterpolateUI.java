/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package interpolate;

import io.MyPrintStream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import readFile.ReadFile;

import GUI.MessageWindow;

import uiComponents.MyJTextField;

public class InterpolateUI extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 920550675311168951L;

	private JFileChooser opener;
	
	private MessageWindow mw;
	
	private File file;
	
	private double x_min, x_max, x_step;
	
	public InterpolateUI() {
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
		
		Box box2 = Box.createHorizontalBox(),
				box3 = Box.createHorizontalBox(),
				box4 = Box.createHorizontalBox(),
				box5 = Box.createHorizontalBox();
		
		JButton btnOpen, btnRun;
		final MyJTextField txt_x_min = new MyJTextField(MyJTextField.txtFieldType.Double, 10),
				txt_x_max = new MyJTextField(MyJTextField.txtFieldType.Double, 10),
				txt_x_step = new MyJTextField(MyJTextField.txtFieldType.Double, 10);
		
		JLabel lbl_x_min = new JLabel("x min: ");
		JLabel lbl_x_max = new JLabel("x max: ");
		JLabel lbl_x_step = new JLabel("x step size: ");

		int horizontal = 10;
		int vertical = 10;
		
		btnOpen = new JButton("Open file");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int val = opener.showOpenDialog(null);
				switch(val) {
				case JFileChooser.APPROVE_OPTION:
					file = opener.getSelectedFile();
					mw.message("Selected File:\n\t" + file.getName());
					break;
				case JFileChooser.CANCEL_OPTION:
					mw.message("Open dialog cancelled.\n");
					break;
				}
			}
		});
		
		btnRun = new JButton("Interpolate");
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				x_min = (Double) txt_x_min.parse();
				x_max = (Double) txt_x_max.parse();
				x_step = (Double) txt_x_step.parse();
				run();
			}
		});
		
		mw = new MessageWindow();
		
		box2.add(btnOpen);
		box2.add(Box.createHorizontalStrut(horizontal));
		box2.add(btnRun);
		box2.add(Box.createHorizontalGlue());
		
		box3.add(lbl_x_min);
		box3.add(Box.createHorizontalStrut(horizontal));
		box3.add(txt_x_min);
		box3.add(Box.createHorizontalGlue());
		
		box4.add(lbl_x_max);
		box4.add(Box.createHorizontalStrut(horizontal));
		box4.add(txt_x_max);
		box4.add(Box.createHorizontalGlue());
		
		box5.add(lbl_x_step);
		box5.add(Box.createHorizontalStrut(horizontal));
		box5.add(txt_x_step);
		box5.add(Box.createHorizontalGlue());
		
		boxMain.add(box2);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box3);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box4);
		boxMain.add(Box.createVerticalStrut(vertical));
		boxMain.add(box5);
		boxMain.add(Box.createVerticalStrut(vertical));
		
		add(boxMain, BorderLayout.CENTER);
		add(mw.getComponent(), BorderLayout.SOUTH);
	}
	
	public void run() {
		String curOutputFile, extension;
		curOutputFile = file.getAbsolutePath();
		int lastPeriod = curOutputFile.lastIndexOf(".");
		extension = curOutputFile.substring(lastPeriod);
		curOutputFile = curOutputFile.substring(0, lastPeriod) + "-parsed" + extension;
				
		MyPrintStream mps = new MyPrintStream(new File(curOutputFile));
		
		mw.message("Opened file: " + file.getName() + "\n");
	
		double[][] inputData = null;
		try {
			inputData = new ReadFile(file, "\t").read();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			mw.message("Error reading input file. Terminating interpolating routine...");
			return;
		}
		
		Interpolate i = new Interpolate(inputData);
		i.interpolate(x_min, x_max, x_step);
		
		Double[] x = i.getInterpolatedX();
		Double[] y = i.getInterpolatedY();

		mw.message("Saved output to: " + curOutputFile);
		for(int idx = 0; idx < x.length; idx++) {
			mps.println(x[idx] + "\t" + y[idx]);
			mw.message(x[idx] + "\t" + y[idx] + "\n");
		}
		
	}
	
	private void initChooser() {
		opener = new JFileChooser(new File("."));
		opener.setFileSelectionMode(JFileChooser.FILES_ONLY);
		opener.setMultiSelectionEnabled(false);
	}
	
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InterpolateUI();
            }
        });
	}
}
