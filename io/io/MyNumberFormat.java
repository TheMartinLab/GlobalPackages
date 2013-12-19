/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MyNumberFormat {

	public static String format(double val, String format) {
		DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
		return df2.format(val);
	}
}
