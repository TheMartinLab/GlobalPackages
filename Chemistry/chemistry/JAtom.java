/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package chemistry;
import io.MyNumberFormat;
import geometry.JVector;


/**
 * This class represents an JAtom.  An JAtom knows its position in (x, y, z) coordinates and its Z.
 * @author Eric Dill
 * 			eddill@ncsu.edu
 *
 */
public class JAtom implements Cloneable {

	/** The abbreviation of the JAtom: Carbon -> C */
	
	/** The Z value of the JAtom: Carbon -> 6 */
	protected int Z;
	
	/** The position in 3 space of the JAtom */
	protected JVector position;
	
	/** The total number of atoms in the system */
	protected static int numberOfAtoms;
	
	/** The identifier of the JAtom object */
	protected final int atomID;
	
	/** The occupancy of this atom */
	protected double occupancy;

	/** 
	 * Constructor, initialize the atom based on a vector and a name
	 * @param Z		Number of electrons in the atom
	 * @param v1	The vector containing the atomic coordinates
	 */
	public JAtom(int Z, JVector v1) {
		position = (JVector) v1.clone();
		
		this.Z = Z;
		
		atomID = numberOfAtoms++;
		occupancy = 1;
	}
	
	public JAtom(int Z, double x, double y, double z) {
		position = new JVector(x, y, z);
		this.Z = Z;
		atomID = numberOfAtoms++;
		occupancy = 1;
	}
	
	public JAtom(int Z, double x, double y, double z, double occupancy) {
		position = new JVector(x, y, z);
		this.Z = Z;
		atomID = numberOfAtoms++;
		this.occupancy = occupancy;
	}
	/**
	 * Shift the position of the atom by the JVector amount
	 * @param amount	Amount to shift the atomic center by
	 */
	public void translate(JVector amount) {
		position = JVector.add(position, amount);
	}
	
	/**
	 * Getter method for number of electrons in the atom
	 * @return	The atomic abbreviation, eg "H" or "He"
	 */
	public int getZ() { return Z; }

	public JVector getPosition() { return position; }
	
	@Override
	public Object clone() {
		return new JAtom(Z, (JVector) position.clone()); 
	}

	/**
	 * toString() override for the JAtom class.  Output: "Z + (i, j, k) + occupancy"
	 */
	@Override
	public String toString() { return Z + "," + position + "," + occupancy; }

	/**
	 * toString() method to output the coordinate list of the atom to create an Atoms .inp file
	 * @param a	The a lattice constant
	 * @param b The b lattice constant
	 * @param c The c lattice constant
	 * @param number	The number of the atom
	 * @param aUnits	The number of units in the a direction
	 * @param bUnits	The number of units in the b direction
	 * @param cUnits	The number of units in the c direction 
	 * @return	The string to print out to the .inp file for Atoms input
	 */
	public String toStringForAtoms(double a, double b, double c, int number, double aUnits, double bUnits, double cUnits) {
		double i = position.i;
		
		double j = position.j;
		
		double k = position.k;
		
		return Z + "\t" + i/(a*aUnits) + "\t" + j/(b*bUnits) + "\t" + k/(c*cUnits) + "\t" + Z + number; 
	}

	public String toStringForXYZ() {
		double i = position.i;
		
		double j = position.j;
		
		double k = position.k;
		return Z + "\t" + i + "\t" + j + "\t" + k;
	}
	/**
	 * Multiply the vectorial position of the atom by the passed scalar amount
	 * @param c	The scalar amount to multiply the atom by
	 */
	public void multiply(double c) { position = JVector.multiply(position, c); }
	public void setNewPos(JVector newPos) { position = newPos; }
	public void setOccupancy(double occ) { occupancy = occ; }
	public double getOccupancy() { return occupancy; }
	public void setPosition(JVector position) { this.position = position; }
}
