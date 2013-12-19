/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
import java.io.Serializable;


public class Activity implements Serializable {

	private static final long serialVersionUID = -2547498196818019338L;
	private double gameValue;
	private String name;
	
	public Activity(String name, double gameValue) {
		this.gameValue = gameValue;
		this.name = name;
	}
	
	public String getName() { return name; }
	public double getValue() { return gameValue; }
	
	public String toString() { return name + ": " + gameValue; }
}
