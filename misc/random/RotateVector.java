package random;

import geometry.JVector;

public class RotateVector {

	public static void main(String[] args) {
		JVector v1 = new JVector(.1535, .1535, 0);
		JVector axis = new JVector(0, 0, 1);
		double rotate = -9.75;
		
		System.out.println(JVector.rotate(v1, axis, JVector.zero, rotate));
		
		v1 = new JVector(1, -1, 0);
		JVector v2 = new JVector(-1, -1, 2);
		System.out.println(v1 + " x " + v2 + " = " + JVector.cross(v1, v2));
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println();
		
		v1 = new JVector(1, 1, 1);
		v2 = new JVector(1, -1, 0);
		System.out.println(v1 + " x " + v2 + " = " + JVector.cross(v1, v2));
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println();

		v1 = new JVector(0, 0, 1);
		v2 = new JVector(2, 1, 0);
		System.out.println(v1 + " x " + v2 + " = " + JVector.cross(v1, v2));
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println();

		v1 = new JVector(-1, 0, 0);
		v2 = new JVector(0, 1, -1);
		System.out.println(v1 + " x " + v2 + " = " + JVector.cross(v1, v2));
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println();
		
		v1 = new JVector(-24, 10, 0);
		v2 = new JVector(-26, 3, 0);
		
		System.out.println(JVector.angleDegrees(v1, v2));
		

		System.out.println("Capillary rotation:");
		v1 = new JVector(1, 1, 1);
		v2 = new JVector(0, 2, -1);
		double angle = 60;
		System.out.println(v1 + " x " + v2 + " = " + JVector.rotate(v1, v2, JVector.zero, angle));
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println();

		System.out.println("Twin law rotation:");
		v1 = new JVector(0, 0, 1);
		v2 = new JVector(1, 1, 0);
		angle = 90;
		System.out.println(v1 + " x " + v2 + " = " + JVector.rotate(v1, v2, JVector.zero, angle));
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println();
		
		v1 = new JVector(1, 1, 1);
		v2 = new JVector(0, 2, -1);
		JVector v3 = v1;
		angle = 2;
		for(int i = 1; i < 180/angle; i++) {
			v1 = JVector.rotate(v1, v2, JVector.zero, angle);
			v3 = (JVector) v1.clone();
			double min = Math.min(Math.abs(v3.i), Math.min(Math.abs(v3.j), Math.abs(v3.k)));
			v3.multiply(1./min);
			System.out.println(40+i * angle + "\t" + v3);
		}

		v1 = new JVector(1, 1, 1);
		v2 = new JVector(-1, 1, 1);
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));

		v1 = new JVector(1, 0, 0);
		v2 = new JVector(0, 1, 1);
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println("Cross product of " + v1 + " and " + v2 + " = " + JVector.cross(v1, v2));
		

		v1 = new JVector(1, 1, 0);
		v2 = new JVector(1, -1, 2);
		System.out.println("Angle between " + v1 + " and " + v2 + " = " + JVector.angleDegrees(v1, v2));
		System.out.println("Cross product of " + v1 + " and " + v2 + " = " + JVector.cross(v1, v2));

	}
}
