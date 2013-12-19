/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package GUI;

import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageWindow extends JComponent {

	private JTextArea messages;
	private JScrollPane scroll;
	private JComponent box;
	public MessageWindow() {
		box = setupMessageWindow();
	}
	private JComponent setupMessageWindow() {
		Box box = Box.createHorizontalBox();
		messages = new JTextArea(15, 50);
		messages.setMargin(new Insets(0, 5, 0, 5));
		scroll = new JScrollPane(messages);
		scroll.setVisible(true);
		scroll.setSize(messages.getSize());
		box.add(scroll);
		box.add(Box.createHorizontalGlue());
		return box;
	}
	/**
	 * Does not print a new line.  Prints only what "msg" is.
	 * @param msg
	 */
	public void message(String msg) {
		messages.append(msg);
		repaint();
	}
	
	public JComponent getComponent() { return box; }
}
