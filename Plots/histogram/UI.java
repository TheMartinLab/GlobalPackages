/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package histogram;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;


public class UI extends JFrame {

	private JPanel pnlMain;
	private JTabbedPane tabMain;
	
	private JTextField txt_user_min_x_2d, txt_user_max_x_2d, txt_user_min_y_2d, txt_user_max_y_2d, txt_bins_x_2d, txt_bins_y_2d,
		txt_auto_min_x_2d, txt_auto_max_x_2d, txt_auto_min_y_2d, txt_auto_max_y_2d,
		txt_open_file_histo_1d, txt_save_file_histo_1d, txt_open_file_histo_2d, txt_save_file_histo_2d,
		txt_x_coord, txt_y_coord, txt_xy_intensity;
	private JCheckBox box_min_x_2d, box_max_x_2d, box_min_y_2d, box_max_y_2d;
	private JToggleButton tog_log_2d;
	private File file_open_histo_1d, file_save_histo_1d,
		file_open_histo_2d, file_save_data_histo_2d, file_save_pic_histo_2d;
	
	private boolean bool_open_histo_2d = false, bool_save_histo_2d = false;
	private boolean bool_open_histo_1d = false, bool_save_histo_1d = false;
	private boolean bool_run_histo_1d = false, bool_run_histo_2d = false;
	
	private Histogram_1d histo_1d;
	private Histogram_2d histo_2d;
	
	private JButton btn_run_histo_1d, btn_run_histo_2d;
	
	private JFrame imageFrame;
	private MyImagePanel pnlImage;
	private int width = 500, height = 500;

