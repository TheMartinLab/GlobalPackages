/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package spotPicking;

import geometry.JVector;
import io.MyObjectInputStream;
import io.MyObjectOutputStream;
import io.MyPrintStream;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

import lattice.BraggReflection;
import lattice.BraggUtilities;

public class BraggCompare {

	private BraggReflection[] calculated, measured, uniqueCalc, uniqueMeas;
	private BraggReflection[][] step1; 
	/**
	 * meas[][0] = q
	 * <br>meas[][1] = phi
	 * <br>meas[][2] = I
	 * <br>meas[][3] = x
	 * <br>meas[][4] = y
	 */
	private double[][] meas;
	private double[] calcI;
	private String[] family;
	private double tolerance;
	private final static double INTENSITY_TOLERANCE = 0.5;
	private final static double PHI_TOLERANCE = 0.02;
	private final static double Q_TOLERANCE = 0.01;
	private MyPrintStream mps;
	
	private String step1_name = "after decompose_step1.obj";
	
	private double x0, y0, sampleToDetector, wavelength, pixelSize, rotationAmount;
	private JVector sampleRotationAxis;
	public BraggCompare(BraggReflection[] allCalc, double[][] meas, double percentTolerance, double x0, double y0,
			double sampleToDetector, double wavelength, double pixelSize, 
			JVector sampleRotationAxis, double rotationAmount) {
		this.x0 = x0;
		this.y0 = y0;
		this.sampleToDetector = sampleToDetector;
		this.wavelength = wavelength;
		this.pixelSize = pixelSize;
		calculated = allCalc;
		this.sampleRotationAxis = sampleRotationAxis;
		this.rotationAmount = rotationAmount;
		uniqueCalc = BraggUtilities.getUnique(allCalc);
		this.meas = meas;
		tolerance = percentTolerance;
		recoverFromBadExit();
		run();
	}
	
	private void recoverFromBadExit() {
		mps = new MyPrintStream(new File("Bragg assignment test.txt"));
		if(mps.getPrintStream() == null) {
			return;
		}
		MyObjectInputStream mis = new MyObjectInputStream(new File(step1_name));
		if(mis.wasInitialized()) {
			step1 = (BraggReflection[][]) mis.readObject();
		}
	}
	public void run() {
		//if(step1 == null) {
		assignFamily();
		mps = new MyPrintStream(new File("Bragg assignment test.txt"));
		measured = toBragg();
			/*step1 = path_1_decompose_step1();
			MyObjectOutputStream mos = new MyObjectOutputStream(new File("after decompose_step1.obj"));
			mos.writeObject(step1);
			mos.close();*/
		//}
		
		//path_1_decompose_step2(step1);
		
		BraggReflection[][] finalPossibilities = path_2_decompose_step2(measured);
		
		for(int i = 0; i < finalPossibilities.length; i++) {
			BraggReflection[] set = finalPossibilities[i];
			String s = "\n\nSet: " + i + " has " + set.length + " possible members.";
			System.out.println(s);
			mps.println(s);
			for(int j = 0; j < set.length; j++) {
				System.out.println(set[j]);
				mps.println(set[j] + "\t" + set[j].getName());
			}
		}
	}
	public void align_first(BraggReflection br) {
		JVector braggFamily = br.getHkl();
		braggFamily.abs();
		
		BraggReflection calc = null;
		double q1 = br.getQ().length(), q2;
		for(int i = 0; i < calculated.length; i++) {
			calc = calculated[i];
			q2 = calc.getQ().length();
			if(calc.sameQ(br, Q_TOLERANCE)) 
				break;
		}
		if(calc == null) 
			return;
		
		BraggCompare2.rotate(calculated, br, calc);
		// check angle to make sure rotation worked
		JVector rotAxis = JVector.cross(br.getQ(), calc.getQ());
		double rotAngle = JVector.angleDegrees(br.getQ(), calc.getQ());
		int numLoops = 0;
		while(rotAngle > 0) {
			numLoops++;
			if(numLoops > 100) {
				System.out.println(numLoops + " is the current loop idx.  You fucked up!");
			}
			BraggCompare2.rotate(calculated, br, calc);
			rotAxis = JVector.cross(br.getQ(), calc.getQ());
			rotAngle = JVector.angleDegrees(br.getQ(), calc.getQ());
		}
		
		
		return;
	}
	private void rotate(BraggReflection br, BraggReflection calc) {
		JVector rotAxis = JVector.cross(br.getQ(), calc.getQ());
		double rotAngle = JVector.angleDegrees(br.getQ(), calc.getQ());
		JVector zero = JVector.zero;
		
		JVector q;
		q = JVector.rotate(calc.getQ(), rotAxis, zero, -rotAngle);
		calc.setQ(q);
		
		for(int i = 0; i < calculated.length; i++) {
			q = calculated[i].getQ();
			q = JVector.rotate(q, rotAxis, zero, -rotAngle);
			calculated[i].setQ(q);
		}

		rotAxis = JVector.cross(br.getQ(), calc.getQ());
		rotAngle = JVector.angleDegrees(br.getQ(), calc.getQ());
	}
	
