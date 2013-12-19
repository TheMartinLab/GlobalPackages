/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package email;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class SendMailTLS {
	public static void send(String to, String subject, String msg, File[] attachments, final String username, final String password) {
		 
		Properties props = PropertiesFactory.getGmailProperties();

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(to));
			message.setSubject(subject);
			MimeBodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setText(msg);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(msgBodyPart);
			for(int i = 0; attachments != null && i < attachments.length; i++) {
				msgBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachments[i]);
				msgBodyPart.setDataHandler(new DataHandler(source));
				msgBodyPart.setFileName(attachments[i].getAbsolutePath());
				multipart.addBodyPart(msgBodyPart);
			}
			message.setContent(multipart);
			
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	public static void send(String to, String subject, String msg, File[] attachments) {
		send(to, subject, msg, attachments, getFromAccount(), getPassword());
	}
	public static void send(String to, String msg) {
		send(to, "", msg, null, getFromAccount(), getPassword());
	}
	public static String getFromAccount() {
		return fromAccount == null ? defaultAccount : fromAccount;
	}
	
	public static String getPassword() {
		return password == null ? defaultPassword : password;
	}
	
	public static String fromAccount, password;
	private final static String defaultAccount = "chemisist.autosend@gmail.com";
	private final static String defaultPassword = "redwhite00";

	public static void main(String[] args) {
 
		
	}
}