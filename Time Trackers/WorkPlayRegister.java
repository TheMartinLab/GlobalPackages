/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WorkPlayRegister  {

	private File fileLog = new File("WorkPlayLog.obj");
	private File fileActivity = new File("WorkPlayActivities.obj");
	
	private JPanel pnlMain, pnlLog;
	
	private JButton btnRegisterWork, btnRegisterExercise, btnRegisterPlay;
	
	private JTextField txtRegisterWork, txtRegisterExercise, txtRegisterPlay;
	private JTextField txtRegisterWorkNote, txtRegisterExerciseNote, txtRegisterPlayNote;
	private JTextField txtTotalWorkHours, txtTotalExerciseHours, txtWorkPlayHours, txtExercisePlayHours;
	private JTextField txtPlayHoursUsed, txtPlayHoursAccrued, txtPlayHoursAvailable;
	private JTextField txtTotalPlayHours;
	
	private JTextArea txtWorkLog, txtExerciseLog, txtPlayLog;
	
	private JComboBox<Activity> boxWork, boxExercise, boxPlay;
	
	private Vector<Register> work, exercise, play;
	private Vector<Activity> workActivities, exerciseActivities, playActivities; 
	
	private final static int LABEL_SPACER = 10; 
	
	public WorkPlayRegister() {
		readLogsFromFile();
		readActivitiesFromFile();
		setUpUI();
		updateTextFields();
	}
	private void makeNewLogs() {
		work = new Vector<Register>();
		exercise = new Vector<Register>();
		play = new Vector<Register>();
		writeLogsToFile();
	}
	private void makeNewActivities() {
		workActivities = new Vector<Activity>();
		workActivities.add(new Activity("Research", .167));
		workActivities.add(new Activity("Writing", .25));
		workActivities.add(new Activity("Grading", .75));
		exerciseActivities = new Vector<Activity>();
		exerciseActivities.add(new Activity("Walking", .5));
		exerciseActivities.add(new Activity("Biking", .75));
		exerciseActivities.add(new Activity("Gym", 1.25));
		playActivities = new Vector<Activity>();
		playActivities.add(new Activity("Gaming", 1));
		playActivities.add(new Activity("TV", .75));
		
		writeActivitiesToFile();
	}
	private void writeLogsToFile() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(fileLog);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos.writeObject(work);
			oos.writeObject(exercise);
			oos.writeObject(play);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void writeActivitiesToFile() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(fileActivity);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			oos.writeObject(workActivities);
			oos.writeObject(exerciseActivities);
			oos.writeObject(playActivities);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setupComboBoxes() {
		boxWork = new JComboBox<Activity>(workActivities);
		boxExercise = new JComboBox<Activity>(exerciseActivities);
		boxPlay = new JComboBox<Activity>(playActivities);
	}
	@SuppressWarnings("unchecked")
	private void readLogsFromFile() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		boolean readSuccess = true;
		try {
			fis = new FileInputStream(fileLog);
			ois = new ObjectInputStream(fis);
		} catch (FileNotFoundException e) {
			makeNewLogs();
			readSuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(readSuccess) {
			Object o1 = null, o2 = null, o3 = null;
			try {
				o1 = ois.readObject();
				o2 = ois.readObject();
				o3 = ois.readObject();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(o1 instanceof Vector<?>) {
				work = (Vector<Register>) o1;	
			}
			if(o2 instanceof Vector<?>) {
				exercise = (Vector<Register>) o2;	
			}
			if(o3 instanceof Vector<?>) {
				play = (Vector<Register>) o3;	
			}
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void readActivitiesFromFile() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		boolean readSuccess = true;
		try {
			fis = new FileInputStream(fileActivity);
			ois = new ObjectInputStream(fis);
		} catch (FileNotFoundException e) {
			makeNewActivities();
			readSuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(readSuccess) {
			Object o1 = null, o2 = null, o3 = null;
			try {
				o1 = ois.readObject();
				o2 = ois.readObject();
				o3 = ois.readObject();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(o1 instanceof Vector<?>) {
				workActivities = (Vector<Activity>) o1;	
			}
			if(o2 instanceof Vector<?>) {
				exerciseActivities = (Vector<Activity>) o2;	
			}
			if(o3 instanceof Vector<?>) {
				playActivities = (Vector<Activity>) o3;	
			}
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setUpUI() {
		JFrame frame = new JFrame();
		JTabbedPane tabs = new JTabbedPane();
		
		setUpMainPanel();
		setupLogPanel();
		tabs.addTab("Main", pnlMain);
		tabs.addTab("Logs", pnlLog);
		frame.add(tabs);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void setupLogPanel() {
		pnlLog = new JPanel();
		pnlLog.setLayout(new BoxLayout(pnlLog, BoxLayout.Y_AXIS));
		int numRows = 8, numCols = 25;
		
		pnlLog.add(new JLabel("Work Log"));
		txtWorkLog = new JTextArea(numRows, numCols);
		txtWorkLog.setEditable(false);
		pnlLog.add(txtWorkLog);
		
		pnlLog.add(new JLabel("Exercise Log"));
		txtExerciseLog = new JTextArea(numRows, numCols);
		txtExerciseLog.setEditable(false);
		pnlLog.add(txtExerciseLog);
		
		pnlLog.add(new JLabel("Play Log"));
		txtPlayLog = new JTextArea(numRows, numCols);
		txtPlayLog.setEditable(false);
		pnlLog.add(txtPlayLog);
		
	}
	private void setUpMainPanel() {
		pnlMain = new JPanel();
		pnlMain.setLayout(new GridLayout(3, 1));
		
		
		pnlMain.add(getRegisterPanel());
		pnlMain.add(getSummaryPanel());
	}
	private void updateTextFields() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3);
		
		double[] totalHours = getTotalHours();
		txtTotalWorkHours.setText(nf.format(totalHours[0]));
		txtTotalExerciseHours.setText(nf.format(totalHours[1]));
		txtTotalPlayHours.setText(nf.format(totalHours[2]));
		
		double[] hours = getHours();
		txtWorkPlayHours.setText(nf.format(hours[0]));
		txtExercisePlayHours.setText(nf.format(hours[1]));
		txtPlayHoursUsed.setText(nf.format(hours[2]));
		
		txtPlayHoursAccrued.setText(nf.format((hours[0] + hours[1])));
		
		txtPlayHoursAvailable.setText(nf.format((hours[0] + hours[1] - hours[2])));
		
		writeLogsToFile();
		writeActivitiesToFile();
		
		String[] logs = getLogStrings();
		txtWorkLog.setText(logs[0]);
		txtExerciseLog.setText(logs[1]);
		txtPlayLog.setText(logs[2]);
	}
	private double[] getHours() {
		double[] hours = new double[3];
		Register r;
		for(int i = 0; i < work.size(); i++) {
			r = work.get(i);
			hours[0] += r.getHours() * r.getPlayVal();
		}
		for(int i = 0; i < exercise.size(); i++) {
			r = exercise.get(i);
			hours[1] += r.getHours() * r.getPlayVal();
		}
		for(int i = 0; i < play.size(); i++) {
			r = play.get(i);
			hours[2] += r.getHours() * r.getPlayVal();
		}
		return hours;
	}
	private String[] getLogStrings() {
		String[] logs = new String[3];
		logs[0] = getLogString(work);
		logs[1] = getLogString(exercise);
		logs[2] = getLogString(play);
		return logs;
	}
	private String getLogString(Vector<Register> which) {
		String log = "";
		for(int i = 0; i < which.size(); i++) {
			log += which.get(i).toString() + "\n";
		}
		return log;
	}
	private double[] getTotalHours() {
		double[] hours = new double[3];
		hours[0] = getHours(work);
		hours[1] = getHours(exercise);
		hours[2] = getHours(play);
		return hours;
	}
	private double getHours(Vector<Register> which) {
		double hours = 0;
		for(int i = 0; i < which.size(); i++) {
			hours += which.get(i).getHours();
		}
		return hours;
	}
	private JPanel getSummaryPanel() {
		JPanel pnlSummary = getBoxLayoutPanel(BoxLayout.Y_AXIS);
		
		JPanel pnlWork = getBoxLayoutPanel(BoxLayout.X_AXIS);
		txtTotalWorkHours = new JTextField(10);
		txtTotalWorkHours.setMaximumSize(txtTotalWorkHours.getPreferredSize());
		txtTotalWorkHours.setEditable(false);
		pnlWork.add(new JLabel("Total work hours: "));
		pnlWork.add(txtTotalWorkHours);
		pnlWork.add(new JLabel(" ==> accrued play hours "));
		txtWorkPlayHours = new JTextField(10);
		txtWorkPlayHours.setMaximumSize(txtWorkPlayHours.getPreferredSize());
		txtWorkPlayHours.setEditable(false);
		pnlWork.add(txtWorkPlayHours);
		
		JPanel pnlExercise = getBoxLayoutPanel(BoxLayout.X_AXIS);
		txtTotalExerciseHours = new JTextField(10);
		txtTotalExerciseHours.setMaximumSize(txtTotalExerciseHours.getPreferredSize());
		txtTotalExerciseHours.setEditable(false);
		pnlExercise.add(new JLabel("Total Exercise hours: "));
		pnlExercise.add(txtTotalExerciseHours);
		pnlExercise.add(new JLabel(" ==> accrued play hours "));
		txtExercisePlayHours = new JTextField(10);
		txtExercisePlayHours.setMaximumSize(txtExercisePlayHours.getPreferredSize());
		txtExercisePlayHours.setEditable(false);
		pnlExercise.add(txtExercisePlayHours);
		
		JPanel pnlPlay = getBoxLayoutPanel(BoxLayout.X_AXIS);
		pnlPlay.add(new JLabel("Total hours played: " ));
		txtTotalPlayHours = new JTextField(10);
		txtTotalPlayHours.setMaximumSize(txtTotalPlayHours.getPreferredSize());
		txtTotalPlayHours.setEditable(false);
		pnlPlay.add(txtTotalPlayHours);
		
		JPanel pnlUsed = getBoxLayoutPanel(BoxLayout.X_AXIS);
		txtPlayHoursUsed = new JTextField(10);
		txtPlayHoursUsed.setMaximumSize(txtPlayHoursUsed.getPreferredSize());
		txtPlayHoursUsed.setEditable(false);
		pnlUsed.add(new JLabel("Effective Play hours used: "));
		pnlUsed.add(txtPlayHoursUsed);
		
		JPanel pnlEarned = getBoxLayoutPanel(BoxLayout.X_AXIS);
		txtPlayHoursAccrued = new JTextField(10);
		txtPlayHoursAccrued.setMaximumSize(txtPlayHoursAccrued.getPreferredSize());
		txtPlayHoursAccrued.setEditable(false);
		pnlEarned.add(new JLabel("Earned Play hours: "));
		pnlEarned.add(txtPlayHoursAccrued);
	
		JPanel pnlAvailable = getBoxLayoutPanel(BoxLayout.X_AXIS);
		txtPlayHoursAvailable = new JTextField(10);
		txtPlayHoursAvailable.setMaximumSize(txtPlayHoursAvailable.getPreferredSize());
		txtPlayHoursAvailable.setEditable(false);
		pnlAvailable.add(new JLabel("Available Play hours: "));
		pnlAvailable.add(txtPlayHoursAvailable);
		
		pnlSummary.add(pnlWork);
		pnlSummary.add(pnlExercise);
		pnlSummary.add(pnlPlay);
		pnlSummary.add(pnlUsed);
		pnlSummary.add(pnlEarned);
		pnlSummary.add(pnlAvailable);
		
		return pnlSummary;
	}
	private JPanel getRegisterPanel() {
		setupComboBoxes();
		JPanel pnlRegisters = getBoxLayoutPanel(BoxLayout.Y_AXIS);
		JPanel registerWork = getBoxLayoutPanel(BoxLayout.X_AXIS);
		btnRegisterWork = new JButton("Register Work Hours");
		btnRegisterWork.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String val = txtRegisterWork.getText();
				if(val.compareTo("") != 0) {
					int actWorkIdx = boxWork.getSelectedIndex();
					Activity actWork = workActivities.get(actWorkIdx);
					double hoursWorked = Double.valueOf(val);
					String date = getDate();
					String note = txtRegisterWorkNote.getText();
					if(note.compareTo("") == 0) { note = ""; }
					work.add(new Register(date, hoursWorked, note, actWork));
					clearTextField(txtRegisterWork);
					clearTextField(txtRegisterWorkNote);
					updateTextFields();
				}
			}
			
		});
		txtRegisterWork = new JTextField(10);
		txtRegisterWork.setMaximumSize(txtRegisterWork.getPreferredSize());
		txtRegisterWorkNote = new JTextField(10);
		txtRegisterWorkNote.setMaximumSize(txtRegisterWorkNote.getPreferredSize());
		registerWork.add(btnRegisterWork);
		registerWork.add(getSpacer(LABEL_SPACER));
		registerWork.add(boxWork);
		registerWork.add(getSpacer(LABEL_SPACER));
		registerWork.add(txtRegisterWork);
		registerWork.add(getLabel("hours"));
		registerWork.add(getSpacer(LABEL_SPACER));
		registerWork.add(txtRegisterWorkNote);
		registerWork.add(getLabel("note"));
		
		JPanel registerExercise = getBoxLayoutPanel(BoxLayout.X_AXIS);
		btnRegisterExercise = new JButton("Register Exercise Hours");
		btnRegisterExercise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String val = txtRegisterExercise.getText();
				if(val.compareTo("") != 0) {
					int actExerciseIdx = boxExercise.getSelectedIndex();
					Activity actExercise = exerciseActivities.get(actExerciseIdx);
					double hoursExercised = Double.valueOf(val);
					String date = getDate();
					String note;
					if((note = txtRegisterExerciseNote.getText()) == null) {
						note = "";
					}
					exercise.add(new Register(date, hoursExercised, note, actExercise));
					clearTextField(txtRegisterExercise);
					clearTextField(txtRegisterExerciseNote);
					updateTextFields();
				}
			}
			
		});
		txtRegisterExercise = new JTextField(10);
		txtRegisterExercise.setMaximumSize(txtRegisterExercise.getPreferredSize());
		txtRegisterExerciseNote = new JTextField(10);
		txtRegisterExerciseNote.setMaximumSize(txtRegisterExerciseNote.getPreferredSize());
		registerExercise.add(btnRegisterExercise);
		registerExercise.add(getSpacer(LABEL_SPACER));
		registerExercise.add(boxExercise);
		registerExercise.add(getSpacer(LABEL_SPACER));
		registerExercise.add(txtRegisterExercise);
		registerExercise.add(getLabel("hours"));
		registerExercise.add(getSpacer(LABEL_SPACER));
		registerExercise.add(txtRegisterExerciseNote);
		registerExercise.add(getLabel("note"));
		
		JPanel registerPlay = getBoxLayoutPanel(BoxLayout.X_AXIS);
		btnRegisterPlay = new JButton("Register Play Hours");
		btnRegisterPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String val = txtRegisterPlay.getText();
				if(val.compareTo("") != 0) {
					int actPlayIdx = boxPlay.getSelectedIndex();
					Activity actPlay = playActivities.get(actPlayIdx);
					double hoursPlayed = Double.valueOf(val);
					String date = getDate();
					String note;
					if((note = txtRegisterPlayNote.getText()) == null) {
						note = "";
					}
					play.add(new Register(date, hoursPlayed, note, actPlay));
					clearTextField(txtRegisterPlay);
					clearTextField(txtRegisterPlayNote);
					updateTextFields();
				}
			}
			
		});
		txtRegisterPlay = new JTextField(10);
		txtRegisterPlay.setMaximumSize(txtRegisterPlay.getPreferredSize());
		txtRegisterPlayNote = new JTextField(10);
		txtRegisterPlayNote.setMaximumSize(txtRegisterPlayNote.getPreferredSize());
		registerPlay.add(btnRegisterPlay);
		registerPlay.add(getSpacer(LABEL_SPACER));
		registerPlay.add(boxPlay);
		registerPlay.add(getSpacer(LABEL_SPACER));
		registerPlay.add(txtRegisterPlay);
		registerPlay.add(getLabel("hours"));
		registerPlay.add(getSpacer(LABEL_SPACER));
		registerPlay.add(txtRegisterPlayNote);
		registerPlay.add(getLabel("note"));
		
		pnlRegisters.add(registerWork);
		pnlRegisters.add(registerExercise);
		pnlRegisters.add(registerPlay);
		
		return pnlRegisters;
	}
	private void clearTextField(JTextField txt) {
		txt.setText("");
	}
	private JLabel getLabel(String label) {
		return new JLabel(label);
	}
	private JLabel getSpacer(int width) {
		String label = "";
		for(int i = 0; i < width; i++) {
			label += " ";
		}
		return new JLabel(label);
	}
	private String getDate() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		String date = hour + ":" + minute + "." + second + "-" + month + "/" + day + "/" + year + "/";
		
		return date;
	}
	private JPanel getBoxLayoutPanel(int boxLayout) {
		JPanel pnlTemp;
		pnlTemp = new JPanel();
		pnlTemp.setLayout(new BoxLayout(pnlTemp, boxLayout));
		return pnlTemp;
	}
	public static void main(String[] args) {
		new WorkPlayRegister();
	}
}