	public BraggReflection[] align_second_potentials(BraggReflection br1, BraggReflection br2) {
		
		JVector rotAxis = br1.getQ();
		JVector alignTarget = br2.getQ();
		JVector calcQ, orthogAxis;
		double phi;
		Vector<BraggReflection> alignable = new Vector<BraggReflection>();
		for(int i = 0; i < calculated.length; i++) {
			if(br2.sameQ(calculated[i], Q_TOLERANCE)) {
				calcQ = calculated[i].getQ();
				orthogAxis = JVector.cross(alignTarget, calcQ);
				phi = JVector.angleDegrees(rotAxis, orthogAxis);
				// if the angle between the aligned br1 and the cross product of
				// the target br2 and the calculated value calculated[i].getQ()
				// is less than 2.5 degrees, than consider them alignable and 
				// therefore the calculated hkl is in the same projection as br2. 
				if(phi < rotationAmount) {
					alignable.add(calculated[i]);
				}
			}
		}
		
		BraggReflection[] alignableArray = new BraggReflection[alignable.size()];
		alignableArray = alignable.toArray(alignableArray);
		
		return alignableArray;
	}
	public boolean hasMatch(BraggReflection br3, BraggReflection br1) {
		boolean isSameQ, isWithinRotationAmount, hasMatchedIntensities;
		BraggReflection calc;
		double angle;
		for(int i = 0; i < calculated.length; i++) {
			calc = calculated[i];
			isSameQ = br3.sameQ(calc, Q_TOLERANCE);
			angle = JVector.angleDegrees(br3.getQ(), calc.getQ());
			isWithinRotationAmount = angle < rotationAmount;
			hasMatchedIntensities = intensitiesMatch(br1, br3);
			if(isSameQ && isWithinRotationAmount && hasMatchedIntensities) {
				//isSameQ = br3.sameQ(calc, Q_TOLERANCE);
				//isWithinRotationAmount = JVector.angleDegrees(br3.getQ(), calc.getQ()) < rotationAmount;
				br3.setHkl(calc.getHkl());
				br3.setCalcI(calc.getMeasI());
				return true;
			}
		}
		return false;
	}
	private void align_second(BraggReflection brAxis, BraggReflection meas, BraggReflection brCalc) {
		JVector rotAxis = brAxis.getQ();
		double phi = JVector.angleDegrees(meas.getQ(), brCalc.getQ());
		JVector zero = JVector.zero;
		JVector q;
		for(int i = 0; i < calculated.length; i++) {
			q = calculated[i].getQ();
			q = JVector.rotate(q, rotAxis, zero, -phi);
			calculated[i].setQ(q);
		}
		
	}
	private void printSet(BraggReflection[] set, BraggReflection primary, 
			BraggReflection secondary, BraggReflection calc) {
		if(primary != null)
			mps.println("Primary alignment:\n\t" + primary.toString());
		if(secondary != null)
			mps.println("Secondary alignment:\n\t" + secondary.toString());
		if(calc != null)
			mps.println("Calculated bragg aligned with secondary:\n\t" + calc.toString());
		else 
			mps.println("Calculated bragg aligned with secondary:\n\t" + set[2].toString());
		for(int i = 0; i < set.length; i++) {
			if(i != 2)
				mps.println(i + "\t" + set[i]);
		}
	}
	private void printSet(Vector<BraggReflection> set, BraggReflection primary, 
			BraggReflection secondary, BraggReflection calc) {
		if(primary != null)
			mps.println("Primary alignment:\n\t" + primary.toString());
		if(secondary != null)
			mps.println("Secondary alignment:\n\t" + secondary.toString());
		if(calc != null)
			mps.println("Calculated bragg aligned with secondary:\n\t" + calc.toString());
		for(int i = 0; i < set.size(); i++) {
			mps.println(i + "\t" + set.get(i));
		}
	}
	public BraggReflection[][] path_2_decompose_step2(BraggReflection[] measured) {
		Vector<BraggReflection[]> possibleSets = new Vector<BraggReflection[]>();
		Vector<BraggReflection[]> topThrees = new Vector<BraggReflection[]>();
		Vector<BraggReflection> set = null;
		
		BraggReflection[][] top3;
		
		BraggReflection br1, br2, br3;
		BraggReflection[] alignable;
		BraggReflection[] aSet;
		
		// primarily align calculated lattice to one bragg reflection
		for(int i = 0; i < measured.length; i++) {
			br1 = measured[i];
			mps.println("i = " + i + " corresponding to bragg reflection: " + br1);
			align_first(br1);
			possibleSets = new Vector<BraggReflection[]>();
			// secondarily align calculated lattice to one of the sets of 
			// the corresponding bragg reflections, if possible
			for(int j = 0; j < measured.length; j++) {
				br2 = measured[j];
				if(i != j && intensitiesMatch(br1, br2)) {
					alignable = null;
					alignable = align_second_potentials(br1, br2);
					if(alignable.length > 0) {
						// loop through the sets of secondary alignment possibilities
						for(int a = 0; a < alignable.length; a++) {
							set = new Vector<BraggReflection>();
							set.add((BraggReflection) br1.clone());
							set.add((BraggReflection) br2.clone());
							set.add(alignable[a]);
							align_second(br1, br2, alignable[a]);
							// loop through the remaining measured spots to see if any others match calculated spots
							for(int k = 0; k < measured.length; k++) {
								br3 = measured[k];
								if(k != i && k != j) {
									if(hasMatch(br3, br1)) {
										set.add((BraggReflection) br3.clone());
									}
								}
							}
							//printSet(set, br1, br2, alignable[a]);
							aSet = new BraggReflection[set.size()];
							aSet = set.toArray(aSet);
							possibleSets.add(aSet);
						}
					}
				}
			}
			// add 3 with the most diffraction spots to the final possibilities
			int biggest = 0, biggestIdx = 0;
			mps.println("Top 3 for i = " + i);
			for(int a = 0; a < 3; a++) {
				if(possibleSets.size() > 0) {
					mps.println((a+1) + " / 3");
					biggest = 0;
					for(int b = 0; b < possibleSets.size(); b++) {
						if(biggest < possibleSets.get(b).length) {
							biggest = possibleSets.get(b).length;
							biggestIdx = b;
						}
					}
					BraggReflection[] bragg = possibleSets.remove(biggestIdx);
					printSet(bragg, bragg[0], bragg[1], null);
					topThrees.add(bragg);
				}
			}
		}
		BraggReflection[][] finalPossibilities = new BraggReflection[topThrees.size()][];
		finalPossibilities = topThrees.toArray(finalPossibilities);
		return finalPossibilities;
	}
	
