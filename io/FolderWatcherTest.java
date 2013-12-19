import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */

/**
 * @author Eric
 *
 */
public class FolderWatcherTest implements Observer {

	private FolderWatcher fw;
	public FolderWatcherTest(File folderToWatch) {
		try {
			fw = new FolderWatcher(folderToWatch, FolderWatcher.WhatToWatchFor.NEW_FILE_IN_FOLDER);
		} catch (InvalidWatchSelectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fw.addObserver(this);
		fw.setMoveTo(new File(folderToWatch.getParentFile() + File.separator + "analyzed"));
		Thread t = new Thread(fw);
		t.start();
	}
	private void calcDiffraction(File f) {
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof FolderWatcher) {
			FolderWatcher fw = (FolderWatcher) arg0;
			if(arg1 instanceof Object[]) {
				Object[] obj = (Object[]) arg1;
				if(obj[0] instanceof FolderWatcher.WhatToWatchFor) {
					File moveTo, folder;
					File[] newFiles;
					Vector<File> actualNewFiles;
					switch((FolderWatcher.WhatToWatchFor) obj[0]) {
					case FOLDER_CREATION:
						System.out.println("The folder was created!");
						break;
					case NEW_FILE_IN_FOLDER:
						moveTo = (File) obj[1];
						folder = fw.getFolder();
						newFiles = folder.listFiles();
						actualNewFiles = new Vector<File>();
						for(File f : folder.listFiles()) {
							if(!f.isDirectory())
								actualNewFiles.add(f);
						}
						newFiles = new File[actualNewFiles.size()];
						newFiles = actualNewFiles.toArray(newFiles);
						String type = newFiles.length > 1 ? " file has" : " files have";
						System.out.println(newFiles.length + type + " appeared. ");
						if(moveTo != null)
							for(File file: newFiles) {
								File newFileName = new File(moveTo + File.separator + file.getParentFile().getName() + File.separator);
								newFileName.mkdirs();
								newFileName = new File(newFileName + File.separator + file.getName());
								if(file.renameTo(newFileName))
									System.out.println("Moving: " + file.getAbsolutePath() + "\n\tto:" + newFileName.getAbsolutePath());
								calcDiffraction(file);
							}
						break;
					case NEW_FOLDER_IN_FOLDER:
						moveTo = (File) obj[1];
						folder = fw.getFolder();
						newFiles = folder.listFiles();
						actualNewFiles = new Vector<File>();
						for(File f : folder.listFiles()) {
							if(f.isDirectory())
								actualNewFiles.add(f);
						}
						newFiles = new File[actualNewFiles.size()];
						newFiles = actualNewFiles.toArray(newFiles);
						System.out.println(newFiles.length + " file(s) have appeared. ");
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		File folderToWatch = new File("C:\\Users\\Eric\\Desktop\\test\\testInnerFolder");
		FolderWatcherTest fwt = new FolderWatcherTest(folderToWatch);
	}
}