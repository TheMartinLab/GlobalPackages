/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

import java.io.File;
import java.util.Vector;

public class XRD_InfoManagerTest {

	public static void main(String[] args) {
		File f = new File("D:\\$research\\czx-1 xray kinetics - time-data\\key\\czx-1 xrd key.txt");
		XRD_InfoManager infoManager = new XRD_InfoManager(f);
		
		Vector<XRD_Info> info = infoManager.getInfo();
		
		System.out.println(infoManager.getHeader());
		for(int i = 0; i < info.size(); i++) {
			System.out.println(info.get(i));
		}
	}
}
