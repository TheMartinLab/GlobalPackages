/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import geometry.JVector;

import java.awt.Point;
import java.awt.geom.Point2D;

public class DiffractionPattern {
	/** need to calculate the intersection of the rays from the detector pixels to the center of the ewald sphere */
	private double sampleToDetector;
	/** needed to calculate the size of the ewald sphere */
	private double wavelength;
	/** the reciprocal lattice holds the reciprocal lattice points and can calculate the diffraction intensity at a given point in 3-space */
	private ReciprocalLattice reciprocalLattice;
	/** the coordinates in x,y of the detector center.  This is used to "align" the sample */
	private Point2D.Double detectorCenter;
	/** the dimensions of the detector(0->xDim, 0->yDim); */
	private int xMin, xMax, yMin, yMax;
	/** the size of the pixels in mm */
	private double pixelSize;
	/** The radius of the ewald sphere */
	private double ewaldSphereR;
	
	private final static double MM_TO_ANGSTROMS = 1e7;
	/**
	 * 
	 * @param sampleToDetector	distance in mm
	 * @param wavelength	in angstroms
	 * @param theLattice	The reciprocal lattice to calculate the diffraction for
	 * @param detectorCenter	The center of the detector in pixels
	 * @param xDim	The number of pixels in the x direction
	 * @param yDim	The number of pixels in the y direction
	 * @param pixelSize	in mm
	 */
	public DiffractionPattern(double sampleToDetector, double wavelength, ReciprocalLattice theLattice,
			Point2D.Double detectorCenter, Point p1, Point p2, double pixelSize) {
		this.sampleToDetector = sampleToDetector*MM_TO_ANGSTROMS;
		this.wavelength = wavelength;
		this.reciprocalLattice = theLattice;
		this.detectorCenter = detectorCenter;
		xMin = p1.x;
		xMax = p2.x;
		yMin = p1.y;
		yMax = p2.y;
		this.pixelSize = pixelSize*MM_TO_ANGSTROMS;
		ewaldSphereR = 2*Math.PI / this.wavelength;
	}
	
	/**
	 * Calculate the diffraction pattern for an unrotated reciprocal lattice
	 * @return	The resultant diffraction image
	 */
	public double[][] calculateImage() {
		return calculateImage(0, 0, 0);
	}
	/**
	 * Calculate the diffraction pattern for a reciprocal lattice rotated by a set amount around each of the axes
	 * in the order rotateXBy(x), rotateYBy(y), rotateZBy(z)
	 * @param xRot	The angle IN DEGREES to rotate around the x axis by
	 * @param yRot	The angle IN DEGREES to rotate around the y axis by
	 * @param zRot	The angle IN DEGREES to rotate around the z axis by
	 * @return	The resultant diffraction image
	 */
	public double[][] calculateImage(double xRot, double yRot, double zRot) {
		BraggReflection[] bragg = reciprocalLattice.getClonedReflections();
		double[][] image = new double[xMax-xMin][yMax-yMin];
		double sx, sy, sz;
		sx = sy = sz = .01;
		rotateLattice(bragg, xRot, yRot, zRot);
		JVector intersection;
		double I;
		for(int x = xMin; x < xMax; x++) {
			for(int y = yMin; y < yMax; y++) {
				intersection = intersectionPoint(x, y);
				I = 0;
				for(int i = 0; i < bragg.length; i++) {
					I += calcContribition(intersection, sx, sy, sz, calculationType.Gaussian, bragg[i]);
				}
				image[xMax-x-1][yMax-y-1] = I;
			}
		}
		return image;
	}
	
	/**
	 * In place rotation of the bragg reflections
	 * @param bragg
	 * @param axis
	 * @param rotationAmount
	 * @return
	 */
	private void rotateLattice(BraggReflection[] bragg, double xRot, double yRot, double zRot) {
		JVector cur;
		/** save time if the rotation results in unity by not performing the rotation operation */
		boolean rotateX = (xRot % 360 != 0);
		boolean rotateY = (yRot % 360 != 0);
		boolean rotateZ = (zRot % 360 != 0);

		/** Loop through the bragg points and rotate around the x, y, z axes by xRot, yRot, zRot */
		for(int i = 0; i < bragg.length; i++) {
			cur = bragg[i].getQ();
			if(rotateX) { cur = JVector.rotate(cur, JVector.x, JVector.zero, xRot); }
			if(rotateY) { cur = JVector.rotate(cur, JVector.y, JVector.zero, yRot); }
			if(rotateZ) { cur = JVector.rotate(cur, JVector.z, JVector.zero, zRot); }
			bragg[i].setQ(cur);
		}
	}
	
	private JVector intersectionPoint(int x, int y) {
		JVector ray = new JVector();
		ray.i = (x-detectorCenter.x) * pixelSize;
		ray.j = (y-detectorCenter.y) * pixelSize;
		ray.k = sampleToDetector + ewaldSphereR;
		
		ray = ray.unit();
		
		ray = JVector.multiply(ray, ewaldSphereR);
		ray.k -= ewaldSphereR;
		
		return ray;
	}
	
	enum calculationType {Gaussian};
	
	private double calcContribition(JVector pos, double sx, double sy, double sz, calculationType theType, BraggReflection bragg) {
		double I = 0;
		switch(theType) {
		case Gaussian:
			I = calcGaussian(bragg.getI(), sx, bragg.getQ(), pos);
			break;
		}
		return I;
	}
	private double calcGaussian(double A, double sx, JVector pos0, JVector pos1) {
		if(JVector.distance(pos0, pos1) < 10*sx) {
			double xExp = Math.pow((pos1.i - pos0.i)/sx, 2)/2;
			double yExp = Math.pow((pos1.j - pos0.j)/sx, 2)/2;
			double zExp = Math.pow((pos1.k - pos0.k)/sx, 2)/2;
			double f = A /Math.sqrt(2*Math.PI)/sx * Math.exp(-1*(xExp + yExp + zExp));
			return f;
		}
		return 0;
	}
}
