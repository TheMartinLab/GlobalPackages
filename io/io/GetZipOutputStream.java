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
/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;

public class GetZipOutputStream {

	private FileOutputStream fos;
	private ZipOutputStream zos;
	public GetZipOutputStream(FileOutputStream fos) {
		this.fos = fos;
		zos = new ZipOutputStream(fos);
	}
	
	public ZipOutputStream getZipOutputStream() { return zos; }
	
	
}
