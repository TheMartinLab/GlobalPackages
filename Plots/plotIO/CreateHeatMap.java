/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package plotIO;

import io.PrintToFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import readFile.ReadFile;
import twoDimensional.HeatMap;

public class CreateHeatMap implements Runnable, Observer {

	private double xMin = 5;
	private double xMax = 15.25;
	private double yMin = -6;
	private double yMax = -1.5;
	private int numStepsX = 50;
	private int numStepsY = 50;
	private boolean outputAverage_1d = true;
	private boolean outputAverage_2d = false;
	private boolean outputRaw_1d = false;
	private boolean outputRaw_2d = false;
	private boolean run;
	private int xIdx = 0;
	private int yIdx = 1;
	private int zIdx = 2;
	private int firstRow = 1;
	private int numProcessors = 2;
	private volatile int numThreadsLaunched = 0;
	private volatile int numThreadsFinished = 0;
	private JFileChooser chooser;
	private JFrame frame;
	private Color error, ok;
	private JTextArea messages;
	private String[] columnLabels;
	private JComboBox<String> boxX, boxY, boxZ;
	
	private File[] files;
	public CreateHeatMap() {
		setupUI();
	}
	public CreateHeatMap(File[] files) {
		setupUI();
		this.files = files;
		message("Files loaded:");
		for(int i = 0; i < files.length; i++) {
			message(files[i].getName());
			
		}
	}
	public File[] getFiles() {
		File[] files = null;
		int val = chooser.showOpenDialog(null);
		switch(val) {
		case JFileChooser.APPROVE_OPTION:
			files = chooser.getSelectedFiles();
			break;
		case JFileChooser.CANCEL_OPTION:
			return null;
		}
		return files;
	}
	
	private void initFileChooser() {
		chooser = new JFileChooser(new File("."));
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	}
	
