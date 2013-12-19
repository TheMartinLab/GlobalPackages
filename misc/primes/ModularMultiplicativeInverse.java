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

import java.math.BigInteger;

public class ModularMultiplicativeInverse {

	public static int modInverse_brute(int a, int b) throws Exception {
		a %= b;
		for(int x = 1; x < b; x++) {
			if((a*x) % b == 1) {
				return x;
			}
		}
		
		throw new Exception("a and b are not invertible");
	}
	public static int modInverse_bruteTest(int a, int b) {
		a %= b;
		int numInverses = 0;
		String inverses = "(a,b): (" + a + "," + b + "): ";
		//System.out.println("Searching for mod inverse for: " + a + ", " + b);
		for(int x = 1; x < b; x++) {
			if((a*x) % b == 1) {
				//System.out.println("(a,b)=c ==> (" + a + "," + b + ")=" + x);
				//return x;
				numInverses++;
				inverses += numInverses + ": " + x + ", ";
			}
		}
		//System.out.println("All inverses found for: " + a + ", " + b);
		if(numInverses > 1) {
			BigInteger b1 = new BigInteger(""+a);
			BigInteger b2 = new BigInteger(""+b);
			b2 = b1.gcd(b2);
			System.out.println(inverses + " gcd of a and b: " + b2);
		}
		return numInverses;
	}
	public static int modInverse_extendedEuc(int a, int b) {
		
		
		return -1;
	}
	public static void main(String[] args) {
		int[] primes = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};
		int maxval = 100000;
		BigInteger[] bigPrimes = new BigInteger[maxval];
		for(int i = 1; i < maxval; i++) {
			bigPrimes[i] = new BigInteger(i + "");
		}
		String failString = "fail";
		String bigIntResult = "fail";
		String myBruteForceResult = "fail";
		/*for(int i = 1; i < maxval; i++) {
			for(int j = i+1; j < maxval; j++) {
				try {
					bigIntResult = bigPrimes[i].modInverse(bigPrimes[j]) + "";
				} catch(ArithmeticException e) {
					bigIntResult = failString;
				}
				try {
					myBruteForceResult = modInverse_brute(i, j) + "";
				} catch(Exception e) {
					myBruteForceResult = failString;
				}
				System.out.print("primes (i,j) = (" + i + ", " + j + ") modInverse: " );
				System.out.print("bigPrimes -- mine: " + bigIntResult);
				System.out.println(" -- " + myBruteForceResult);
			}
		}*/
		

		int maxInverseVal = 0;
		int tmpInverseVal = 0;
		for(int i = 1; i < maxval; i+=(int) (Math.random() / Math.random() / Math.random()/ Math.random()/ Math.random()/ Math.random())) {
			System.out.println(i);
			for(int j = i+1; j < maxval; j+=(int) (Math.random() / Math.random() / Math.random())) {
				tmpInverseVal = modInverse_bruteTest(i,j);
				if(tmpInverseVal > maxInverseVal) {
					maxInverseVal = tmpInverseVal;
				}
			}
		}
		
		System.out.println("maxInverseVal: " + maxInverseVal);
	}
}
