package edu.uoregon.scrumthing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JFrame;
import javax.swing.UIManager;

import edu.uoregon.scrumthing.ControllerPool.ControllerNode;
import edu.uoregon.scrumthing.swingext.AddressBookGUI;

public class Application extends Controller {
	EntryContainer<Entry> addressBook; // List<EntryContainer<Entry>> if multiple books supported
//	int ACTIVE_BOOK;
	AddressBookGUI GUI;
	String filePath;
	private static List<String> parsingFields = Arrays.asList("city", "state", "zip", "address1", "address2", "lastName", "firstName", "phoneNumber", "email");
	private boolean modified = false;
	
	ControllerNode node;
	
	// Application Pool
	public static ControllerPool appPool;
	
	public static void main(String[] args) {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
	    
	    // TODO: check recent file / empty name
	    appPool = new ControllerPool();
	    Application app = new Application();
	    
	    // Add the initial application to the pool
	    appPool.add(app);

	}
	
	public Application() {
		this("New Address Book");
	}
	
	public Application(String name) {
		addressBook = new AddressBook(name);
		GUI = new AddressBookGUI(this);
	}
	
	@Override
	public Controller createNewAddressBook(String name) {
		Controller newApp = new Application(name);
		appPool.add(newApp);
		return newApp;
	}
	
	@Override
	public Controller createNewAddressBook() {
		return createNewAddressBook("New Address Book");
	}
	
	private Entry createEntryFromLine(String line) {
		String[] parts = line.split("\\t", 9);
			
		Contact temp = null;
		try {
			temp = new Contact(parts[6], parts[5], parts[3], parts[4], parts[0], parts[1], parts[2], parts[7], parts[8]);
		} catch (ArrayIndexOutOfBoundsException e) {
			temp = new Contact(parts[6], parts[5], parts[3], parts[4], parts[0], parts[1], parts[2], parts[7]);
//			e.printStackTrace();
		}
		return temp; 
	}

	@Override
	public int openAddressBook(String fileName) {
		System.out.println(fileName);
		// creates new AddressBook to dump data into
		int successes = -1;
		if (!modified) {
			addressBook = new AddressBook();
			successes = this.importAddressBook(fileName);
		} else {
			// warn user this will wipe all their unsaved data
			addressBook = new AddressBook();
			successes = this.importAddressBook(fileName);
		}
		GUI.updateList();
		return successes;
	}
	
	@Override
	public int importAddressBook(String fileName) {
		BufferedReader reader = null;
		int successes = 0;
		try {
			reader = new BufferedReader(new FileReader(fileName));
	        String str;
	        str = reader.readLine(); // skips header line 
	        while ((str = reader.readLine()) != null) {
	        	Entry e = createEntryFromLine(str);
	        	if (e != null) {
	        		e.toTabString();
	        		addressBook.addEntry(e);
	        		successes++;
	        	} else {
	        		System.out.println("line parse failure");
	        	}
	        } 
	        modified = true;
	        filePath = fileName;
		} catch (IOException e) {
			e.printStackTrace();
			successes = -1;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addressBook.sort();
		return successes;
	}

	@Override
	public boolean saveAddressBook() {
		boolean saved = this.saveAsAddressBook(filePath);
		if (saved) {
			modified = false;
		}
		return saved;
	}

	@Override
	public boolean saveAsAddressBook(String filePath) {
		BufferedWriter writer = null;
		String header = "";
		String body = "";
		boolean success = false;
		try {
			writer = new BufferedWriter(new FileWriter(filePath));
			for (String fieldName : parsingFields) {
				header += fieldName.toUpperCase() + "\t";
			}
			header += "\n";
			for (Entry contact : addressBook.getAll()) {
				body += contact.toTabString() + "\n";
			}
			writer.append(header + body);
			success = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (success) {
			modified = false;
		}
		return success;
	}

	@Override
	public boolean closeAddressBook() {
		// TODO: warn use;
		GUI.dispose();
		node.removeSelf();
		return true;
	}

	@Override
	public Entry createEmptyEntry() {
		return addressBook.getTemplate();
	}

	@Override
	public void addNewEntry(Entry newEntry) {
		modified = true;
		addressBook.addEntry(newEntry);
		addressBook.sort();
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
	public List<Entry> getEntryList() {
		return addressBook.getAll();
	}

	@Override
	public void sortBy(String field) {
		addressBook.sortBy(field);
	}

	@Override
	public String getAddressBookName() {
		return addressBook.getAddressBookName();
	}

	@Override
	public void registerNode(ControllerNode node) {
		this.node = node;
	}

	@Override
	public void closeAllAddressBook() {
		appPool.closeAll();
	}
}
