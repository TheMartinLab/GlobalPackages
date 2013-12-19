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
package primes;

public class blah {

	public static void main(String[] args) {
		for(int i = 0; i < 100; i++) {
			System.out.println(i + " " + ((i&1) == 0) + " " + ((i%2)==0));

		}
		TestInnerForLoopInCPUWeaveAlgorithm.run();
	}
}
