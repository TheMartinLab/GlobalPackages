/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import io.StringConverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

public class AddPercentOfWhole {

	private final static int IDX_LINE = 35;
	private final static int VOL_LINE = 50;
	private final static int KA_LINE = 42;
	private final static int PERCENT_LINE = 51;
	private static void outputPercent(double totalVolume, Vector<String[]> linesToOutput, PrintStream ps) {
		double fractionOfWhole;
		String[] parsedLine;
		while(!linesToOutput.isEmpty()) {
			parsedLine = linesToOutput.remove(0);
			fractionOfWhole = Double.valueOf(parsedLine[VOL_LINE]) / totalVolume;
			ps.print(StringConverter.arrayToTabString(parsedLine));
			ps.print(fractionOfWhole + "\n");
		}
	}
	private static void percentOfWhole(Scanner s, PrintStream ps) {
		String wholeLine;
		String[] parsedLine;
		String xtalIdx;
		Vector<String[]> linesToOutput = new Vector<String[]>();
		double volume;
		while(s.hasNextLine()) {
			wholeLine = s.nextLine();
			parsedLine = wholeLine.split("\t");
			linesToOutput.add(parsedLine);
			xtalIdx = parsedLine[IDX_LINE];
			volume = Double.valueOf(parsedLine[VOL_LINE]);
			if(xtalIdx.compareTo("bulk") == 0) {
				outputPercent(volume, linesToOutput, ps);
			}
		}
	}	
	private static void outputWeightedkA(Vector<String[]> linesToOutput, PrintStream ps) {
		double totalWeighted=0, weightedkA;
		String[] parsedLine;
		while(!linesToOutput.isEmpty()) {
			parsedLine = linesToOutput.remove(0);
			weightedkA = Double.valueOf(parsedLine[PERCENT_LINE]) * Double.valueOf(parsedLine[KA_LINE]);
			if(linesToOutput.isEmpty()) {
				ps.print(StringConverter.arrayToTabString(parsedLine));
				ps.print(totalWeighted + "\n");
				totalWeighted = 0;
			} else {
				totalWeighted += weightedkA;
				ps.print(StringConverter.arrayToTabString(parsedLine));
				ps.print(weightedkA + "\n");
			}
			
		}
	}
	private static void volumeWeightedkA(Scanner s, PrintStream ps) {
		String wholeLine;
		String[] parsedLine;
		String xtalIdx;
		Vector<String[]> linesToOutput = new Vector<String[]>();
		while(s.hasNextLine()) {
			wholeLine = s.nextLine();
			parsedLine = wholeLine.split("\t");
			linesToOutput.add(parsedLine);
			xtalIdx = parsedLine[IDX_LINE];
			if(xtalIdx.compareTo("bulk") == 0) {
				outputWeightedkA(linesToOutput, ps);
			}
		}
	}
	public static void main(String[] args) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("addPercentOfWhole.txt");
			fos = new FileOutputStream("addedPercentOfWhole.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner s = new Scanner(fis);
		PrintStream ps = new PrintStream(fos);
		volumeWeightedkA(s, ps);
	}
}