	public void assignFamily() {
		family = new String[meas.length];
		String assigned;
		calcI = new double[meas.length];
		for(int i = 0; i < meas.length; i++) {
			assigned = getFamily(meas[i][0], i);
			if(assigned != "") {
				family[i] = assigned;
				System.out.println("Measured: Q = " + meas[i][0] + ", phi = " + meas[i][1]);
				System.out.println("\tAssigned to: " + family[i]);
			}
		}
	}
	private BraggReflection getMember(BraggReflection meas) {
		BraggReflection calc;
		
		for(int i = 0; i < calculated.length; i++) {
			calc = calculated[i];
			if(meas.sameQ(calc, Q_TOLERANCE)) {
				if(meas.sameFamily(calc)) {
					return calc;
				}
			}
		}
		return null;
	}
	private String getFamily(double Q, int idx) {
		double min = 150;
		double calc;
		String vec = "";
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		for(int i = 0; i < calculated.length; i++) {
			calc = calculated[i].getQ().length();
			if(min > Math.abs(Q-calc)) {
				min = Math.abs(Q-calc);
				vec = "Q = " + twoDForm.format(calculated[i].getQ().length()) + ", " ;
				vec += calculated[i].getName();
				calcI[idx] = calculated[i].getMeasI();
			}
		}
		if(min > 0.03) {
			vec = "";
		}
		return vec;
	}
	private BraggReflection[] toBragg() {
		Vector<BraggReflection> toBragg = new Vector<BraggReflection>();
		
		double q1;
		BraggReflection br;
		JVector[] options;
		JVector q, hkl;
		
		for(int i = 0; i < meas.length; i++) {
			if(family[i] != null) {
				q1 = meas[i][0];
				options = assignmentToHKL(family[i]);
				for(int optionIdx = 0; optionIdx < options.length; optionIdx++) {
					hkl = options[optionIdx];
					q = new JVector(meas[i][3] - x0, meas[i][4] - y0, 0).unit();
					q = JVector.multiply(q, q1);
					br = new BraggReflection(meas[i][2], hkl, q);
					br.setPhi(meas[i][1]);
					br.setCoords(new JVector(meas[i][3], meas[i][4], 0));
					br.setCalcI(BraggUtilities.getCalcI(br, calculated));
					toBragg.add(br);
				}
			}
		}
		BraggReflection[] theBragg = new BraggReflection[toBragg.size()];
		theBragg = toBragg.toArray(theBragg);
		return theBragg;
	}
	public void path_1_decompose_step2(BraggReflection[][] allSets) {
		mps.println("********************************");
		mps.println("********************************");
		mps.println("********************************");
		mps.println("********************************");
		mps.println("**                            **");
		mps.println("**                            **");
		mps.println("**                            **");
		mps.println("**     decompose_step2()      **");
		mps.println("**                            **");
		mps.println("**                            **");
		mps.println("**                            **");
		mps.println("**                            **");
		mps.println("********************************");
		mps.println("********************************");
		mps.println("********************************");
		mps.println("********************************");
		Vector<JVector[]> fromOneSet = new Vector<JVector[]>();
		Vector<JVector> crossProd;
		JVector hkl1, hkl2;
		JVector cross, zoneAxis = new JVector();
		for(int idx = 0; idx < allSets.length; idx++) {
			mps.close();
			mps = new MyPrintStream(new File(idx + ".txt"));
			int[][] indices = getIndices(allSets[idx]);
			double angle = 0;
			
			for(int i = 0; i < indices.length; i++) {
				crossProd = new Vector<JVector>();
				hkl1 = allSets[idx][0].getDegenerateBragg(indices[i][0]).getHkl();
				for(int j = 1; j < indices[i].length; j++) {
					hkl2 = allSets[idx][j].getDegenerateBragg(indices[i][j]).getHkl();
					cross = JVector.cross(hkl1, hkl2);
					zoneAxis = JVector.add(zoneAxis, cross);
					crossProd.add(cross);
				}
				JVector[] temp = new JVector[crossProd.size()];
				temp = crossProd.toArray(temp);
				fromOneSet.add(temp);
				
				// compute angle between cross product vectors and the zone axis
				hkl1 = allSets[idx][0].getDegenerateBragg(indices[i][0]).getHkl();
				mps.println(hkl1);
				for(int j = 1; j < indices[i].length; j++) {
					hkl2 = allSets[idx][j].getDegenerateBragg(indices[i][j]).getHkl();
					cross = JVector.cross(hkl1, hkl2);
					if(cross.length() == 0) {
						angle = 0;
					} else {
						angle = JVector.angleDegrees(zoneAxis, cross);
					}
					mps.println(allSets[idx][j].getDegenerateBragg(indices[i][j]).toString() + "\t" + angle + "\t" + cross);
					crossProd.add(cross);
				}
			}
		}
	}
	
