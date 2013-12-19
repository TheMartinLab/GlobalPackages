/*******************************************************************************
 * Copyright (c) 2013 Eric Dill -- eddill@ncsu.edu. North Carolina State University. All rights reserved.
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Eric Dill -- eddill@ncsu.edu - initial API and implementation
 ******************************************************************************/
package uiComponents;

import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class DataTypeChecker implements FocusListener {

	public final static int INTEGER = 0;
	public final static int DOUBLE = 1;
	public final static int STRING = 2;
	private int dataType;
	private double min, max;
	private boolean enforceBounds = false;
	
	public DataTypeChecker(int dataType) {
		this.dataType = dataType;
		enforceBounds = false;
	}
	
	public DataTypeChecker(int dataType, double min, double max) {
		this.dataType = dataType;
		this.min = min;
		this.max = max;
		enforceBounds = true;
	}
	@Override
	public void focusGained(java.awt.event.FocusEvent e) {
		check(e.getSource());
	}

	@Override
	public void focusLost(java.awt.event.FocusEvent e) {
		check(e.getSource());
	}

	public void check(Object o) {
		JTextField txt;
		if(o instanceof JTextField) {
			txt = (JTextField) o;
			switch(dataType) {
			case INTEGER:
				try {
					int val = Integer.parseInt(txt.getText());
					if(enforceBounds) {
						if(val <= min ) {
							txt.setText("" + (int) min);
						} else if (val >= max && max >= min) {
							txt.setText("" + (int) max);
						}
					}
				}
				catch (NumberFormatException nfe) {
					txt.setText("0");
				}
				break;
			case DOUBLE:
				try {
					double val = Double.parseDouble(txt.getText());
					if(enforceBounds) {
						if(val <= min ) {
							txt.setText(min + "");
						} else if (val >= max) {
							txt.setText(max + "");
						}
					}
				} 
				catch (NumberFormatException nfe) {
					txt.setText("0");
				}
				break;
			case STRING:
				try {
					txt.getText();
				}
				catch (NullPointerException npe) {
					txt.setText("");
				}
			}
		}
	}

}
