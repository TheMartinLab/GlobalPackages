/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
import java.io.File;


public class AddSuffix {

	private File directory;
	private String newSuffix;
	public AddSuffix(File directory, String newSuffix) {
		this.directory = directory;
		this.newSuffix = newSuffix;
	}
	public void addSuffix() {
		File[] files = directory.listFiles();
		if(files == null) { return; }
		String period = "";
		if(newSuffix.substring(0, 1).compareTo(".") != 0) {
			period = ".";
		}
		for(int i = 0; i < files.length; i++) {
			files[i].renameTo(new File(files[i].toString() + period + ".001"));
		}
	}
	
	public static void main(String[] args) {
		String name = "Z:\\DATA\\bnl 04-06\\CZX1b\\270115a7\\u";
		File directory = new File(name);
		String newSuffix = ".001";
		AddSuffix ad = new AddSuffix(directory, newSuffix);
		ad.addSuffix();
	}
}
