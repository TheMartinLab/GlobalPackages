/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package email;

import java.io.Serializable;

public class Account implements Serializable {

	private static final long serialVersionUID = 9128943199829797845L;
	private String s1, s2;
	
	public Account(String emailAddress, String password) {
		s1 = emailAddress;
		s2 = password;
	}
	
	public String getName() { return s1; }
	public String getPw() { return s2; }
}
