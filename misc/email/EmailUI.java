/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package email;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class EmailUI extends JFrame {

	private Account acct = new Account("MajesticLandscapesINC@gmail.com", "crews1963");
	//private Account acct = new Account("eddill@ncsu.edu", "redwhite09");
	private int height = 400;
	private int width = 400;
	
	private JTextField txtAddressFrom, txtPassword, txtAddressTo, txtSubject;
	private JTextArea txtMessage;
	private File attachment = new File("params.txt");
	private JLabel getSpacer() {
		return new JLabel("     ");
	}
	
	public EmailUI() {
		setSize(height, width);
		setVisible(true);
		setTitle("Send an Email");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(getMainPanel());
		pack();
	}
	
	private JPanel getMainPanel() {
		JPanel pnlMain = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
		
		pnlMain.add(getFromPanel());
		pnlMain.add(getToPanel());
		
		JButton sendButton = new JButton("Send email");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SendMailTLS.send(txtAddressTo.getText(), txtSubject.getText(), 
						txtMessage.getText(), new File[] {attachment}, 
						txtAddressFrom.getText(), txtPassword.getText());
				JOptionPane.showMessageDialog(null, "Email sent to client.");

				SendMailTLS.send(txtAddressFrom.getText(), "[copy] " + txtSubject.getText(), 
						txtMessage.getText(), new File[] {attachment}, 
						txtAddressFrom.getText(), txtPassword.getText());
				JOptionPane.showMessageDialog(null, "Email sent to self.");
			}
		});
		pnlMain.add(sendButton);
		return pnlMain;
	}
	private JPanel getFromPanel() {
		JPanel pnlFrom = new JPanel();
		pnlFrom.setLayout(new BoxLayout(pnlFrom, BoxLayout.Y_AXIS));
		pnlFrom.setBorder(BorderFactory.createTitledBorder("From"));
		JPanel pnlAddressFrom = new JPanel();
		pnlAddressFrom.setLayout(new BoxLayout(pnlAddressFrom, BoxLayout.X_AXIS));
		pnlAddressFrom.add(new JLabel("Your Email Address: "));
		pnlAddressFrom.add(getSpacer());
		txtAddressFrom = new JTextField(30);
		txtAddressFrom.setText(acct.getName());
		pnlAddressFrom.add(txtAddressFrom);
		
		JPanel pnlPasswordFrom = new JPanel();
		pnlPasswordFrom.setLayout(new BoxLayout(pnlPasswordFrom, BoxLayout.X_AXIS));
		
		pnlPasswordFrom.add(new JLabel("Your Email Password: "));
		
		pnlPasswordFrom.add(getSpacer());
		
		txtPassword = new JTextField(15);
		txtPassword.setText(acct.getPw());
		pnlPasswordFrom.add(txtPassword);
		pnlPasswordFrom.add(getSpacer());
		
		final JToggleButton btnPw = new JToggleButton("Hide password?");
		btnPw.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(btnPw.isSelected()) {
					txtPassword.setVisible(false);
				} else {
					txtPassword.setVisible(true);
				}
			} 
		});
		btnPw.setSelected(false);
		pnlPasswordFrom.add(getSpacer());
		pnlPasswordFrom.add(btnPw);
		
		pnlFrom.add(pnlAddressFrom);
		pnlFrom.add(pnlPasswordFrom);
		
		return pnlFrom;
	}
	private JPanel getToPanel() {
		JPanel pnlTo = new JPanel();
		pnlTo.setBorder(BorderFactory.createTitledBorder("To"));
		pnlTo.setLayout(new BoxLayout(pnlTo, BoxLayout.Y_AXIS));
		
		JPanel pnlAddressTo = new JPanel();
		pnlAddressTo.setLayout(new BoxLayout(pnlAddressTo, BoxLayout.X_AXIS));
		pnlAddressTo.add(new JLabel("To: "));
		pnlAddressTo.add(getSpacer());
		
		txtAddressTo = new JTextField(30);
		pnlAddressTo.add(txtAddressTo);
		
		JPanel pnlSubject = new JPanel();
		pnlSubject.setLayout(new BoxLayout(pnlSubject, BoxLayout.X_AXIS));
		pnlSubject.add(new JLabel("Email Subject: "));
		pnlSubject.add(getSpacer());
		txtSubject = new JTextField(50);
		txtSubject.setText("Bill from Majestic Landscapes Inc.");
		pnlSubject.add(txtSubject);
		
		JPanel pnlMessage = new JPanel();
		pnlMessage.setBorder(BorderFactory.createTitledBorder("Message"));
		txtMessage = new JTextArea(10, 50);
		txtMessage.setText("Thank you for your business!  Attached is a bill for services rendered.\n\nDavid Crews\nMajestic Landscapes Inc.\n(919) 630-2392");
		pnlMessage.add(txtMessage);
		
		JPanel pnlAttachment = new JPanel();
		pnlAttachment.setLayout(new BoxLayout(pnlAttachment, BoxLayout.X_AXIS));
		JButton btnSelectAttachment = new JButton("Select Attachment");
		final JButton btnRemoveAttachment = new JButton("Remove Attachment");
		final JLabel lblFileName = new JLabel();
		final JButton btnViewAttachment = new JButton("View attached bill.");
		btnSelectAttachment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					attachment = chooser.getSelectedFile();
					lblFileName.setText(attachment.getName());
					btnViewAttachment.setEnabled(true);
					btnRemoveAttachment.setEnabled(true);
		        } else {
					JOptionPane.showMessageDialog(null, "Bill selection cancelled. No bill is currently attached.");
		        }
				
			}
		});
		
		pnlAttachment.add(btnSelectAttachment);
		pnlAttachment.add(getSpacer());
		pnlAttachment.add(btnViewAttachment);
		pnlAttachment.add(getSpacer());
		pnlAttachment.add(btnRemoveAttachment);

		btnViewAttachment.setEnabled(false);
		btnViewAttachment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(attachment != null) {
					openAttachment();
				} else {
					JOptionPane.showMessageDialog(null, "Bill selection cancelled. No bill is currently attached.");
				}
			}
		});
		btnRemoveAttachment.setEnabled(false);
		btnRemoveAttachment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attachment = null;
				btnRemoveAttachment.setEnabled(false);
				btnViewAttachment.setEnabled(false);
				lblFileName.setText("");
			}
		});
		JPanel pnlAttachmentName = new JPanel();
		pnlAttachmentName.setLayout(new BoxLayout(pnlAttachmentName, BoxLayout.X_AXIS));
		pnlAttachmentName.add(new JLabel("Attachment file name:"));
		pnlAttachmentName.add(getSpacer());
		pnlAttachmentName.add(lblFileName);
		
		pnlTo.add(pnlAddressTo);
		pnlTo.add(pnlSubject);
		pnlTo.add(pnlMessage);
		pnlTo.add(pnlAttachment);
		pnlTo.add(pnlAttachmentName);
		
		return pnlTo;
	}
	public static void main(String[] args) {
		new EmailUI();
	}
	
	private void openAttachment() {
		try {
			if (attachment.exists()) {
	 
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(attachment);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}
	 
			} else {
				System.out.println("File is not exists!");
			}
	 
			System.out.println("Done");
	 
		  } catch (Exception ex) {
			ex.printStackTrace();
		  }
	}

}
