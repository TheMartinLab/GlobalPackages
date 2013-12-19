/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import java.util.Random;

import geometry.JVector;

public class Test {

	public JVector rotate(JVector v1, JVector v2) {
		JVector axis = JVector.cross(v1, v2);
		double phi = JVector.angleDegrees(v1, v2);
		v2 = JVector.rotate(v2, axis, JVector.zero, -phi);
		phi = JVector.angleDegrees(v1, v2);
		return v2;
	}
	public static void test() {
		JVector v1 = new JVector(0, -2, 2);
		JVector v2 = new JVector(0, 0, 2);
		JVector v3 = new JVector(-1, -1, 1);
		System.out.println(JVector.angleDegrees(v1, v2));
		System.out.println(JVector.angleDegrees(v1, v3));
	}
	public static void main(String[] args) {
		test();
		JVector v1 = new JVector(0.6942172670414181,-1.4858204420930359,0.0);
		JVector v2 = new JVector(0.0,1.1581908400331036,1.1581908400331036);
		
		Random r = new Random();
		
		//v1 = new JVector(r.nextDouble(), r.nextDouble(), r.nextDouble());
		//v2 = new JVector(r.nextDouble(), r.nextDouble(), r.nextDouble());
		
		Test t = new Test();
		
		v2 = t.rotate(v1, v2);
		
		double phi = JVector.angleDegrees(v1, v2);
		
		while(phi > 0) {
			phi = JVector.angleDegrees(v1, v2);
			v2 = t.rotate(v1, v2);
		}
		
		v1 = new JVector(-27, 10, 0);
		v2 = new JVector(-28, 3, 0);
		
		System.out.println(JVector.angleDegrees(v1, v2));
	}
}
