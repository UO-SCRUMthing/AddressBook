package edu.uoregon.scrumthing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JFrame;
import javax.swing.UIManager;
import edu.uoregon.scrumthing.swingext.AddressBookGUI;

public class Application extends Controller {
	EntryContainer<Entry> addressBook; // List<EntryContainer<Entry>> if multiple books supported
//	int ACTIVE_BOOK;
	JFrame GUI;
	String filePath;
	private static List<String> parsingFields = Arrays.asList("city", "state", "zip", "address1", "address2", "lastName", "firstName", "phoneNumber", "email");
	private boolean modified = false;
	
	public static void main(String[] args) {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
	    Application app = new Application();
	    app.openAddressBook("src/ABTestSimple.tsv");	
	    app.saveAsAddressBook("src/ABTestSimpleTEST.tsv");
	    app.openAddressBook("src/ABTestSimpleTEST.tsv");	
	    app.saveAsAddressBook("src/ABTestSimpleTEST2.tsv");

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
		// creates new AddressBook to dump data into
		if (!modified) {
			addressBook = new AddressBook();
			return this.importAddressBook(fileName);
		} else {
			// warn user this will wipe all their unsaved data
			addressBook = new AddressBook();
			return this.importAddressBook(fileName);
		}

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
		// TODO Auto-generated method stub
		return false;
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
}
