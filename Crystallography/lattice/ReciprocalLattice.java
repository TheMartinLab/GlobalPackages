/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import java.util.Vector;

import bravaisLattice.BravaisLattice;

import geometry.JComplex;
import geometry.JVector;
import scattering.ComplexScatteringFactor;
import calculate.Calibration;
import calculate.Calibration.parameters;
import chemistry.JAtom;

public class ReciprocalLattice implements Cloneable {
	private double x0, y0, pixelSize, sampleToDetector;
	private double wavelength = .13702;
	private double cutoff = 1e-5;
	private BravaisLattice theLattice;
	private Vector<BraggReflection> theReflections;
	public ReciprocalLattice(BravaisLattice theLattice) {
		this.theLattice = theLattice;
		theReflections = new Vector<BraggReflection>();
	}

	public void setCalib(Calibration c) {
		x0 = (Double) c.getParam(Calibration.parameters.x);
		y0 = (Double) c.getParam(Calibration.parameters.y);
		pixelSize = Double.valueOf(c.getParam(Calibration.parameters.pixel_size) + "");
		sampleToDetector = Double.valueOf(c.getParam(Calibration.parameters.distance) + "");
	}
	public void setCalib(double X, double Y, double pixelSize, double sampleToDetector) {
		x0 = X;
		y0 = Y;
		this.pixelSize = pixelSize;
		this.sampleToDetector = sampleToDetector;
	}
	public void calculateReciprocalLattice(double maxQ, double maxH) throws Exception {
		JAtom[] theAtoms = theLattice.getAtoms();
		ComplexScatteringFactor csf = new ComplexScatteringFactor();
		double I;
		JComplex scatteringFactor;
		JComplex totalScattering = new JComplex(0, 0);
		JComplex tempScattering = new JComplex(0, 0);
		int min = (int) -Math.rint(maxH);
		int max = (int) Math.rint(maxH);
		JVector Q;
		JVector hkl = new JVector();
		JAtom cur;
		JVector q110 = theLattice.calcQ(new JVector(1, 1, 0));
		double theta = Math.asin(q110.length() * wavelength / 4 / Math.PI);
		/** sampleToDetector in mm */
		/** pixelSize in um */
		double x = Math.tan(2*theta) * sampleToDetector * 1000 / pixelSize;
		BraggReflection br;
		JVector coords, zero = new JVector(x0, y0, 0);
		for(int l = min; l <= max; l++) {
			hkl.k = l;
			for(int k =min; k <= max; k++) {
				hkl.j = k;
				for(int h = min; h <= max; h++) {
					hkl.i = h;
					Q = theLattice.calcQ(hkl);
					if(Q.length() < maxQ) {
						totalScattering.setIm(0);
						totalScattering.setRe(0);
						if(l == 0 && k == 0 && h == 1) {
							System.out.println("h,k,l = (1,0,0)");
						}
						for(int i = 0; i < theAtoms.length; i++) {
							cur = theAtoms[i];
							scatteringFactor = csf.generateF0(Q, cur.getZ(), csf.getComplexF(1, wavelength, cur.getZ()));
							//atomPos = theLattice.fractionalToReal(cur.getPosition());
							tempScattering.setIm(2*Math.PI*JVector.dot(hkl, cur.getPosition()));
							tempScattering.setRe(0);
							tempScattering = JComplex.exponential(tempScattering);
							tempScattering = JComplex.multiply(scatteringFactor, tempScattering);
							totalScattering = JComplex.add(totalScattering, tempScattering);
						}
						I = totalScattering.modulus2()*lorentzPolarizationFactor(Q.length());
						if(I > cutoff && I < Double.POSITIVE_INFINITY) {
							br = new BraggReflection(I, (JVector) hkl.clone(), (JVector) Q.clone());
							br.setMeasI(0);
							br.setCalcI(I);
							coords = JVector.multiply(hkl, x);
							coords = JVector.add(coords, zero);
							br.setCoords(coords);
							theReflections.add(br);
						}
					}
				}
			}
		}
	}
	private double lorentzPolarizationFactor(double Q) {
		double theta = Math.asin(Q*wavelength/4/Math.PI);
		double lorentzPol = (1+Math.pow((Math.cos(2*theta)), 2))/(Math.pow(Math.sin(2*theta), 2)*Math.cos(theta));
		return lorentzPol;
	}
	public Vector<BraggReflection> getReflections() { return theReflections; }
	public BraggReflection[] getClonedReflections() {
		BraggReflection[] bragg = new BraggReflection[theReflections.size()];
		for(int i = 0; i < bragg.length; i++) {
			bragg[i] = (BraggReflection) theReflections.get(i).clone();
		}
		return bragg;
	}
}
