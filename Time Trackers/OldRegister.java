/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */

public class OldRegister {
	private static final long serialVersionUID = -4064356659247722597L;
	private String date;
	private double minutes;
	public OldRegister(String date, double minutes) {
		this.date = date;
		this.minutes = minutes;
	}
	public double getMinutes() { return minutes; }
	public String getDate() { return date; }
	public String toString() { return minutes + " hrs\t" + date; }
}