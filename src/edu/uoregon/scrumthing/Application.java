package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import javax.swing.UIManager;

import edu.uoregon.scrumthing.swingext.AddressBookGUI;
import edu.uoregon.scrumthing.test.TestContainer;

public class Application extends Controller {
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
	public boolean createNewAddressBook() {
		// TODO Auto-generated method stub
		return false;
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
	public boolean addNewEntryToModel(List<SimpleEntry<String, String>> details) {
		// TODO Auto-generated method stub
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
}
