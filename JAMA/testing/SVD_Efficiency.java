package testing;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

import jama.Matrix;

public class SVD_Efficiency {

	public static void main(String[] args) {
		int[] size2 = new int[] {100};
		int[] size1 = new int[] {1000, 5000, 10000, 25000, 50000, 100000};
		Matrix m;
		double[][] d;
		Random r = new Random();
		long t1, t2;
		for(int b = 0; b < size2.length; b++) {
			System.out.print("\t" + size2[b]);
		}
		System.out.println();
		for(int a = 0; a <size1.length; a++) {
			System.out.print(size1[a] + "\t");
			for(int b = 0; b < size2.length; b++) {
				d = new double[size1[a]][size2[b]];
				
				for(int j = 0; j < d.length; j++) {
					for(int k = 0; k < d[j].length; k++) {
						d[j][k] = r.nextDouble();
					}
				}
				m = new Matrix(d);
				t1 = System.currentTimeMillis();
				m.svd();
				t2 = System.currentTimeMillis();
				
				System.out.print((t2-t1) + "\t");
			}
			System.out.println();
		}
	}
}
