package edu.uoregon.scrumthing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.uoregon.scrumthing.ControllerPool.ControllerNode;
import edu.uoregon.scrumthing.swingext.AddressBookGUI;

public class Application extends Controller {
	private static final String SaveFileExtension = "sab";
	private static final String ImportFileExtension = "tsv";
	
	EntryContainer<Entry> addressBook; 
	AddressBookGUI GUI;
	
	private String filePath;
	private static List<String> parsingFields = Arrays.asList("city", "state", "zip", "address1", "address2", "lastName", "firstName", "phoneNumber", "email");
	private boolean modified = false;
	private String tempFilePath = System.getProperty("user.home") + File.separator + "scrumthingAddressBookTempFile.tmp";
	
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
	    Application app = new Application(null);
	    appPool.add(app);
	    if (!app.loadLastAddressBook()) {
    			app.closeAddressBook();
    			app = (Application)app.createWindowForNewAddressBook();
	    } else {
	    	app.GUI.setVisible(true);
	    }
	    
//	    app.openAddressBook("src/ABTestSimple.tsv");	
//	    app.saveAsAddressBook("src/ABTestSimpleTEST.tsv");
//	    app.openAddressBook("src/ABTestSimpleTEST.tsv");	
//	    app.saveAsAddressBook("src/ABTestSimpleTEST2.tsv");

	}
	
	public Application(JFrame relative) {
		addressBook = new AddressBook();
		GUI = new AddressBookGUI(relative, this);
	}
	
	public Application(JFrame relative, String name) {
		addressBook = new AddressBook(name);
		GUI = new AddressBookGUI(relative, this);
	}
	
	private boolean loadLastAddressBook() {
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

	public int openAddressBook(String fileName) {
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
	public boolean saveAddressBook(String filePath) {
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
			this.filePath = filePath;
			GUI.notice("Saved file: " + file.getName(), 0);
			saveLastAddressBook(file);
		}
		return success;
	}

	@Override
	public boolean closeAddressBook() {
		if (modified) {
			int n = JOptionPane.showConfirmDialog(
				    GUI,
				    "Do you want to save the address book?",
				    this.getAddressBookName(),
				    JOptionPane.YES_NO_CANCEL_OPTION);
			
			switch (n) {
			case JOptionPane.YES_OPTION:
				if (!this.saveCurrentAddressBook()) return false;
				break;
			case JOptionPane.CANCEL_OPTION:
				return false;
			}
		}
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
	
	@Override
	public boolean exportAddressBook(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAddressBookName(String name) {
		addressBook.setAddressBookName(name);
		GUI.setTitle(name);
	}

	@Override
	public Controller createWindowForNewAddressBook() {
		Application newApp = new Application(GUI);
		appPool.add(newApp);
		newApp.GUI.setVisible(true);
		
		if (!newApp.createRenameDialog()) {
			newApp.closeAddressBook();
			newApp = null;
		}
		return newApp;
	}

	@Override
	public Controller createWindowForExistingAddressBook() {
		JFileChooser fileDiag = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Address Book File (*."+SaveFileExtension+")", SaveFileExtension);
		fileDiag.setFileFilter(filter);
	    int returnVal = fileDiag.showOpenDialog(GUI);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    		// Open file and notice any failure
	    		String filename = fileDiag.getSelectedFile().getAbsolutePath();
	    		
	    		Application newApp = new Application(GUI);

	    		if (openAddressBook(filename) < 0) {
	    			GUI.notice("Failed to open file: " + fileDiag.getSelectedFile().getName(), 2);
	    			newApp.GUI.dispose();
	    			return null;
	    		} else {
	    			GUI.notice("New window opened.", 0);
	       			newApp.GUI.setVisible(true);
	    			appPool.add(newApp);
	    			return newApp;
	    		}
	    }
		return null;
	}

	@Override
	public boolean createSaveDialog() {
		JFileChooser fileDiag = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Address Book File (*."+SaveFileExtension+")", SaveFileExtension);
		fileDiag.setFileFilter(filter);
		fileDiag.setSelectedFile(new File(getAddressBookName() + "." + SaveFileExtension));
	    int returnVal = fileDiag.showSaveDialog(GUI);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    		// Add a extension if there isn't one
	    	   	String filename = fileDiag.getSelectedFile().getAbsolutePath();
	    	   	if (!filename .endsWith("."+SaveFileExtension)) filename += "."+SaveFileExtension;
	    	   	return saveAddressBook(filename);
	    }
		return false;
	}

	@Override
	public boolean saveCurrentAddressBook() {
		if (filePath == null || filePath.length() <= 0) {
			return createSaveDialog();
		}
		else {
			return saveAddressBook(filePath);
		}
	}

	@Override
	public boolean createRenameDialog() {
		String newAddressBookName = (String)JOptionPane.showInputDialog(
                GUI,
                "Please enter a name",
                "New Address Book",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                getAddressBookName());

		if ((newAddressBookName != null) && (newAddressBookName.length() > 0)) {
			setAddressBookName(newAddressBookName);
			modified = true;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean createImportDialog() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExportDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
