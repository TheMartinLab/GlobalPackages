/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package twoDimensional;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CreateHeatMapGUI extends JFrame {

	private JFileChooser chooser;
	private JTextField txtX, txtY, txtZ;
	private JFrame frame;
	
	public CreateHeatMapGUI() {
		
	}
	
	private void initFileChooser() {
		chooser = new JFileChooser(new File("."));
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	}
	private void init() {
		initFileChooser();
		setupUI();
	}
	private void setupUI() {
		Box boxMain = Box.createHorizontalBox();
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( arg0.getSource() instanceof JTextField) {
					((JTextField) arg0.getSource()).selectAll();
				}
			}
		};
		
		boxMain.add(getColumnSelections(al));
		boxMain.add(Box.createHorizontalStrut(10));
		boxMain.add(getLimitsSelections(al));
		boxMain.add(Box.createHorizontalStrut(10));
		boxMain.add(Box.createHorizontalGlue());
		
		add(boxMain, BorderLayout.CENTER);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Heat Map Creation");
		
	}
	private Component getLimitsSelections(ActionListener al) {
		
	}
	private Component getColumnSelections(ActionListener al) {
		Box boxV = Box.createHorizontalBox();
		boxV.setBorder(BorderFactory.createTitledBorder("Column selections"));

		txtX = new JTextField(5);
		txtY = new JTextField(5);
		txtZ = new JTextField(5);

		txtX.addActionListener(al);
		txtY.addActionListener(al);
		txtZ.addActionListener(al);
		
		Box box1 = Box.createHorizontalBox();
		box1.add(new JLabel("x index"));
		box1.add(Box.createHorizontalStrut(5));
		box1.add(txtX);
		box1.add(Box.createHorizontalGlue());
		
		Box box2 = Box.createHorizontalBox();
		box2.add(new JLabel("y index"));
		box2.add(Box.createHorizontalStrut(5));
		box2.add(txtY);
		box2.add(Box.createHorizontalGlue());

		Box box3 = Box.createHorizontalBox();
		box3.add(new JLabel("z index"));
		box3.add(Box.createHorizontalStrut(5));
		box3.add(txtZ);
		box3.add(Box.createHorizontalGlue());
		

		boxV.add(box1);
		boxV.add(Box.createVerticalStrut(5));
		boxV.add(box2);
		boxV.add(Box.createVerticalStrut(5));
		boxV.add(box3);
		boxV.add(Box.createVerticalStrut(5));
		
		return boxV;
	}
	public File[] getFiles() {
		File[] files = null;
		int val = chooser.showOpenDialog(null);
		switch(val) {
		case JFileChooser.APPROVE_OPTION:
			files = chooser.getSelectedFiles();
			break;
		case JFileChooser.CANCEL_OPTION:
			int returnVal = JOptionPane.showConfirmDialog(null, "File selection cancelled.  (yes) Retry file selection or (no) End program.", "File selection cancelled window.", 
					JOptionPane.YES_NO_OPTION);
			switch(returnVal) {
			case JOptionPane.YES_OPTION:
				return getFiles();
			case JOptionPane.NO_OPTION:
				System.exit(1);
			default:
				System.exit(1);
			}
		}
		return files;
	}
}
