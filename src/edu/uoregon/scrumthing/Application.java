package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

import edu.uoregon.scrumthing.swingext.AddressBookGUI;
import edu.uoregon.scrumthing.test.TestContainer;

public class Application extends Controller {
	EntryContainer<Entry> addressBook; // List<EntryContainer<Entry>> if multiple books supported
//	int ACTIVE_BOOK;
	JFrame GUI;
	String filePath;
	private boolean modified = false;
	
	public static void main(String[] args) {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
		TestContainer testData = new TestContainer();
		AddressBookGUI gui = new AddressBookGUI(testData);
	}

	@Override
	public void createNewAddressBook() {
		// TODO create addressbook constructor
		addressBook = new AddressBook();
	}

	@Override
	public boolean openAddressBook(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveAddressBook() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveAddressBook(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeAddressBook() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entry createEmptyEntry() {
		return new Contact();
	}

	@Override
	public boolean addNewEntryToModel(Entry newEntry) {
		return false;
	}

	@Override
	public boolean updateEntry(int index, HashMap<String, String> details) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEntry(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void itemSelected(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}
}
