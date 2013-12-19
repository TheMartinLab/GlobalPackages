/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package readFile;

import java.io.File;

public class TestPDFGetX3Output {

	public static void main(String[] args) {
		File f = new File("D:\\Documents referenced in lab notebooks\\Dill-12\\34\\a\\out\\czx1-10_230-145-#2_90kev_4s_139f.cor.000.gr");
		PDFGetX3Output o = new PDFGetX3Output(f);
		o.run();
		double[] x = o.getX();
		double[] y = o.getY();
		for(int i = 0; i < x.length; i++) {
			System.out.println(x[i] + "\t" + y[i]);
		}
	}
}
