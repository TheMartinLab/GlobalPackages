/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package email;

import java.util.Properties;

public class PropertiesFactory {

	public static Properties getGmailProperties() {
		Properties gmail = new Properties();
		gmail.put("mail.smtp.auth", "true");
		gmail.put("mail.smtp.starttls.enable", "true");
		gmail.put("mail.smtp.host", "smtp.gmail.com");
		gmail.put("mail.smtp.port", "587");
		return gmail;
	}
}
