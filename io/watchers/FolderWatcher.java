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
package watchers;
import java.io.File;
import java.util.Observable;
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
public class FolderWatcher extends Observable implements Runnable {

	public enum WhatToWatchFor{
		FOLDER_CREATION,
		NEW_FILE_IN_FOLDER,
		NEW_FOLDER_IN_FOLDER,
	}
	private WhatToWatchFor watchFor;
	private File folder;
	private boolean isWatching;
	private File moveTo;
	private long millisToSleep;

	public FolderWatcher(File folder, WhatToWatchFor watchFor) throws InvalidWatchSelectionException {
		isWatching = true;
		millisToSleep = 10000;
		this.watchFor = watchFor;
		this.folder = folder;
		switch(watchFor) {
			case NEW_FOLDER_IN_FOLDER:
			case NEW_FILE_IN_FOLDER:
				if(!folder.exists())
					throw new InvalidWatchSelectionException("Cannot watch for a new file in folder: " + folder.getAbsolutePath() +
							"because this folder doesn't yet exist.");
			break;
			case FOLDER_CREATION:
			break;
		}
	}
	
	@Override
	public void run() {
		File[] filesInFolder;
		while(isWatching) {
			boolean notify = false;
			WhatToWatchFor whichOne = null;
			switch(watchFor) {
			case NEW_FILE_IN_FOLDER:
				filesInFolder = folder.listFiles();
				if(folder.exists() && filesInFolder != null && filesInFolder.length > 0) {
					notify = true;
					whichOne = WhatToWatchFor.NEW_FILE_IN_FOLDER;
				}
				break;
			case NEW_FOLDER_IN_FOLDER:
				if(folder.exists() && folder.listFiles().length > 0)
					for(File potentialFolder : folder.listFiles())
						if(potentialFolder.isDirectory()) {
							notify = true;
							whichOne = WhatToWatchFor.NEW_FOLDER_IN_FOLDER;
						}
				break;
			case FOLDER_CREATION:
				if(folder.exists()) {
					notify = true;
					whichOne = WhatToWatchFor.FOLDER_CREATION;
				}
			}
			
			
			if(notify) {
				setChanged();
				notifyObservers(new Object[] {whichOne, moveTo});
			}
			try {
				Thread.sleep(millisToSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public WhatToWatchFor getWatchFor() { return watchFor; }
	public void setWatchFor(WhatToWatchFor watchFor) { this.watchFor = watchFor; }
	public File getFolder() { return folder; }
	public void setFolder(File folder) { this.folder = folder; }
	public boolean isWatching() { return isWatching; }
	public void setWatching(boolean isWatching) { this.isWatching = isWatching; }
	public File getMoveTo() { return moveTo; }
	public void setMoveTo(File moveTo) { this.moveTo = moveTo; }
	public long getMillisToSleep() { return millisToSleep; }
	public void setMillisToSleep(long millisToSleep) { this.millisToSleep = millisToSleep; }
}
