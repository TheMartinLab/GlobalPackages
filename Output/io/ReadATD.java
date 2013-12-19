/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;
import java.util.Scanner;

import chemistry.JAtom;

public class ReadATD {

	private File f;
	
	private JAtom[] atoms;
	
	private final static int FIRST_LINE = 10; 
	private final static int LAST_LINE = 94;
	public ReadATD(File f) {
		this.f = f;
		read();
	}
	
	private void read() {
		MyFileInputStream mis = new MyFileInputStream(f);
		Scanner s = new Scanner(mis.getFileInputStream());
		
		for(int i = 0; i < FIRST_LINE; i++) {
			s.nextLine();
		}
		
		String line;
		String[] split;
		
		double x, y, z, occupancy;
		String name;
		int Z;
		atoms = new JAtom[LAST_LINE - FIRST_LINE];
		for(int i = 0; i < LAST_LINE - FIRST_LINE; i++) {
			s.next();
			name = s.next();
			Z = s.nextInt();
			x = s.nextDouble();
			y = s.nextDouble();
			z = s.nextDouble();
			occupancy = 1;
			if(name.compareTo("N") == 0 ||
					name.compareTo("C") == 0 ||
					name.compareTo("H") == 0) {
				occupancy = 0.25;
			}
			
			atoms[i] = new JAtom(Z, x, y, z, occupancy);
			s.nextLine();
		}
		
		for(int j = 0; j < atoms.length; j++) {
			System.out.println(atoms[j].toString());
		}
	}
	
	public JAtom[] getAtoms() { return atoms; }
	
	public static void main(String[] args) {
		File f = new File("C:\\Users\\eric\\Downloads\\CZX1-cat.atd");
		new ReadATD(f);
	}
}
