/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package lattice;

import java.io.Serializable;
import java.util.Vector;

import geometry.JVector;

public class BraggReflection implements Cloneable, Serializable {

	private static final long serialVersionUID = 5381635426580180019L;
	private double measI, calcI;
	private JVector hkl;
	private JVector Q;
	private JVector coords;
	private double q, phi;
	private String name;

	private Vector<BraggReflection> degenerate;
	
	public BraggReflection(double I, JVector hkl, JVector Q) {
		this.setMeasI(I);
		this.setHkl(hkl);
		this.setQ(Q);
		name = BraggReflection.vecToName(hkl);
		q = Q.length();
	}
	
	public BraggReflection(double I, double Q, JVector hkl) {
		this.setMeasI(I);
		this.setHkl(hkl);
		this.setQ(JVector.multiply(hkl.unit(), Q));
		name = BraggReflection.vecToName(hkl);
		q = Q;
	}
	public int getNumDegenerate() { 
		if(degenerate == null) { return 0; }
		return degenerate.size()-1;
	}
	/**
	 * 
	 * @param idx
	 * @return null if idx > degenerate.size()
	 */
	public BraggReflection getDegenerateBragg(int idx) {
		if(degenerate == null)
			return this;

			if(idx >= degenerate.size())
			return null;
		
		return degenerate.get(idx);
	}
	public void addDegenerateBragg(BraggReflection bragg) {
		if(degenerate == null) {
			degenerate = new Vector<BraggReflection>();
			degenerate.add(this);
			degenerate.add(bragg);
			appendName(vecToName(bragg.hkl));
		} else {
			BraggReflection cur;
			boolean accountedFor = false;
			for(int i = 0; i < degenerate.size(); i++) {
				cur = degenerate.get(i);
				if(cur.sameFamily(bragg) || cur.sameQ(bragg, 0)) {
					accountedFor = true;
				}
			}
			if(!accountedFor) {
				degenerate.add(bragg);
				appendName(vecToName(bragg.hkl));
			}
		}
	}
	
	public boolean sameFamily(BraggReflection bragg) {
		double compSum1 = bragg.getHkl().absComponentSum();
		double compSum2 = this.getHkl().absComponentSum();
		
		if(compSum1 == compSum2) { return true; }
		
		return false;
	}
	
	public boolean sameQ(BraggReflection bragg, double tolerance) {
		double q1 = Q.length();
		double q2 = bragg.getQ().length();
		double val = Math.abs(q1-q2)/q1;
		if(val < tolerance) { return true; }
		
		return false;
	}
	@Override
	public String toString() {
		return getMeasI() + "\t" + getCalcI() + "\t"+ getHkl() + "\t" + getQ().length() + "\t" + getPhi();
	}

	public void setQ(JVector q) {
		Q = q;
		this.q = Q.length();
	}

	public void setCoords(JVector COORDS) { coords = (JVector) COORDS.clone(); }
	public JVector getCoords() { return coords; }
	public void setMeasI(double i) {measI = i; }
	public double getMeasI() { return measI; }
	public void setCalcI(double i) {calcI = i; }
	public double getCalcI() { return calcI; }
	public JVector getQ() { return Q; }
	public void setHkl(JVector hkl) { this.hkl = hkl; }
	public JVector getHkl() { return hkl; }
	public void setPhi(double phi) { this.phi = phi; }
	public double getPhi() { return phi; }
	public boolean isDegenerate() { 
		return degenerate != null && degenerate.size() > 1;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		BraggReflection br = new BraggReflection(measI, (JVector) hkl.clone(), (JVector) Q.clone());
		br.q = q;
		br.phi = phi;
		br.calcI = calcI;
		if(degenerate != null) {
			br.degenerate = (Vector<BraggReflection>) degenerate.clone();
		}
		return br;
	}

	public static String vecToName(JVector vec) {
		return "< " + vec.i + " " + vec.j + " " + vec.k + " >";
	}
	public static String vecToNameAbs(JVector vec) {
		return "< " + Math.abs(vec.i) + " " + Math.abs(vec.j)+ " " + Math.abs(vec.k) + " >";
	}
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	public void appendName(String app) {
		name += ", " + app;
	}
	
	
}
