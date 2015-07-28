package com.andreistraut.monkins.model;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class FileAccessHelper {
    
    public static String getConfigFilePath() throws IOException {
	return resolveSettingsFolderPath() + System.getProperty("file.separator") + "config.json";
    }

    private static String resolveSettingsFolderPath() throws IOException {
	//First check for writeable folders in the defined path list, remove non-writeable
	LinkedList<String> possibleFolders = getPossibleSettingsFolderPaths();
	Iterator<String> it = possibleFolders.iterator();
	while (it.hasNext()) {
	    String possibleLocation = it.next();
	    File possibleFolder = new File(possibleLocation);
	    
	    if (!possibleFolder.canWrite()) {
		it.remove();
	    }
	}
	
	if(possibleFolders.isEmpty()) {
	    throw new IOException("No possible writeable folders found to store settings");
	}

	//In the list of writeable folders, check if any of them already have a defined monkins folder. If yes, return it
	it = possibleFolders.iterator();
	while (it.hasNext()) {
	    String possibleLocation = it.next();
	    File settingsFile = new File(possibleLocation);
	    
	    if (settingsFile.exists()) {
		return possibleLocation;
	    }
	}
	
	//If not existing settings folder is found, return the first on the list, it's as good as any
	return possibleFolders.getFirst();
    }

    private static LinkedList<String> getPossibleSettingsFolderPaths() {
	LinkedList<String> possibleLocs = new LinkedList<String>();

	possibleLocs.add(System.getProperty("user.home") + System.getProperty("file.separator")
		+ ".monkins");
	possibleLocs.add(System.getProperty("user.dir") + System.getProperty("file.separator")
		+ ".monkins");
	possibleLocs.add(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
		+ ".monkins");

	return possibleLocs;
    }
}
