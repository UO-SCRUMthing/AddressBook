package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

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
	    Application app = new Application();
	}
	
	public Application() {
		createNewAddressBook();
		AddressBookGUI gui = new AddressBookGUI(this);
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
		return addressBook.getTemplate();
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
	public List<SimpleEntry<String, String>> itemSelected(int index) {
		return addressBook.getEntry(index).getDetailList();
		
	}

	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Entry> getEntryList() {
		return addressBook.getAll();
	}
}
