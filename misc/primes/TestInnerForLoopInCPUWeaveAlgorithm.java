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

public class TestInnerForLoopInCPUWeaveAlgorithm {

	public static void run() {
		long nSolvedMultiplier1, nSolvedMultiplier2;
		long bnFixedInverse = 1091782309871231l;
		int[] primes = new int[] {17, 19, 23, 29, 31, 37, 43, 47, 53};
		
		for(int i = 0; i < primes.length; i++) {
			nSolvedMultiplier1 = (bnFixedInverse*(primes[i]+1))%primes[i];
			nSolvedMultiplier2 = (bnFixedInverse*(primes[i]-1))%primes[i];
			
			System.out.println(nSolvedMultiplier1 + "\t" + nSolvedMultiplier2);
		}
	}
	
	public static void main() {
		run();
	}
}
