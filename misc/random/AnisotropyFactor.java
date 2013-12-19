/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package random;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class AnisotropyFactor {

	private int volume;
	private PrintStream ps;
	public AnisotropyFactor(int vol, PrintStream ps) {
		volume = vol;
		this.ps = ps;
	}
	public void run() {
		int vol;
		double factor = 0, max;
		for(int x = 0; x < volume; x++) {
			for(int y = 0; y < volume; y++) {
				for(int z = 0; z < volume; z++) {
					vol = x * y * z;
					if(vol == volume) {
						max = x;
						if(max < y) { max = y; }
						if(max < z) { max = z; }
						factor = ((double) (x+y+z)) / max;
						ps.println(x + "\t" + y + "\t" + z + "\t" + factor);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		int volumeTarget = 10000;
		FileOutputStream fos = new FileOutputStream("AnisotropyFactor.txt");
		PrintStream ps = new PrintStream(fos);
		AnisotropyFactor af = new AnisotropyFactor(volumeTarget, ps);
		af.run();
		
	}
}
