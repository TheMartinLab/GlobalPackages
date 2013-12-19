/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
import java.io.Serializable;


public class Register implements Serializable {
	private static final long serialVersionUID = -4064356659247722597L;
	private String date;
	private double hours;
	private String note;
	private Activity act;
	public Register(String date, double minutes, String note, Activity act) {
		this.date = date;
		this.hours = minutes;
		this.note = note;
		this.act = act;
	}
	public double getHours() { return hours; }
	public String getDate() { return date; }
	public String getNote() { return note; }
	public double getPlayVal() {
		double value = act.getValue();
		if(value == 0) {
			return 1;
		}
		return value;
	}
	public String toString() { return hours + " hrs\t" + date + "\t" + note; }
}
