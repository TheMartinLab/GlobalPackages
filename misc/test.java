import geometry.JVector;


public class test {

	public static void main(String[] args) {
		JVector v1 = new JVector(10,28,0);
		JVector v2 = new JVector(19,22,0);
		JVector v3 = new JVector(-22,15,0);
		System.out.println(JVector.angleDegrees(v1, v2));
		System.out.println(JVector.angleDegrees(v2, v3));
	}
}
