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
package watchers;

public class InvalidWatchSelectionException extends Exception {

	private static final long serialVersionUID = -7879307443649050225L;

	/**
	 * @param string
	 */
	public InvalidWatchSelectionException(String string) {
		super(string);
	}
}
