/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package email;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = -3396837848146795259L;
	private String emailAddress;
	
	public Address(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getAddress() { return emailAddress; }
}
