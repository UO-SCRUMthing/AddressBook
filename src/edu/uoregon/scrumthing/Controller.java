package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import edu.uoregon.scrumthing.ControllerPool.ControllerNode;

public abstract class Controller {
	/*
	public static boolean FieldCheck(String field, String data) {
		
		
	}
	*/
	
	public abstract String getAddressBookName();
	public abstract void setAddressBookName(String name);
		//unset data member var
    	//returns true if successful, false otherwise
	
	public abstract Entry createEmptyEntry();
		//SetDetailPane with field info from model
    	//wait for parameterized AddNewEntry	
	
	public abstract void addNewEntry(Entry newEntry);
		// pass details to addressBook data member 
	
	public abstract void updateEntry(int index, HashMap<String, String> details);
		// pass details to entry found at index
	
	public abstract void deleteEntry(int index);
		// delete entry at index
	
	// display operations 
	public abstract List<SimpleEntry<String, String>> itemSelected(int index);
		//set display through SetDetailPane	
	
	public abstract List<Entry> getEntryList();
	
	public abstract void sortBy(String field);
	
	// use to remove itself from the application pool
	public abstract void registerNode(ControllerNode node);
	public abstract void closeAllAddressBook();

	public abstract List<Entry> getEntryList(String searchTerm);
	
	// GUI file menu calls
	public abstract Controller createWindowForNewAddressBook(); 
	// opens a new addressbook that ask user to enter a name for.
	
	public abstract Controller createWindowForExistingAddressBook();
	// opens a existing addressbook stored in a file. return null if failed.

	public abstract boolean createSaveDialog();
	
	public abstract boolean createImportDialog();
	
	public abstract boolean createExportDialog();
		//checks for recorded filename: if found, save to filename; if not request filename from GUI
	
	public abstract boolean createRenameDialog();
	
	public abstract boolean saveCurrentAddressBook();
	
	public abstract boolean closeAddressBook();
		//sets filePath
    	//attempt to save data to filename, returns true if successful, false otherwise.
		//Does not check for file collision, overwrites if filename already exists
	
	// export the address book in required format.
	
	// file operations for models
	public abstract int openAddressBook(String fileName);
	public abstract boolean saveAddressBook(String fileName);
	public abstract int importAddressBook(String fileName);
	public abstract boolean exportAddressBook(String fileName);
}
