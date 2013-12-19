/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package uiComponents;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JTextField;

public class MyJTextField extends JTextField {
	
	private Color originalBackground, errorColor;
	
	public enum txtFieldType { String, Integer, Double };
	
	public enum txtFieldCondition {
		greaterThan, lessThan, between, equalTo, notEqualTo;
		double val1, val2;
		
		public void setVal1(double val) { val1 = val; }
		public void setVal2(double val) { val2 = val; }
		
		public boolean isMet(double val) {
			switch(this) {
			case between:
				return (val > val1 && val < val2);
			case equalTo:
				return (val == val1);
			case greaterThan:
				return (val > val1);
			case lessThan:
				return (val < val1);
			case notEqualTo:
				return (val != val1);
			default:
				return false;
			}
		}
		public String getCondition() {
			switch(this) {
			case between:
				return val1 + " < input < " + val2;
			case equalTo:
				return "input = " + val1;
			case greaterThan:
				return "input > " + val1;
			case lessThan:
				return "input < " + val1;
			case notEqualTo:
				return "input != " + val1;
			default:
				return "";
			}
		}
	}
	
	private Vector<txtFieldCondition> conditions;
	private txtFieldType type;
	
	public MyJTextField(txtFieldType type, int width, txtFieldCondition condition) {
		super(width);
		this.type = type;
		conditions = new Vector<txtFieldCondition>();
		conditions.add(condition);
		initFocusListener();
	}
	
	public MyJTextField(txtFieldType type, int width, Vector<txtFieldCondition> conditions) {
		super(width);
		this.type = type;
		this.conditions = conditions;
		initFocusListener();
	}
	public MyJTextField(txtFieldType type, int width) {
		super(width);
		conditions = new Vector<txtFieldCondition>();
		this.type = type;
		initFocusListener();
	}
	
	public Object parse() {
		switch(type) {
		case String:
			return getText();
		case Integer:
			return Integer.valueOf(getText());
		case Double:
			return Double.valueOf(getText());
		}
		return null;
	}
	
	private void initFocusListener() {
		errorColor = Color.yellow;
		originalBackground = getBackground();
		
		final MyJTextField txt = this;
		FocusListener fl = new FocusListener(){
			public void focusGained(FocusEvent arg0) {
				txt.selectAll();
			}
			
			public void focusLost(FocusEvent arg0) {
				boolean invalid = false;
				switch(type) {
				case Double:
					try {
						double val = Double.valueOf(getText());
						for(int i = 0; i < conditions.size(); i++) {
							if(!conditions.get(i).isMet(val)) {
								invalid = true;
								setText(conditions.get(i).getCondition());
								break;
							}
						}
					} catch(NumberFormatException nfe) {
						setText("Must be a number");
						invalid = true;
					}
					break;
				case Integer:
					try {
						int val = Integer.valueOf(getText());
						for(int i = 0; i < conditions.size(); i++) {
							if(!conditions.get(i).isMet(val)) {
								invalid = true;
								setText(conditions.get(i).getCondition());
								break;
							}
						}
					} catch(NumberFormatException nfe) {
						setText("Must be an integer");
						invalid = true;
					}
					break;
				case String:
					invalid = false;
					break;
				default:
					invalid = false;
					break;
				}
				if(invalid) {
					setBackground(errorColor);
				} else {
					setBackground(originalBackground);
				}
			}
		};
		addFocusListener(fl);
	}
	public void removeConditions() { conditions = new Vector<txtFieldCondition>(); }
	
	public void addCondition(txtFieldCondition condition) { conditions.add(condition); }
	
	public txtFieldCondition[] getConditions() {
		txtFieldCondition[] conditions = new txtFieldCondition[this.conditions.size()];
		conditions = this.conditions.toArray(conditions);
		return conditions;
	}
}
