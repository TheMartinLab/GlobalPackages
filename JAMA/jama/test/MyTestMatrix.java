
package jama.test;

import jama.Matrix;

public class MyTestMatrix {

	public static void main(String[] args) {
		double[][] vals = { {1, 3, 3}, {1, 4, 3}, {1, 3, 4} };
		Matrix m = new Matrix(vals);
		
		Matrix inv = m.inverse();
		
		System.out.println("Matrix m:");
		m.print(5, 2);
		
		System.out.println("inverse of m:");
		inv.print(5, 2);
		
		System.out.println("m * m^-1");
		Matrix mult = m.times(inv);
		mult.print(5, 2);
		
		vals = new double[][] {{0.602533729,	0.517948359,	0.698682755,	0.283334091, 0.175799139,	0.887542819},
		{0.841965221,	0.813318865,	0.615475233,	0.911229809, 0.714828881,	0.499945949},
		{0.405399624,	0.193790035,	0.490083951,	0.556255089, 0.66322629,	0.287102999},
		{0.971199563,	0.247063541,	0.477522083,	0.617408931, 0.58613526,	0.89430832},
		{0.717145996,	0.369714178,	0.930134299,	0.390331367, 0.358440609,	0.564375107},
		{0.583754326,	0.75899946,	0.197215288,	0.519592486, 0.441969924,	0.783689554}};
		
		System.out.println("inverse of random number matrix: ");
		m = new Matrix(vals);
		inv = m.inverse();
		inv.print(14, 15);

	}
}
