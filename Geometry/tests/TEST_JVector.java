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
package tests;

import geometry.JVector;

public class TEST_JVector {

	public static void main(String[] args) {
		System.out.println(JVector.cross(new JVector(0, 1, -1), new JVector(-1, 0, 0)));
		
		JVector[][] axes = JVector.axes111U;
		for(int i = 0; i < axes.length; i++) {
			for(int j = 0; j < axes[0].length; j++) {
				System.out.print(axes[i][j].toString() + "\t");
			}
			System.out.println();
		}
	}
}
