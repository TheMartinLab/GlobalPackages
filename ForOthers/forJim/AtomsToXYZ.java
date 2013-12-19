package forJim;

import geometry.JVector;
import io.MyFileInputStream;
import io.MyPrintStream;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import chemistry.JAtom;
import chemistry.JAtomTools;

public class AtomsToXYZ {

	private File inFile;
	private File outFile;
	private int numA, numB, numC;
	private double xLo, xHi, yLo, yHi, zLo, zHi, xy, xz, yz;
	private int[] ZToIdx;
	public AtomsToXYZ() {
		ZToIdx = new int[100];
	}
	
	private JAtom[] readCell() {
		MyFileInputStream mfis = new MyFileInputStream(inFile);
		Scanner s = mfis.getScanner();
		Vector<JAtom> unitCell = new Vector<JAtom>();
		while(s.hasNextLine()) {
			String[] splitLine = s.nextLine().split("\t");
			int idx = Integer.valueOf(splitLine[0]);
			int Z = Integer.valueOf(splitLine[1]);
			double x = Double.valueOf(splitLine[2]);
			double y = Double.valueOf(splitLine[3]);
			double z = Double.valueOf(splitLine[4]);
			
			unitCell.add(new JAtom(Z, new JVector(x, y, z)));
		}
		
		s.close();
		mfis.close();
		return unitCell.toArray(new JAtom[unitCell.size()]);
	}
	public void run() {
		JAtom[] unitCell = readCell();
		
		int numAtoms = unitCell.length * numA * numB * numC;
		MyPrintStream mps = new MyPrintStream(outFile);
		Vector<Integer> numTypes = new Vector<Integer>();
		
		/* DETERMINE THE NUMBER OF ATOM TYPES */
		for(JAtom atom : unitCell)
			if(!numTypes.contains(atom.getZ()))
					numTypes.add(atom.getZ());
		
		
		/* WRITE THE HEADER FOR THE LAMMPS INPUT FILE */
		mps.println("atom_style atomic");
		mps.println();
		mps.println(numAtoms + " atoms");
		mps.println();
		mps.println(numTypes.size() + " atom types");
		mps.println(xy + " " + xz + " " + yz + " xy xz yz");
		mps.println(xLo + " " + xHi + " xlo xhi");
		mps.println(yLo + " " + yHi + " ylo yhi");
		mps.println(zLo + " " + zHi + " zlo zhi");
		mps.println();
		mps.println("Masses");
		
		for(int i = 0; i < numTypes.size(); i++) {
			ZToIdx[numTypes.get(i)] = (i+1);
			mps.println((i+1) + " " + JAtomTools.getMass(numTypes.get(i)));
		}
		
		mps.println();
		mps.println("Atoms");
		mps.println();
		
		int idx = 0;
		int Z;
		double x, y, z;
		JVector pos;
		for(JAtom atom : unitCell) {
			pos = atom.getPosition();
			for(int a = 0; a < numA; a++) {
				x = pos.getI() * a;
				for(int b = 0; b < numB; b++) {
					y = pos.getJ() * b;
					for(int c = 0; c < numC; c++) {
						z = pos.getK() * c;
						mps.println(++idx + " " + ZToIdx[atom.getZ()] + " " + x + " " + y + " " + z);
					}
				}
			}
		}
		
		mps.flush();
		mps.close();
	}
	
	public static void main(String[] args) {
		File atomsFile = new File("Z:\\Simulation\\Eric\\ZnCl2-3H2O\\zncl2-3h2o 1 unit cell (# Z x y z)");
		File outFile = new File(atomsFile.getParent() + File.separator + "zncl2-3h20 (4x4x4 unit cells) (LAMMPS INPUT FILE).data");
		
		int numA = 5;
		int numB = 5;
		int numC = 5;
		
		double xLo = 0,
				yLo = 0,
				zLo = 0,
				xHi = 6.477,
				yHi = 6.480202214,
				zHi = 14.11501645,
				xy = -0.632534799,
				xz = -2.210966131,
				yz = -0.332596344;
		
		AtomsToXYZ atoms = new AtomsToXYZ();
		atoms.inFile = atomsFile;
		atoms.numA = numA;
		atoms.numB = numB;
		atoms.numC = numC;
		atoms.outFile = outFile;
		
		/* ATOMS BOX COORDINATES */
		atoms.xLo = xLo;
		atoms.yLo = yLo;
		atoms.zLo = zLo;
		atoms.xHi = xHi * numA;
		atoms.yHi = yHi * numB;
		atoms.zHi = zHi * numC;
		atoms.xy = xy;
		atoms.xz = xz;
		atoms.yz = yz;
		
		atoms.run();
		
	}
}
