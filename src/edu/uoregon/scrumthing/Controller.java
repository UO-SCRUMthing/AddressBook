package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JFrame;

public abstract class Controller {

	EntryContainer<Entry> addressBook; // List<EntryContainer<Entry>> if multiple books supported
//	int ACTIVE_BOOK;
	JFrame GUI;
	String filePath;
	private boolean modified = false;
	
	/**
	 * Return true if the content have be changed after last save.
	 */
	public abstract boolean isChanged();
	
	// file operations
	public abstract void createNewAddressBook(String name);
    	//creates and sets new address book to addressBook    
		//appends to addressBook list if multiple books supported
    	//returns true if successful, false otherwise
	
	public abstract void createNewAddressBook(); 
	
	public abstract boolean openAddressBook(String fileName);
		//sets filePath
    	//returns true if filename successfully found and parsed, false otherwise
	
	public abstract boolean importAddressBook(String fileName);
	
	public abstract boolean saveAddressBook();
		//checks for recorded filename: if found, save to filename; if not request filename from GUI
	
	public abstract boolean saveAddressBook(String fileName);
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
}
