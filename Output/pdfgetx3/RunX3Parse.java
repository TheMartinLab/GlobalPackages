/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package pdfgetx3;

import java.io.File;
import java.util.Vector;

/**
 * @author Eric
 *
 */
public class RunX3Parse {

	public static void main(String[] args) {
		ParseX3Output_v2 parse = new ParseX3Output_v2();
		
		String drive = "C:";
		String extension = ".gr";
		String inputPath = "\\$temp\\pdfgetx3\\12-34-a\\out\\230-80-#2\\";
		String outputFolderName = "SVD";
		String verboseOutputFolderName = "verbose";
		String summaryFileName = "SVD Summary";

		
		int startIdx = 1;
		int finishIdx = 7;
		
		File[] inputFolders;
		File inputFolder, outputFolder, verboseOutputFolder;
		
		for(int i = startIdx; i <= finishIdx; i++) {
			inputFolder = new File(drive + inputPath + startIdx + File.separator);
			inputFolders = inputFolder.listFiles();
			
			outputFolder = new File(inputFolder + outputFolderName);
			outputFolder.mkdir();
			verboseOutputFolder = new File(outputFolder + File.separator + verboseOutputFolderName);
			outputFolder.mkdir();

			
			
			parse.setSummaryFileRoot(summaryFileName);
			parse.setOutputFolder(outputFolder);
			parse.setVerboseOutputFolder(verboseOutputFolder);
			parse.setInputFolders(inputFolders);
			
			parse.run();
		}
		
	}
}