	public int[][] getIndices(BraggReflection[] set) {

		int[] maxVals = new int[set.length];
		int[] curVals = new int[set.length];
		
		int numPossibleSets = 1;
		
		for(int i = 0; i < set.length; i++) {
			maxVals[i] = set[i].getNumDegenerate();
			numPossibleSets += maxVals[i];	// subtract 1 due to offset between counting and java numbering
		}
		
		int[][] keys = new int[numPossibleSets][set.length];
		boolean increment = true;
		for(int i = 0; i < numPossibleSets; i++) {
			increment = true;
			for(int j = 0; j < set.length; j++) {
				if(increment && curVals[j] < maxVals[j]) {
					increment = false;
					keys[i][j] = curVals[j]++;
				} else {
					keys[i][j] = curVals[j];
				}
			}
		}
		
		return keys;
	}
	public BraggReflection[][] path_1_decompose_step1() {
		Vector<BraggReflection[]> decomposed = new Vector<BraggReflection[]>();
		
		Vector<BraggReflection> set = new Vector<BraggReflection>();
		BraggReflection br1, br2;
		
		for(int i = 0; i < measured.length; i++) {
			br1 = measured[i];
			set = new Vector<BraggReflection>();
			set.add(br1);
			for(int j = 0; j < measured.length; j++) {
				br2 = measured[j];
				if(intensitiesMatch(br1, br2)) {
					BraggReflection[] possibleAssignments = related(br1, br2);
					if(possibleAssignments != null) {
						BraggReflection br2Clone = (BraggReflection) br2.clone();
						for(int p = 0; p < possibleAssignments.length; p++) {
							br2Clone.addDegenerateBragg(possibleAssignments[p]);
						}
						set.add(br2Clone);
					}
				}
			}
			BraggReflection[] brSet = new BraggReflection[set.size()];
			brSet = set.toArray(brSet);
			decomposed.add(brSet);
		}
		BraggReflection[][] sets = new BraggReflection[decomposed.size()][];
		sets = decomposed.toArray(sets);
		return sets;
	}
	private boolean intensitiesMatch(BraggReflection br1, BraggReflection br2) {
		double calcI1, calcI2, measI1, measI2, calcRatio, measRatio;
		if(br1.getCalcI() != 0 && br2.getCalcI() != 0) {
			calcI1 = br1.getCalcI();
			calcI2 = br2.getCalcI();
			calcRatio = calcI1/calcI2;
			
			measI1 = br1.getMeasI();
			measI2 = br2.getMeasI();
			measRatio = measI1/measI2;
			if(Math.abs((calcRatio - measRatio) / measRatio) < INTENSITY_TOLERANCE) {
				return true;
			}
			return false;
		}
		calcI1 = BraggUtilities.getCalcI(br1, uniqueCalc);
		calcI2 = BraggUtilities.getCalcI(br2, uniqueCalc);
		
		measI1= br1.getMeasI();
		measI2 = br2.getMeasI();
		
		calcRatio = calcI1/calcI2;
		measRatio = measI1/measI2;
		
		if(Math.abs((calcRatio - measRatio) / measRatio) < INTENSITY_TOLERANCE) {
			return true;
		}
		
		return false;
	}
	private BraggReflection[] related(BraggReflection br1, BraggReflection br2) {
		if(br1 == br2) { return null; }
		
		double phi1 = br1.getPhi();
		double phi2 = br2.getPhi();
		double diff = Math.abs(phi1-phi2);
		
		JVector[] family2 = JVector.getHKL(br2.getHkl());
		
		Vector<BraggReflection> possibleAssignments = new Vector<BraggReflection>();
		
		double angle, percentDiff;
		
		for(int j = 0; j < family2.length; j++) {
			angle = JVector.angleDegrees(br1.getHkl(), family2[j]);
			percentDiff = Math.abs(angle-diff) / diff;
			if(percentDiff < PHI_TOLERANCE) {
				BraggReflection assigned = (BraggReflection) br2.clone();
				assigned.setHkl(family2[j]);
				possibleAssignments.add(assigned);
			}
		}
		
		BraggReflection[] bragg = new BraggReflection[possibleAssignments.size()];
		bragg = possibleAssignments.toArray(bragg);
		
		return bragg;
	}
	
	private JVector[] assignmentToHKL(String assignment) {
		String[] hkl = assignment.split(",");
		
		JVector[] hkls = new JVector[hkl.length-1];
		
		for(int i = 0; i < hkls.length; i++) {
			hkls[i] = nameToHKL(hkl[i+1]);
		}
		
		return hkls;
	}
	
	private JVector nameToHKL(String name) {
		String[] hkl = name.split(" ");
		return new JVector(Double.valueOf(hkl[2]), Double.valueOf(hkl[3]), Double.valueOf(hkl[4]));
	}
	
}
