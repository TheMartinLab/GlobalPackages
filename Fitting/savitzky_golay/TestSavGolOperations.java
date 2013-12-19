/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package savitzky_golay;

public class TestSavGolOperations {

	public static void main(String[] args) {
		double[][] data = null;
		SavGolOperations sgo = new SavGolOperations(data, new SavitzkyGolay(SavitzkyGolay.ORDER.THIRD_ORDER, SavitzkyGolay.FIVE_POINT));
	}
}
