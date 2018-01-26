package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JFrame;
import javax.swing.UIManager;
import edu.uoregon.scrumthing.swingext.AddressBookGUI;
import edu.uoregon.scrumthing.test.TestContainer;

public class Application extends Controller {
	@SuppressWarnings("rawtypes")
	EntryContainer addressBook; // List<EntryContainer<Entry>> if multiple books supported
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
	    Application app = new Application();
	}
	
	public Application() {
		createNewAddressBook("New Address Book");
		AddressBookGUI gui = new AddressBookGUI(this);
	}
	
	@Override
	public void createNewAddressBook(String name) {
		addressBook = new AddressBook(name);
	}
	
	@Override
	public void createNewAddressBook() {
		addressBook = new AddressBook();
	}

	@Override
	public boolean openAddressBook(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean importAddressBook(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveAddressBook() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveAddressBook(String filePath) {
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
		return addressBook.getTemplate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addNewEntry(Entry newEntry) {
		modified = true;
		addressBook.addEntry(newEntry);
	}

	@Override
	public void updateEntry(int index, HashMap<String, String> details) {
		modified = true;
		addressBook.getEntry(index).updateDetails(details);
	}

	@Override
	public void deleteEntry(int index) {
		modified = true;
		addressBook.deleteEntry(index);
	}

	@Override
	public List<SimpleEntry<String, String>> itemSelected(int index) {
		return addressBook.getEntry(index).getDetailList();
		
	}

	@Override
	public boolean isChanged() {
		return modified;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entry> getEntryList() {
		return addressBook.getAll();
	}

	@Override
	public void sortBy(String field) {
		addressBook.sortBy(field);
	}
}