	private ImageClickListener imageClickListener;
	private JButton btnHelp;
	private String strHelp;
	public UI() {
		setup();
	}
	private void setup() {
		setupImageFrame();
		pnlMain = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
		tabMain = new JTabbedPane();
		tabMain.addTab("2d Histogram", setup2d());
		btnHelp = setupHelpButton();
		pnlMain.add(btnHelp);
		pnlMain.add(tabMain);
		pnlMain.add(setupImageFrame());
		pnlMain.add(setupImageClickInfo());
		add(pnlMain, BorderLayout.CENTER);
		setSize(1500, 1000);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	private JButton setupHelpButton() {
		btnHelp = new JButton("Help");
		strHelp = "* Input file format should be two tab delimited columns of x, y data points.";
		strHelp +="\n* The program will automatically determine the min and max x and y values, but you can select different ones if you so desire.";
		strHelp += "\n* After the file is successfully loaded, make sure all the parameters are what you would like, including the number of bins in each dimension.";
		strHelp += "\n* Then click the \"Create 2d Histogram\" button and then click the \"Display 2d Histogram\" button.";
		strHelp += "\n* Every time a parameter is changed, you need to click the create and then display buttons again, in that order.";
		strHelp += "\n* Click on the histogram image and then x- and y- coordinates will be displayed along with the count of data points at that coordinate below the histogram.";
		strHelp += "\n* The button labelled \"Save 2d Histogram data\" will output your data in three columnar tab-delimited format, column 1 is x, column 2 is y and column 3 is the count";
		strHelp += "\n* The button labelled \"Save 2d Histogram as Picture\" will output the visible image below as a .png";
		strHelp += "\n* For any other issues, email me at eddill@ncsu.edu";
		btnHelp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showMessage(strHelp, "Help dialog.", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		return btnHelp;
	}
	private JPanel setupImageFrame() {
		pnlImage = new MyImagePanel(width, height);
		pnlImage.createTestImage(width, height);
		imageClickListener = new ImageClickListener();
		pnlImage.addMouseListener(imageClickListener);
		return pnlImage;
	}
	
	private JPanel setupImageClickInfo() {
		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
		txt_x_coord = new JTextField(15);
		txt_y_coord = new JTextField(15);
		txt_xy_intensity = new JTextField(15);
		
		pnl.add(new JLabel("x: "));
		pnl.add(txt_x_coord);
		pnl.add(new JLabel("y: "));
		pnl.add(txt_y_coord);
		pnl.add(new JLabel("#: "));
		pnl.add(txt_xy_intensity);
		
		pnl.setMaximumSize(pnl.getPreferredSize());
		return pnl;
	}
	
	private JPanel setup1d() {
		JPanel pnl1d = new JPanel();
		
		return pnl1d;
	}
	
	private JPanel setup2d() {
		JPanel pnl2d = new JPanel();
		pnl2d.setLayout(new GridLayout(10, 4));
		
		txt_user_min_x_2d = new JTextField(5);
		txt_user_max_x_2d = new JTextField(5);
		txt_user_min_y_2d = new JTextField(5);
		txt_user_max_y_2d = new JTextField(5);
		
		txt_user_min_x_2d.setEditable(false);
		txt_user_max_x_2d.setEditable(false);
		txt_user_min_y_2d.setEditable(false);
		txt_user_max_y_2d.setEditable(false);
		
		txt_auto_min_x_2d = new JTextField(5); 
		txt_auto_max_x_2d = new JTextField(5);
		txt_auto_min_y_2d = new JTextField(5);
		txt_auto_max_y_2d = new JTextField(5);
		
		txt_auto_min_x_2d.setEditable(false); 
		txt_auto_max_x_2d.setEditable(false); 
		txt_auto_min_y_2d.setEditable(false); 
		txt_auto_max_y_2d.setEditable(false); 
		
		txt_bins_x_2d = new JTextField(5);
		txt_bins_y_2d = new JTextField(5);
		txt_open_file_histo_2d = new JTextField();
		txt_open_file_histo_2d.setEditable(false);
		txt_save_file_histo_2d = new JTextField();
		txt_save_file_histo_2d.setEditable(false);
		
		box_min_x_2d = new JCheckBox();
		box_min_x_2d.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean checked = box_min_x_2d.isSelected();
				if(checked) {
					txt_user_min_x_2d.setText(txt_auto_min_x_2d.getText());
					txt_user_min_x_2d.setEditable(false);
				} else {
					txt_user_min_x_2d.setEditable(true);
				}
			}
		});
		box_max_x_2d = new JCheckBox();
		box_max_x_2d.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean checked = box_max_x_2d.isSelected();
				if(checked) {
					txt_user_max_x_2d.setText(txt_auto_max_x_2d.getText());
					txt_user_max_x_2d.setEditable(false);
				} else {
					txt_user_max_x_2d.setEditable(true);
				}
			}
		});
		box_min_y_2d = new JCheckBox();
		box_min_y_2d.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean checked = box_min_y_2d.isSelected();
				if(checked) {
					txt_user_min_y_2d.setText(txt_auto_min_y_2d.getText());
					txt_user_min_y_2d.setEditable(false);
				} else {
					txt_user_min_y_2d.setEditable(true);
				}
			}
		});
		box_max_y_2d = new JCheckBox();
		box_max_y_2d.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean checked = box_max_y_2d.isSelected();
				if(checked) {
					txt_user_max_y_2d.setText(txt_auto_max_y_2d.getText());
					txt_user_max_y_2d.setEditable(false);
				} else {
					txt_user_max_y_2d.setEditable(true);
				}
			}
		});
		
		box_min_x_2d.setSelected(true);
		box_max_x_2d.setSelected(true);
		box_min_y_2d.setSelected(true);
		box_max_y_2d.setSelected(true);
		
		pnl2d.add(new JLabel());
		pnl2d.add(new JLabel("User values"));
		pnl2d.add(new JLabel("Automatic values"));
		pnl2d.add(new JLabel("Automatically determine?"));
		
		JLabel lbl = new JLabel("Minimum x value: ");
		pnl2d.add(lbl);
		pnl2d.add(txt_user_min_x_2d);
		pnl2d.add(txt_auto_min_x_2d);
		pnl2d.add(box_min_x_2d);
		
		
		lbl = new JLabel("Maximum x value: ");
		pnl2d.add(lbl);
		pnl2d.add(txt_user_max_x_2d);
		pnl2d.add(txt_auto_max_x_2d);
		pnl2d.add(box_max_x_2d);
		
		lbl = new JLabel("Minimum y value: ");
		pnl2d.add(lbl);
		pnl2d.add(txt_user_min_y_2d);
		pnl2d.add(txt_auto_min_y_2d);
		pnl2d.add(box_min_y_2d);
		
		lbl = new JLabel("Maximum y value: ");
		pnl2d.add(lbl);
		pnl2d.add(txt_user_max_y_2d);
		pnl2d.add(txt_auto_max_y_2d);
		pnl2d.add(box_max_y_2d);
		
		lbl = new JLabel("Number of bins in x: ");
		pnl2d.add(lbl);
		pnl2d.add(txt_bins_x_2d);
		pnl2d.add(new JLabel());
		pnl2d.add(new JLabel());
		
		
		lbl = new JLabel("Number of bins in y: ");
		pnl2d.add(lbl);
		pnl2d.add(txt_bins_y_2d);
		pnl2d.add(new JLabel());
		pnl2d.add(new JLabel());
		final JFileChooser chooser = new JFileChooser();
		JButton btnOpenFile = new JButton("Open 2d histogram");
		btnOpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.setDialogTitle("Open 2d histogram data");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					file_open_histo_2d = chooser.getSelectedFile();
					txt_open_file_histo_2d.setText(file_open_histo_2d.toString());
					bool_open_histo_2d = true;
					txt_open_file_histo_2d.setToolTipText(txt_open_file_histo_2d.getText());
					
					file_save_data_histo_2d = new File(file_open_histo_2d.toString() + ".histo");
					txt_save_file_histo_2d.setText(file_save_data_histo_2d.toString());
					bool_save_histo_2d = true;
					txt_save_file_histo_2d.setToolTipText(txt_save_file_histo_2d.getText());
					try {
						histo_2d = new Histogram_2d(file_open_histo_2d);
					} catch (FileNotFoundException e1) {
						showMessage("2d histogram file not openable, please select another",
								"File Access Error", JOptionPane.ERROR_MESSAGE);
					}
					update2DText();
				} else {
					showMessage("Open dialog cancelled by user.", "Cancel message", JOptionPane.INFORMATION_MESSAGE);
					bool_open_histo_2d = false;
				}
			}
		});
		pnl2d.add(btnOpenFile);
		pnl2d.add(txt_open_file_histo_2d);
		
		JButton btnSaveFile = new JButton("Set save file");
		btnSaveFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Choose save file for 2d histogram data");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				int result = chooser.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					file_save_data_histo_2d = chooser.getSelectedFile();
					txt_save_file_histo_2d.setText(file_save_data_histo_2d.toString());
					bool_save_histo_2d = true;
					txt_save_file_histo_2d.setToolTipText(txt_save_file_histo_2d.getText());
				} else {
					JOptionPane.showMessageDialog(null, "Open dialog cancelled by user.", "Cancel message",
							JOptionPane.INFORMATION_MESSAGE);
					bool_save_histo_2d = false;
				}
			}
		});
		pnl2d.add(btnSaveFile);
		pnl2d.add(txt_save_file_histo_2d);
		
		tog_log_2d = new JToggleButton("Plot logarithmically");
		pnl2d.add(tog_log_2d);
		pnl2d.add(new JLabel());
		pnl2d.add(new JLabel());
		pnl2d.add(new JLabel());
		
		JButton btnCreate = new JButton("Create 2d Histogram");
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!box_min_x_2d.isSelected()) { histo_2d.set_bin_min_x(Double.valueOf(txt_user_min_x_2d.getText())); }
				if(!box_max_x_2d.isSelected()) { histo_2d.set_bin_max_x(Double.valueOf(txt_user_max_x_2d.getText())); }
				if(!box_min_y_2d.isSelected()) { histo_2d.set_bin_min_y(Double.valueOf(txt_user_min_y_2d.getText())); }
				if(!box_max_y_2d.isSelected()) { histo_2d.set_bin_max_y(Double.valueOf(txt_user_max_y_2d.getText())); }
				histo_2d.set_bin_num_x(Integer.valueOf(txt_bins_x_2d.getText()));
				histo_2d.set_bin_num_y(Integer.valueOf(txt_bins_y_2d.getText()));
				histo_2d.createHistogram();
			}
		});
		JButton btnDisplay = new JButton("Display 2d Histogram");
		btnDisplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double[][] xy = histo_2d.getHisto();
				double x_step = histo_2d.get_bin_step_x();
				double y_step = histo_2d.get_bin_step_y();
				double x_min = histo_2d.get_bin_min_x();
				double y_min = histo_2d.get_bin_min_y();
				double x_max = histo_2d.get_bin_max_x();
				double y_max = histo_2d.get_bin_max_y();
				boolean logPlot = tog_log_2d.isSelected();
				pnlImage.create2dHistogram(xy, x_step, x_min, x_max, y_step, y_min, y_max, logPlot);
			}
		});
		JButton btnSaveData = new JButton("Save 2d Histogram data");
		btnSaveData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					histo_2d.printFile(file_save_data_histo_2d);
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Save file not accessible.", "File access error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JButton btnSavePicture= new JButton("Save 2d Histogram as Picture");
		btnSavePicture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				file_save_pic_histo_2d = new File(file_open_histo_2d.toString() + ".png");
				JFileChooser chooser = new JFileChooser(file_save_pic_histo_2d);
				chooser.setSelectedFile(file_save_pic_histo_2d);
				chooser.setDialogTitle("Choose save file for 2d histogram picture");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					file_save_pic_histo_2d = chooser.getSelectedFile();
					try {
					    BufferedImage bi = pnlImage.getBufferedImage();
					    ImageIO.write(bi, "png", chooser.getSelectedFile());
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(null, "Save file not accessible.", "File access error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Save dialog cancelled by user.", "Cancel message",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		pnl2d.add(btnCreate);
		pnl2d.add(btnDisplay);
		pnl2d.add(btnSaveData); 
		pnl2d.add(btnSavePicture);
		return pnl2d;
	}
	
	private void update2DText() {
		double min_x = histo_2d.get_bin_min_x();
		double min_y = histo_2d.get_bin_min_y();
		double max_x = histo_2d.get_bin_max_x();
		double max_y = histo_2d.get_bin_max_y();
		
		txt_auto_min_x_2d.setText(min_x + "");
		txt_auto_min_x_2d.setToolTipText(txt_auto_min_x_2d.getText());
		
		txt_auto_min_y_2d.setText(min_y + "");
		txt_auto_min_y_2d.setToolTipText(txt_auto_min_y_2d.getText());
		
		txt_auto_max_x_2d.setText(max_x + "");
		txt_auto_max_x_2d.setToolTipText(txt_auto_max_x_2d.getText());
		
		txt_auto_max_y_2d.setText(max_y + "");
		txt_auto_max_y_2d.setToolTipText(txt_auto_max_y_2d.getText());
		
		//txt_bins_x_2d.setText(histo_2d.get_bin_num_x() + "");
		//txt_bins_y_2d.setText(histo_2d.get_bin_num_y() + "");
		
		if(box_min_x_2d.isSelected()) {
			txt_user_min_x_2d.setText(min_x + "");
			txt_user_min_x_2d.setToolTipText(txt_auto_min_x_2d.getText());
		}
		if(box_max_x_2d.isSelected()) {
			txt_user_max_x_2d.setText(max_x + "");
			txt_user_max_x_2d.setToolTipText(txt_auto_max_x_2d.getText());
		}
		if(box_min_y_2d.isSelected()) {
			txt_user_min_y_2d.setText(min_y + "");
			txt_user_min_y_2d.setToolTipText(txt_auto_min_y_2d.getText());
		}
		if(box_max_y_2d.isSelected()) {
			txt_user_max_y_2d.setText(max_y + "");
			txt_user_max_y_2d.setToolTipText(txt_auto_max_y_2d.getText());
		}
	}
	private final static int HISTO_2D = 2;
	private final static int HISTO_1D = 1;
	private boolean checkOpenClose(int whichHistoTab) {
		switch(whichHistoTab) {
		case HISTO_1D:
			if(bool_open_histo_1d && bool_save_histo_1d) {
				return true;
			} else if(!bool_open_histo_1d) {
				showMessage("Select a file message", "Select a 1d histogram file to open.", JOptionPane.ERROR_MESSAGE);
				return false;
			} else if(!bool_save_histo_1d) {
				showMessage("Select a file message", "Select a file to save the 1d histogram to.", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		case HISTO_2D:
			if(bool_open_histo_2d && bool_save_histo_2d) {
				return true;
			} else if(!bool_open_histo_2d) {
				showMessage("Select a file message", "Select a 1d histogram file to open.", JOptionPane.ERROR_MESSAGE);
				return false;
			} else if(!bool_save_histo_2d) {
				showMessage("Select a file message", "Select a file to save the 1d histogram to.", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return false;
	}
	
	private void showMessage(String msg, String title, int msgType) {
		JOptionPane.showMessageDialog(null, msg, title, msgType);
	}
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
		new UI();
	}
	
	class ImageClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Point p = arg0.getPoint();
			if(pnlImage != null && histo_2d != null) {
				p.x = (int) ((double) p.x / (double) pnlImage.getDisplayWidth() * histo_2d.get_bin_num_x());
				p.y = (int) ((double) p.y / (double) pnlImage.getDisplayHeight() * histo_2d.get_bin_num_y());
				System.out.println(p);
				double[] xyI = histo_2d.get_xyI(p.x, p.y);
				txt_x_coord.setText(xyI[0] + "");
				txt_y_coord.setText(xyI[1] + "");
				txt_xy_intensity.setText(xyI[2] + "");
			}
		}
	}
}
