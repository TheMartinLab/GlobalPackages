/*******************************************************************************
 * Copyright (c) 2013 Eric Dill -- eddill@ncsu.edu. North Carolina State University. All rights reserved.
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Eric Dill -- eddill@ncsu.edu - initial API and implementation
 ******************************************************************************/
package chemistry;

public class JAtomTools {

	
	
	private static double[] mass = new double[] { 1.00794, 4.002602, 6.941, 9.012182, 10.811, 12.0107, 14.0067, 15.9994, 18.9984032, 20.1797, 22.98976928, 
		24.305, 26.9815386, 28.0855, 30.973762, 32.065, 35.453, 39.948, 39.0983, 40.078, 44.955912, 47.867, 50.9415, 
		51.9961, 54.938045, 55.845, 58.933195, 58.6934, 63.546, 65.39, 69.723, 72.64, 74.9216, 78.96, 79.904, 
		83.798, 85.4678, 87.62, 88.90585, 91.224, 92.906, 95.94, 97.9072, 101.07, 102.905, 106.42, 107.8682, 
		112.411, 114.818, 118.71, 121.76, 127.6, 126.904, 131.293, 132.9054519, 137.327, 138.90547, 140.116, 
		140.90765, 144.242, 144.9127, 150.36, 151.964, 157.25, 158.92535, 162.5, 164.93, 167.259, 168.93421, 
		173.04, 174.967, 178.49, 180.94788, 183.84, 186.207, 190.23, 192.217, 195.084, 196.966569, 200.59, 204.3833, 
		207.2, 208.9804, 208.9824, 209.9871, 222.0176, 223.0197, 226.0254, 227.0277, 232.03806, 231.03588, 238.02891, 
		237.0482, 244.0642, 243.0614, 247.0704, 247.0703, 251.0796, 252.083, 257.0951, 258.0984, 259.101, 262.1097, 
		261.1088, 262.0, 266.0, 264.0, 277.0, 268.0, 271.0, 272.0, 285.0, 284.0, 289.0, 288.0, 292.0, 293.0, 
		294.0 };
	private static String[] abbreviation = new String[] { "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", 
		"K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", 
		"Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", 
		"Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", 
		"Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", 
		"Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", 
		"Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Uub", "Uut", "Uuq", "Uup", "Uuh", "Uus", "Uuo" };

	private static String[] name = new String[] { "Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", 
		"Neon", "Sodium ", "Magnesium", "Aluminium ", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", 
		"Potassium ", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron ", "Cobalt", 
		"Nickel", "Copper ", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", 
		"Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", 
		"Silver ", "Cadmium", "Indium", "Tin ", "Antimony ", "Tellurium", "Iodine", "Xenon", "Caesium ", "Barium", 
		"Lanthanum", "Cerium", "Praseodymium", "Neodymium", "Promethium", "Samarium", "Europium", "Gadolinium", 
		"Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", 
		"Tungsten ", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold ", "Mercury ", "Thallium", "Lead ", "Bismuth", 
		"Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinium", "Thorium", "Protactinium", "Uranium", 
		"Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", 
		"Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", 
		"Meitnerium", "Darmstadtium", "Roentgenium", "Ununbium", "Ununtrium", "Ununquadium", "Ununpentium", "Ununhexium", 
		"Ununseptium", "Ununoctium" };
	
	/**
	 * This method is not implemented.
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean makeBond(JAtom one, JAtom two) {
		assert one != null : "Method: JAtomTools.makeBond(JAtom, JAtom). Error: atom one is null";
		assert two != null : "Method: JAtomTools.makeBond(JAtom, JAtom). Error: atom two is null";
//		boolean bond1to2 = one.addBondedAtom(two);
//		boolean bond2to1 = two.addBondedAtom(one);
//		return bond1to2 && bond2to1;
		return false;
	}
	public static String getAbbreviation(int Z) {
		assert abbreviation != null : "JAtomTools.getAbbreviation(int): abbreviation array is null.";
		assert Z < abbreviation.length : "Z selection in JAtomTools.getAbbreviation(" + Z + ") is out of range.";
		return abbreviation[Z-1];
	}
	public static String getName(int Z) {
		assert name != null : "JAtomTools.getName(int): name array is null.";
		assert Z < name.length : "Z selection in JAtomTools.getName(" + Z + ") is out of range.";
		return name[Z-1];
	}
	public static double getMass(int Z) {
		assert mass != null : "JAtomTools.getMass(int): mass array is null.";
		assert Z < mass.length : "Z selection in JAtomTools.getMass(" + Z + ") is out of range.";
		return mass[Z-1];
	}
}
