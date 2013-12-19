/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package scattering;

public class ComplexScatteringFactorTest {

	public static void main(String[] args)
	{
		ComplexScatteringFactor csf = new ComplexScatteringFactor();
		
		
		double wavelength = .13702;
		
		try {
			System.out.println(csf.getComplexF(1, wavelength, 6));
			System.out.println(csf.getComplexF(1, .13702, 35));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	/*	
		JVector vx = new JVector(1, 0, -1);
		
		JVector vy = new JVector(0, -1, 1);
		for(double j = 10; j < 31; j += 5)
		{
			Date d1 = new Date();
			
			double qStep = 1./j;
			
			HashMap<Double, Complex> hm = csf.buildHashMap(qMax, qStep, wavelength, 6, vx, vy);
			//TreeMap<Double, Complex> tm = dm.buildTreeMap(qMax, qStep, wavelength, 6);
			int counter = 0;
			
			for(double qy = 0; qy < qMax; qy+= qStep)
			{
				for(double qx = 0; qx < qMax; qx += qStep)
				{
					JVector Qx = JVector.multiply(vx, qx);
					
					JVector Qy = JVector.multiply(vy, qy);
					
					JVector Q = JVector.add(Qx, Qy);
					
					double key = Q.length();
					
					hm.get(key);
					
					counter++;
				}
			}
			Date d2 = new Date();
			
			System.out.println(j + "\t" + counter + "\t" + (d2.getTime() - d1.getTime()));
		}*/
	}
}
