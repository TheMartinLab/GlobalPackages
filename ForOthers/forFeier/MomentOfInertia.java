/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package forFeier;
/**
 * Calculates moment of inertia of differently deuterated templates in CZX-1
 */
public class MomentOfInertia {
	public static final double MASS_N = 14.01;
	public static final double MASS_C = 12.01;
	public static final double MASS_H = 1.008;
	public static final double MASS_D = 2.008;
	
	public static void main(String[] args) {
		//Define the coordinates of the axis and atoms
		double[] A = new double[3];
		double[] B = new double[3];
		double[] N = new double[3];
		double[][] C = new double[3][3];
		double[][] H = new double[10][3];
		
		//define the axis
		for(int i = 0; i < 3; i++) {
			A[i] = 0;
		}
		B[0] = (-3.98664555 - 1.38288422 - 1.38288422)/3;
		B[1] = (1.38288422 + 1.38288422 + 3.98664555)/3;
		B[2] = (-1.38288422 - 3.98664555 - 1.38288422)/3;
		
		//define the atoms
		N[0] = -0.76873962;
		N[1] = -0.76873962;
		N[2] = 0.76873962;
		
		C[0][0] = -1.08851836;
		C[0][1] = -1.08851836;
		C[0][2] = -0.78250493;
		
		C[1][0] = -1.08851836;
		C[1][1] = 0.78250493;
		C[1][2] = 1.08851836;
		
		C[2][0] = 0.78250493;
		C[2][1] = -1.08851836;
		C[2][2] = 1.08851836;
		
		H[0][0] = -1.3235875;
		H[0][1] = -1.3235875;
		H[0][2] = 1.3235875;
		
		H[1][0] = -0.96463057;
		H[1][1] = -2.06161989;
		H[1][2] = -0.88097984;
		
		H[2][0] = -2.06161989;
		H[2][1] = -0.88097984;
		H[2][2] = -0.96463057;
		
		H[3][0] = -0.50508099;
		H[3][1] = -0.50508099;
		H[3][2] = -1.3659423;
		
		H[4][0] = -0.88097984;
		H[4][1] = 0.96463057;
		H[4][2] = 2.06161989;
		
		H[5][0] = -2.06161989;
		H[5][1] = 0.96463057;
		H[5][2] = 0.88097984;
		
		H[6][0] = -0.50508099;
		H[6][1] = 1.3659423;
		H[6][2] = 0.50508099;
		
		H[7][0] = 0.96463057;
		H[7][1] = -2.06161989;
		H[7][2] = 0.88097984;
		
		H[8][0] = 0.96463057;
		H[8][1] = -0.88097984;
		H[8][2] = 2.06161989;
		
		H[9][0] = 1.3659423;
		H[9][1] = -0.50508099;
		H[9][2] = 0.50508099;
		
		//calculate rho squared for each atom
		double rho_N_2 = distanceSquared(A, B, N);
		double[] rho_C_2 = new double[3];
		for(int i = 0; i < 3; i++) {
			rho_C_2[i] = distanceSquared(A, B, C[i]);
		}
		double[] rho_H_2 = new double[10];
		for(int i = 0; i < 10; i++) {
			rho_H_2[i] = distanceSquared(A, B, H[i]);
		}

		//calculate moment of inertia for d0, d1, d9, d10-TMA
		double IN = MASS_N * rho_N_2;
		double[] IC = new double[3];
		for(int i = 0; i < 3; i++) {
			IC[i] = MASS_C * rho_C_2[i];
		}
		double[] IH = new double[10];
		for(int i = 0; i < 10; i++) {
			IH[i] = MASS_H * rho_H_2[i];
		}
		double[] ID = new double[10];
		for(int i = 0; i < 10; i++) {
			ID[i] = MASS_D * rho_H_2[i];
		}

		double I_d0 = IN;
		double I_d1 = IN;
		double I_d9 = IN;
		double I_d10 = IN;
		for(int i = 0; i < 3; i++) {
			I_d0 += IC[i];
			I_d1 += IC[i];
			I_d9 += IC[i];
			I_d10 += IC[i];
		}
		for(int i = 0; i < 10; i++) {
			I_d0 += IH[i];
			I_d10 += ID[i];
		}
		I_d1 += ID[0];
		for(int i = 1; i < 10; i++) {
			I_d1 += IH[i];
		}
		I_d9 += IH[0];
		for(int i = 1; i < 10; i++) {
			I_d9 += ID[i];
		}
		
		System.out.println("Moment of Inertia of d0-TMA is: " + I_d0);
		System.out.println("Moment of Inertia of d1-TMA is: " + I_d1);
		System.out.println("Moment of Inertia of d9-TMA is: " + I_d9);
		System.out.println("Moment of Inertia of d10-TMA is: " + I_d10);
	}
	
	//Calculate the cross product of AB and CA
	public static double[] crossProduct(double[] pointA, double[] pointB, double[] pointC) {
		double[] AB = new double[3];
		double[] CA = new double[3];
		double[] cross = new double[3];
		for(int i = 0; i < 3; i++) {
			AB[i] = pointB[i] - pointA[i];
			CA[i] = pointA[i] - pointC[i];
		}
		cross[0] = AB[1]*CA[2]-AB[2]*CA[1];
		cross[1] = AB[2]*CA[0]-AB[0]*CA[2];
		cross[2] = AB[0]*CA[1]-AB[1]*CA[0];
		
		return cross;
	}
	
	//Calculate the distance squared from A to B
	public static double lineDistanceSquared(double[] AB) {
		double distance = 0;
		for(int i = 0; i < 3; i++) {
			distance += AB[i]*AB[i];
		}
		return distance;
	}
	
	//Calculate the distance squared from point C to line AB
	public static double distanceSquared(double[] pointA, double[] pointB, double[] pointC) {
		double[] cross = crossProduct(pointA, pointB, pointC);
		double[] AB = new double[3];
		for(int i = 0; i < 3; i++) {
			AB[i] = pointB[i] - pointA[i];
		}
		double distance_squared = lineDistanceSquared(cross)/lineDistanceSquared(AB);
		return distance_squared;
	}
}