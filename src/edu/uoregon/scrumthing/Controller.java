package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JFrame;

import edu.uoregon.scrumthing.ControllerPool.ControllerNode;

public abstract class Controller {
	/*
	public static boolean FieldCheck(String field, String data) {
		
		
	}
	*/
	
	public abstract String getAddressBookName();
	
	// file operations
	public abstract Controller createNewAddressBook(String name);
    	//creates and sets new address book to addressBook    
		//appends to addressBook list if multiple books supported
    	//returns true if successful, false otherwise
	
	public abstract Controller createNewAddressBook(); 
	
	public abstract int openAddressBook(String fileName);
		//sets filePath
    	//returns number of entries read correctly if filename successfully found and parsed, -1 if nothing otherwise
	
	public abstract int importAddressBook(String fileName);
	
	public abstract boolean saveAddressBook();
		//checks for recorded filename: if found, save to filename; if not request filename from GUI
	
	public abstract boolean saveAsAddressBook(String fileName);
		//sets filePath
    	//attempt to save data to filename, returns true if successful, false otherwise.
		//Does not check for file collision, overwrites if filename already exists
	
	public abstract boolean closeAddressBook();
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
}
