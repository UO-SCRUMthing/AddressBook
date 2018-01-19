package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

public abstract class Controller {

	EntryContainer<Entry> addressBook; // List<EntryContainer<Entry>> if multiple books supported
//	int ACTIVE_BOOK;
	JFrame GUI;
	String filePath;
	
	// file operations
	public abstract boolean createNewAddressBook();
    	//creates and sets new address book to DATA    #appends to DATA list if multiple books supported
    	//returns true if successful, false otherwise
	
	public abstract boolean openAddressBook(String fileName);
		//sets FILENAME
    	//returns true if filename successfully found and parsed, false otherwise
	
	public abstract boolean saveAddressBook();
		//checks for recorded filename: if found, save to filename; if not request filename from GUI
	
	public abstract boolean saveAddressBook(String fileName);
		//sets FILENAME
    	//attempt to save data to filename, returns true if successful, false otherwise.
		//Does not check for file collision, overwrites if filename already exists
	
	public abstract boolean closeAddressBook();
		//unset data member var
    	//returns true if successful, false otherwise
	
	public abstract void addNewEntry();
		//SetDetailPane with field info from model
    	//wait for parameterized AddNewEntry	
	
	public abstract boolean addNewEntry(List<SimpleEntry<String, String>> details);
		// pass details to addressBook data member 
		// this two things seem to do very different things...
	
	public abstract boolean updateEntry(int index, HashMap<String, String> details);
		// pass details to entry found at index
	
	public abstract boolean deleteEntry(int index);
		// delete entry at index
	
	// display operations 
	public abstract void itemSelected(int index);
		//set display through SetDetailPane	
	
	


}
