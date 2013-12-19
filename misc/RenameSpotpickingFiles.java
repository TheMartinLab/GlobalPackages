/*******************************************************************************
 * Copyright (c) 2013 Eric Dill -- eddill@ncsu.edu. North Carolina State University. All rights reserved.
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Eric Dill -- eddill@ncsu.edu - initial API and implementation
 ******************************************************************************/
import java.io.File;


public class RenameSpotpickingFiles implements Runnable {

	private String strFolderTarget = "Spotpicked ";
	private String strFileTarget = "spotpicked";
	private String searchFolderRoot = "D:\\Data\\aps 09\\";
	public void run() {
		File root = new File(searchFolderRoot);
		File[] filesInRoot = root.listFiles();
		
		for(int i = 0; i < filesInRoot.length; i++) {
			if(filesInRoot[i].isDirectory()) {
				String replaceWith = filesInRoot[i].getName() + " -- ";
				File[] filesInCurFolder = filesInRoot[i].listFiles();
				for(int j = 0; j < filesInCurFolder.length; j++) {
					if(filesInCurFolder[j].isDirectory() && filesInCurFolder[j].getName().contains(strFolderTarget)) {
						File[] filesInTargetFolder = filesInCurFolder[j].listFiles();
						for(int k = 0; k < filesInTargetFolder.length; k++) {
							if(filesInTargetFolder[k].getName().contains(strFileTarget)) {
								String oldName = filesInTargetFolder[k].getAbsolutePath();
								String newName = oldName.replace(strFileTarget, replaceWith);
								filesInTargetFolder[k].renameTo(new File(newName));
							}
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new RenameSpotpickingFiles().run();
	}
}
