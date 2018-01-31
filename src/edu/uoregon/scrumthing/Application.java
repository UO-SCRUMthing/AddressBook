package edu.uoregon.scrumthing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.UIManager;

import edu.uoregon.scrumthing.ControllerPool.ControllerNode;
import edu.uoregon.scrumthing.swingext.AddressBookGUI;

public class Application extends Controller {
	EntryContainer<Entry> addressBook; 
	AddressBookGUI GUI;
	String filePath;
	private static List<String> parsingFields = Arrays.asList("city", "state", "zip", "address1", "address2", "lastName", "firstName", "phoneNumber", "email");
	private boolean modified = false;
	private String tempFilePath =  System.getProperty("user.home") + File.separator + "scrumthingAddressBookTempFile.tmp";
	
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
	    if (!app.openLastAddressBook()) {
	    	// prompt user to select an address book to open
	    	System.out.println("No temp file, user must select address book to open.");
	    }
//	    app.openAddressBook("src/ABTestSimple.tsv");	
//	    app.saveAsAddressBook("src/ABTestSimpleTEST.tsv");
//	    app.openAddressBook("src/ABTestSimpleTEST.tsv");	
//	    app.saveAsAddressBook("src/ABTestSimpleTEST2.tsv");
	    
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
	
	private boolean openLastAddressBook() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(tempFilePath));
		    String str = reader.readLine();
		    if (str != null) {
		    	importAddressBook(str);	
		    }
		    reader.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private void saveLastAddressBook(File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath));
			writer.append(file.getAbsolutePath());	
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int openAddressBook(String fileName) {
		if (!fileName.endsWith(".tsv")) {
			return -1;
		}
		File file = new File(fileName);
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
		if (successes >= 0) {
			GUI.notice("Opened file: " + file.getName(), 0);
		}
		
		saveLastAddressBook(file);
		return successes;
	}
	
	private Entry createEntryFromLine(String line) {
		String[] parts = line.split("\\t", parsingFields.size());
			
		Contact newContact = null;
		int emptyFields = 0;
		if (parts.length < 8 || (parts[6].isEmpty() && parts[5].isEmpty())) { // line either has too few fields or both name fields are empty
//			System.err.println("too few fields or name fields empty");
			return null;
		} else if (parts[6].isEmpty() || parts[5].isEmpty()) { // if one name field is empty 
			for (String field : parts) {
				if (field.isEmpty()) {
					emptyFields++;
				}
			}
			if (emptyFields > parts.length - 2) { // must have at least 2 fields non empty
//				System.err.println("too many empty fields");
				return null;
			}
		}
		if (parts.length < 9) {
			newContact = new Contact(parts[6], parts[5], parts[3], parts[4], parts[0], parts[1], parts[2], parts[7]);
		} else {
			newContact = new Contact(parts[6], parts[5], parts[3], parts[4], parts[0], parts[1], parts[2], parts[7], parts[8]);
		}
		
		return newContact; 
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
	        		System.err.println("line parse failure");
	        	}
	        } 
	        modified = true;
	        filePath = fileName;
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("File '" + fileName + "' not found");
			successes = -1;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addressBook.sort();
		GUI.updateList();
		return successes;
	}

	@Override
	public boolean saveAddressBook() {
		// TODO: fix save feature + notice
		boolean saved = this.saveAsAddressBook(filePath);
		if (saved) {
			modified = false;
		}
		return saved;
	}

	@Override
	public boolean saveAsAddressBook(String filePath) {
		File file = new File(filePath);
		BufferedWriter writer = null;
		String header = "";
		String body = "";
		boolean success = false;
		try {
			writer = new BufferedWriter(new FileWriter(file));
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
			GUI.notice("Failed to save file: " + e.getMessage(), 2);
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
			GUI.notice("Saved file: " + file.getName(), 0);
			saveLastAddressBook(file);
		}
		return success;
	}

	@Override
	public boolean closeAddressBook() {
		// TODO: warn user;
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
		GUI.notice("New contact added", 0);
		addressBook.sort();
	}

	@Override
	public void updateEntry(int index, HashMap<String, String> details) {
		modified = true;
		addressBook.getEntry(index).updateDetails(details);
		GUI.notice("Contact updated", 0);
	}

	@Override
	public void deleteEntry(int index) {
		modified = true;
		addressBook.deleteEntry(index);
		// TODO: Undo
		GUI.notice("Selected contact deleted", 0);
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
	public List<Entry> getEntryList(String searchTerm) {
		return addressBook.getAll(searchTerm);
	}

	@Override
	public void sortBy(String field) {
		addressBook.sortBy(field);
		GUI.notice("Sorted by " + field, 0);
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
