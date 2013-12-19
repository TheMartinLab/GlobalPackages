/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package geometry;

import java.util.Arrays;

public class Plane {
	
	public double a, b, c, d;
	public JVector p1, p2, p3;
	public Plane(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	public Plane(JVector p1, JVector p2, JVector p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		initPlane(p1, p2, p3);
	}
	public Plane(JVector n, JVector point) {
		a = n.i;
		b = n.j;
		c = n.k;
		d = -n.i*point.i - n.j*point.j - n.k-point.k;
	}
	public Plane(double[] d) {
		if(d.length != 4) {
			throw new RuntimeException("Array: " + Arrays.toString(d) + " does not contain four values with which to initialize a new plane.");
		} else {
			a = d[0];
			b = d[1];
			c = d[2];
			this.d = d[3];
		}
	}
	public Plane(JVector[] points) {
		this(points[0], points[1], points[2]);
	}
	public static double distance(JVector point, Plane p) {
		double numerator = Math.abs(point.i * p.a + point.j * p.b + point.k * p.c - p.d);
		
		double denominator = Math.sqrt(Math.pow(p.a, 2) + Math.pow(p.b, 2) + Math.pow(p.c, 2));
		
		double val = numerator / denominator;
		
		return val;
	}
	
	public static double distance(Plane p1, Plane p2) {
		if(areParallel(p1, p2)) {
			JVector point = p1.getPoint();
			double d = distance(point, p2); 
			return d;
		} else {
			throw new RuntimeException("Planes p1 " + p1.toString() + " and p2 " + p2.toString() + " are not parallel.  " +
					"Distance determination cannot proceed.");
		}
	}
	public static boolean areParallel(Plane p1, Plane p2) {
		JVector v1 = p1.getNormalVec();
		JVector v2 = p2.getNormalVec();
		
		if(JVector.cross(v1, v2).length() == 0) {
			return true;
		}
		return false;
	}
	
	// instance methods
	public JVector getNormalVec() {
		return new JVector(a, b, c);
	}
	
	public JVector getPoint() {
		if(p1 != null) { return p1; }
		if(p2 != null) { return p2; }
		if(p3 != null) { return p3; }
		
		throw new RuntimeException("Point determination not set up unless the plane is initialized by three points.");
	}

	/**
	 * 
	 * @param a Point 1
	 * @param b Point 2
	 * @param c Point 3
	 * @return 
	 */
	private void initPlane(JVector a, JVector b, JVector c) {
		JVector a1, b1, c1;
		
		a1 = (JVector) a.clone();
		b1 = (JVector) b.clone();
		c1 = (JVector) c.clone();
		a1.i=1;
		b1.i=1;
		c1.i=1;
		this.a = JVector.det(a1, b1, c1);
		
		a1 = (JVector) a.clone();
		b1 = (JVector) b.clone();
		c1 = (JVector) c.clone();
		a1.j=1;
		b1.j=1;
		c1.j=1;
		this.b = JVector.det(a1, b1, c1);
		
		a1 = (JVector) a.clone();
		b1 = (JVector) b.clone();
		c1 = (JVector) c.clone();
		a1.k=1;
		b1.k=1;
		c1.k=1;
		this.c = JVector.det(a1, b1, c1);
		
		this.d = -1 * JVector.det(a, b, c);
		
		double max = Math.max(Math.max(Math.abs(this.a), Math.abs(this.b)), Math.max(Math.abs(this.c), Math.abs(this.d)));
		this.a /= max;
		this.b /= max;
		this.c /= max;
		this.d /= max;
		/**
		JVector v1 = JVector.subtract(b, a);
		JVector v2 = JVector.subtract(c, a);
		
		JVector n = JVector.cross(v1, v2);
		
		double d = JVector.dot(n, a);
		double x = d/n.i;
		double y = d/n.j;
		double z = d/n.k;
		
		this.a = x;
		this.b = y;
		this.c = z;
		this.d = d;
		*/
	}
	@Override
	public String toString() {
		return "(" + a + ", " + b + ", " + c + ", " + d + ")";
	}
}
