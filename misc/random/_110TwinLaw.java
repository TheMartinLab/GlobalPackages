package random;

import geometry.JVector;

public class _110TwinLaw {

	public static void main(String[] args) {
		JVector[] v110s1 = JVector.v110s;
		
		JVector[] v110s2 = new JVector[v110s1.length];
		
		for(int i = 0; i < v110s1.length; i++) {
			v110s2[i] = JVector.rotate(v110s1[i], JVector.xy, JVector.zero, 90);
		}
		
		
	}
}