	/* GUI */
	private void setupUI() {
		initFileChooser();
		frame = new JFrame();
		Box boxMain = Box.createVerticalBox();


		boxMain.add(getUIPanels());
		boxMain.add(Box.createVerticalGlue());
		
		frame.add(boxMain, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Heat Map Creation");
		frame.setVisible(true);
		
	}
	private void message(String message) {
		messages.append(message + "\n");
	}
	private Component getUIPanels() {
		
		Box boxV = Box.createVerticalBox();
		
		Box boxV1 = Box.createHorizontalBox();
		boxV1.setBorder(BorderFactory.createTitledBorder("Column selections"));

		JLabel lblX, lblY, lblZ;
		lblX = new JLabel("x index");
		lblY = new JLabel("y index");
		lblZ = new JLabel("z index");
		
		Font f = lblX.getFont();
		lblX.setFont(new Font(f.getName(), Font.BOLD, 18));
		lblY.setFont(new Font(f.getName(), Font.BOLD, 18));
		lblZ.setFont(new Font(f.getName(), Font.BOLD, 18));
		
		boxX = new JComboBox<String>();
		boxY = new JComboBox<String>();
		boxZ = new JComboBox<String>();
		
		Box box1 = Box.createHorizontalBox();
		box1.add(lblX);
		box1.add(Box.createHorizontalStrut(5));
		box1.add(boxX);
		box1.add(Box.createHorizontalGlue());
		
		Box box2 = Box.createHorizontalBox();
		box2.add(lblY);
		box2.add(Box.createHorizontalStrut(5));
		box2.add(boxY);
		box2.add(Box.createHorizontalGlue());

		Box box3 = Box.createHorizontalBox();
		box3.add(lblZ);
		box3.add(Box.createHorizontalStrut(5));
		box3.add(boxZ);
		box3.add(Box.createHorizontalGlue());
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() instanceof JComboBox<?>) {
					JComboBox<String> box = (JComboBox<String>) arg0.getSource();
					try {
						if(box == boxX) {
							xIdx = boxX.getSelectedIndex();
							message("x index set to " + columnLabels[xIdx] + " (column #" + xIdx + ")");
						} else if(box == boxY) {
							yIdx = boxY.getSelectedIndex();
							message("y index set to " + columnLabels[yIdx] + " (column #" + yIdx + ")");
			 				
						} else if(box == boxZ) {
							zIdx = boxZ.getSelectedIndex();
							message("z index set to " + columnLabels[zIdx] + " (column #" + zIdx + ")");
							
						}
					} catch(ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
		};
		
		boxX.addActionListener(al);
		boxY.addActionListener(al);
		boxZ.addActionListener(al);
		
		boxV1.add(box1);
		boxV1.add(Box.createVerticalStrut(5));
		boxV1.add(box2);
		boxV1.add(Box.createVerticalStrut(5));
		boxV1.add(box3);
		boxV1.add(Box.createVerticalStrut(5));
		
		Box boxV2 = Box.createVerticalBox();
		boxV2.setBorder(BorderFactory.createTitledBorder("File Selection"));
		JButton btnGetFiles = new JButton("Select File(s)");
		final JButton btnRun = new JButton("Create Heat Maps");
		messages = new JTextArea(10, 30);
		messages.setLineWrap(true);
		messages.setEditable(false);
		messages.setVisible(true);
		JScrollPane scroll = new JScrollPane(messages);
		scroll.setSize(messages.getSize());
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		btnGetFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				files = getFiles();
				if(files == null) {
					message("No files selected.");
					btnRun.setEnabled(false);
				} else {
					message("Files loaded:");
					for(int i = 0; i < files.length; i++) {
						message(files[i].getName());
					}
					ReadFile rf = new ReadFile();
					rf.setFile(files[0]);
					try {
						rf.read(1);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					columnLabels = rf.getHeader().split("\t");
					
					boxX.removeAllItems();
					boxY.removeAllItems();
					boxZ.removeAllItems();
					
					for(int i = 0; i < columnLabels.length; i++) {
						boxX.addItem(columnLabels[i]);
						boxY.addItem(columnLabels[i]);
						boxZ.addItem(columnLabels[i]);
					}
					frame.repaint();
					frame.pack();
					btnRun.setEnabled(true);
				}
			}
		});
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				run = true;
				if(run) {
					run();
				}
			}
		});

		btnRun.setEnabled(false);
		f = btnRun.getFont();
		btnRun.setFont(new Font(f.getName(), Font.BOLD, 16));
		btnRun.setForeground(Color.blue);

		btnGetFiles.setFont(new Font(f.getName(), Font.BOLD, 16));
		btnGetFiles.setForeground(Color.black);

		boxV2.add(btnGetFiles);
		boxV2.add(Box.createVerticalStrut(5));
		boxV2.add(btnRun);
		boxV2.add(Box.createVerticalStrut(5));
		boxV2.add(scroll);
		boxV2.add(Box.createVerticalGlue());
		
		boxV.add(boxV1);
		boxV.add(Box.createVerticalStrut(10));
		boxV.add(boxV2);
		boxV.add(Box.createVerticalGlue());
		return boxV;
	}
	
	private int parseTextField(JTextField txt) {
		int idx = 0;
		try {
			idx = Integer.parseInt(txt.getText());
		} catch (NumberFormatException nfe) {
			run = false;
			JOptionPane.showMessageDialog(null, "One of the index fields is not an integer.  Please change the highlighted field to an integer.",
					"Index field number format error.", JOptionPane.ERROR_MESSAGE);
		}
		return idx;
	}
	/* ANALYSIS */
	
	private String getIdxSelection() {
		return " -- (" + xIdx + "," + yIdx + "," + zIdx + ")";
	}
	private String getIdxNames() {
		String x = columnLabels[xIdx];
		String y = columnLabels[yIdx];
		String z = columnLabels[zIdx];
		
		return " -- (" + x + "," + y + "," + z + ")";
	}
	public void output(HeatMap hm) {
		File f = hm.getF();
		File out;
		PrintToFile pf;
		if(outputRaw_2d) {
			out = new File(f.getAbsolutePath() + getIdxNames() + " -- raw 2d.heatMap");
			pf = new PrintToFile(hm.getRaw_2d(), out);
			pf.setHeader(columnLabels[xIdx] + "\t" + columnLabels[yIdx] + "\t" + columnLabels[zIdx]);
			pf.print();
			message("raw 2d printed to file for: " + out.getName());
		}
		if(outputRaw_1d) {
			out = new File(f.getAbsolutePath() + getIdxNames() + " -- raw 1d.heatMap");
			pf = new PrintToFile(hm.getRaw_1d(), out);
			pf.setHeader(columnLabels[xIdx] + "\t" + columnLabels[yIdx] + "\t" + columnLabels[zIdx]);
			pf.print();
			message("raw 1d printed to file for: " + out.getName());
		}
		if(outputAverage_2d) {
			out = new File(f.getAbsolutePath() + getIdxNames() + " -- averaged 2d.heatMap");
			pf = new PrintToFile(hm.getAverage_2d(), out);
			pf.setHeader(columnLabels[xIdx] + "\t" + columnLabels[yIdx] + "\t" + columnLabels[zIdx]);
			pf.print();
			message("averaged 2d printed to file for: " + out.getName());
		}
		if(outputAverage_1d) {
			out = new File(f.getAbsolutePath() + getIdxNames() + " -- averaged 1d.heatMap");
			pf = new PrintToFile(hm.getAverage_1d(), out);
			pf.setHeader(columnLabels[xIdx] + "\t" + columnLabels[yIdx] + "\t" + columnLabels[zIdx]);
			pf.print();
			message("averaged 1d printed to file for: " + out.getName());
		}
	}
	public void run() {
		frame.setVisible(true);
		double[][] xyz = null;
		HeatMap hm;
		ExecutorService ex = Executors.newFixedThreadPool(numProcessors);

		String delimiter = "\t";
		Stack<HeatMap> toRun = new Stack<HeatMap>();
		ReadFile rf = new ReadFile(delimiter);
		for(int i = 0; i < files.length; i++) {
			try {
				rf.setFile(files[i]);
				xyz = rf.read(firstRow);
				columnLabels = rf.getHeader().split(delimiter);
			} catch (FileNotFoundException e) {
				numThreadsLaunched--;
				e.printStackTrace();
			}
			hm = new HeatMap(xyz, files[i]);
			hm.setxMin(xMin);
			hm.setyMin(yMin);
			hm.setxMax(xMax);
			hm.setyMax(yMax);
			hm.setNumStepsX(numStepsX);
			hm.setNumStepsY(numStepsY);
			hm.setxIdx(xIdx);
			hm.setyIdx(yIdx);
			hm.setzIdx(zIdx);
			hm.addObserver(this);
			toRun.push(hm);
			numThreadsLaunched++;
		}
		while(!toRun.isEmpty()) {
			ex.execute(toRun.pop());
		}
	}
	
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
		CreateHeatMap chm = new CreateHeatMap();
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof HeatMap) {
			HeatMap hm = (HeatMap) arg0;
			if(arg1 instanceof String) {
				String message = (String) arg1;
				if(message.compareTo(HeatMap.PROCESSING_FINISHED) == 0) {
					output(hm);
				}
			}
		}
		
	}
}
