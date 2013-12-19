/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package calculate;

import geometry.JVector;
import io.MyFileInputStream;
import io.MyPrintStream;
import io.StringConverter;

import java.awt.Point;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JOptionPane;

import calculate.Calibration.parameters;

public class Calibration{
	private String synchrotron, date, calibrant, notes;
	private double x0, y0, distance, wavelength, backgroundScale;
	private int pixel_size;
	private File f;
	public enum parameters {
		/** name of the synchrotron where the data was collected */
		synchrotron,
		/** date that the data was collected */
		date,
		/** name of the calibrant used */
		calibrant,
		/** extra notes about the calibration */
		notes,
		/** x coordinate of the detector center <b>in pixels</b> */
		x,
		/** y coordinate of the detector center <b>in pixels</b> */
		y,
		/** physical size of the detector's pixels <b>in um</b> */
		pixel_size,
		/** distance between sample and detector center <b>in mm</b> */
		distance,
		/** wavelength of incident radiation <b>in angstroms</b> */
		wavelength,
		/** scaling parameter for the background file */
		backgroundScale,
		;
	}
	public Calibration(File f) {
		this.f = f;
		parseFile();
	}
	
	public JVector qToX00(double q) {
		double theta = 2*Math.asin(q * wavelength / 4 / Math.PI);
		double x = Math.tan(theta) * distance * 1000 / pixel_size;
		return new JVector(x, 0, 0);
	}
	public JVector qToPixels(JVector q) {
		JVector q100 = new JVector(1, 0, 0);
		double theta = Math.asin(q100.length() * wavelength / 4 / Math.PI);
		double x = Math.tan(2*theta) * distance * 1000 / pixel_size;
		
		JVector pix = JVector.multiply(q, x);
		pix = JVector.add(pix, new JVector(x0, y0, 0));
		
		return pix;
		
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @return {q, phi}
	 */
	public double[] coordsToQAndPhi(double x, double y) {
		// calculate q
		double theta = Math.atan(((double) pixel_size)/1000 * Math.sqrt(Math.pow(x-x0, 2)+Math.pow(y-y0, 2))/ distance );
		double q = format(4*Math.PI*Math.sin(theta/2.) / wavelength);
		
		// calculate phi
		double phi = Math.atan((y-y0)/(x-x0)) * 180/Math.PI;
		phi = format(phi);
		if((x-x0) < 0) { phi += 180; }
		else if((y-y0) < 0) { phi += 360; }
		
		return new double[] {q, phi};
	}
	public File getFile() { return f; }
	public Calibration(String synchrotron, String date, String calibrant, String notes, double x, 
			double y, int pixel_size, double distance, double wavelength, double backgroundScale) {
		this.synchrotron = synchrotron;
		this.date = date;
		this.calibrant = calibrant;
		this.notes = notes;
		this.x0 = x;
		this.y0 = y;
		this.pixel_size = pixel_size;
		this.distance = distance;
		this.wavelength = wavelength;
		this.backgroundScale = backgroundScale;
		writeToFile(synchrotron + "--" + date + "--" + calibrant + ".calib");
	}
	private void parseFile() {
		MyFileInputStream fis = new MyFileInputStream(f);
		Scanner s = new Scanner(fis.getFileInputStream());
		Scanner lineScanner;
		int lineIdx = 0;
		String key, val, line;
		while(s.hasNextLine()) {
			line = s.nextLine();
			lineScanner = new Scanner(line);
			key = lineScanner.next();
			val = lineScanner.next();
			setParam(key, val, lineIdx);
			lineIdx++;
		}
		fis.close();
	}
	public JVector getDetectorCenter() {
		return new JVector(x0, y0, 0);
	}
	public JVector coordsToQ(Point p) {
		double[] qPhi = coordsToQAndPhi(p.x, p.y);
		
		double qx = qPhi[0] * Math.cos(qPhi[1]);
		
		double qy = qPhi[0] * Math.sin(qPhi[1]);
		
		return new JVector(qx, qy, 0);
	}
	public void setParam(String key, String val, int lineIdx) {
		switch(parameters.valueOf(key)) {
		case synchrotron:
			synchrotron = val;
			break;
		case date:
			date = val;
			break;
		case calibrant:
			calibrant = val;
			break;
		case x:
			x0 = Double.valueOf(val);
			break;
		case y:
			y0 = Double.valueOf(val);
			break;
		case distance:
			distance = Double.valueOf(val);
			break;
		case wavelength:
			wavelength = Double.valueOf(val);
			break;
		case pixel_size:
			pixel_size = Integer.valueOf(val);
			break;
		case notes:
			notes = val;
			break;
		case backgroundScale:
			backgroundScale = Double.valueOf(val);
			break;
		default:
			String msg = "The key: " + key + " or option: " + val + " did not parse correctly\n";
			String options = StringConverter.arrayToTabString(parameters.values());
			String line = "\nOn line: " + lineIdx + " of the input file.";
			JOptionPane.showMessageDialog(null, msg + options + line, "Parse error", JOptionPane.ERROR_MESSAGE);
		}
	}
	public String toString() {
		String s = "";
		s += parameters.synchrotron.toString() + "\t" + synchrotron + "\n";
		s += parameters.date.toString() + "\t" + date + "\n";
		s += parameters.calibrant.toString() + "\t" + calibrant + "\n";
		s += parameters.notes.toString() + "\t" + notes + "\n";
		s += parameters.x.toString() + "\t" + x0 + "\n";
		s += parameters.y.toString() + "\t" + y0 + "\n";
		s += parameters.distance.toString() + "\t" + distance + "\n";
		s += parameters.wavelength.toString() + "\t" + wavelength + "\n";
		s += parameters.backgroundScale.toString() + "\t" + backgroundScale + "\n";
		s += parameters.pixel_size.toString() + "\t" + pixel_size;
		return s;
	}
	public void writeToFile(String fName) {
		String fileSeparator = StringConverter.getSystemFileSeparator();
		f = new File("Calibration" + fileSeparator + fName);
		MyPrintStream mps = new MyPrintStream(f);
		mps.print(toString());
		mps.close();
	}
	public Object getParam(parameters p) {
		switch(p) {
		case synchrotron:
			return synchrotron;
		case date:
			return date;
		case calibrant:
			return calibrant;
		case x:
			return x0;
		case y:
			return y0;
		case distance:
			return distance;
		case wavelength:
			return wavelength;
		case pixel_size:
			return pixel_size;
		case notes:
			return notes;
		case backgroundScale:
			return backgroundScale;
		default:
			throw new RuntimeException(p.name() + " is not usable");
		}
	}

	public String getSynchrotron() { return synchrotron; }
	public void setSynchrotron(String synchrotron) { this.synchrotron = synchrotron; }
	public String getDate() { return date; }
	public void setDate(String date) { this.date = date; }
	public String getCalibrant() { return calibrant; }
	public void setCalibrant(String calibrant) { this.calibrant = calibrant; }
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	public double getX0() { return x0; }
	public void setX0(double x0) { this.x0 = x0; }
	public double getY0() { return y0; }
	public void setY0(double y0) { this.y0 = y0; }
	public double getDistance() { return distance; }
	public void setDistance(double distance) { this.distance = distance; }
	public double getWavelength() { return wavelength; }
	public void setWavelength(double wavelength) { this.wavelength = wavelength; }
	public double getBackgroundScale() { return backgroundScale; }
	public void setBackgroundScale(double backgroundScale) { this.backgroundScale = backgroundScale; }
	public int getPixel_size() { return pixel_size; }
	public void setPixel_size(int pixel_size) { this.pixel_size = pixel_size; }
	public File getF() { return f; }
	public void setF(File f) { this.f = f; }
	
	/** STATIC METHODS */
	

	/**
	 * 
	 * @param x
	 * @param y
	 * @return {q, phi}
	 */
	
	public static double[] coordsToQAndPhi(double x, double y, double pixel_size, double x0, 
			double y0, double wavelength, double distance) {
		// calculate q
		double theta = Math.atan(((double) pixel_size)/1000 * Math.sqrt(Math.pow(x-x0, 2)+Math.pow(y-y0, 2))/ distance );
		double q = format(4*Math.PI*Math.sin(theta/2.) / wavelength);
		
		// calculate phi
		double phi = Math.atan((y-y0)/(x-x0)) * 180/Math.PI;
		phi = format(phi);
		if((x-x0) < 0) { phi += 180; }
		else if((y-y0) < 0) { phi += 360; }
		
		return new double[] {q, phi};
	}
	
	public static double format(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		double val = 0;
		try {
			val = Double.valueOf(twoDForm.format(d));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			System.out.println("Number: " + d + " cannot be parsed.");
		}
		return val;
	}
}