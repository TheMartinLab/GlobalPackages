package random;

import io.MyFileInputStream;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class ParseAtomicInfo {

	public static void main(String[] args) {
		File input = new File("D:\\$research\\current\\eclipse projects\\MyPackages\\AtomicInfo.txt");
		MyFileInputStream mfis = new MyFileInputStream(input);
		Scanner s = mfis.getScanner();
		int idx;
		String[] splitLine;
		Vector<String> abbrev = new Vector<String>();
		Vector<String> name = new Vector<String>();
		Vector<Double> mass = new Vector<Double>();
		while(s.hasNextLine()) {
			splitLine = s.nextLine().split("\t");
			if(splitLine[0].substring(0, 1).compareTo("#") == 0)
				continue;
			idx = Integer.valueOf(splitLine[0]);
			name.add(splitLine[1]);
			abbrev.add(splitLine[2]);
			mass.add(Double.valueOf(splitLine[3]));
		}
		Vector<Vector<?>> temp = new Vector<Vector<?>>();
		temp.add(abbrev);
		temp.add(name);
		temp.add(mass);
		for(Vector<?> v : temp) {
			String line = "{ ";
			for(int i = 0; i < abbrev.size(); i++) {
				line += "\"" + v.get(i) + "\", ";
				if(line.length() > 100) {
					System.out.println(line);
					line = "";
				}
			}
			if(line.length() > 0)
				System.out.print(line.substring(0, line.length()-2));
			System.out.print(" }");
			System.out.println("\n");
		}
	}
}
