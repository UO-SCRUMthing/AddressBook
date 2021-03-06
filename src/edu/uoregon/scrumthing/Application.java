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
	private static final String tempFilePath = System.getProperty("user.home") + File.separator + "scrumthingAddressBookTempFile.tmp";
	
	EntryContainer<Entry> addressBook; 
	AddressBookGUI GUI;
	Entry selectedItem;
	int selectedIndex;
	
	private String filePath;
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
		
		appPool = new ControllerPool();
		Application app = new Application(null);
		appPool.add(app);
		if (!app.loadLastAddressBook()) {
				Object[] options = {"Create a new address book",
		                "Open a existing address book",
		                "Quit"};
				int n = JOptionPane.showOptionDialog(null, "What would you like to do?",
					    "SCRUMthing Address Book",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[0]);
				if (n == JOptionPane.YES_OPTION) {
					app.createWindowForNewAddressBook();
					app.closeAddressBook();
				} else if (n == JOptionPane.NO_OPTION) {
					Controller newApp = app.createWindowForExistingAddressBook();
					if (newApp == null) {
						app.modified = true;
						app.GUI.setVisible(true);
					}
				}
				
		} else {
			app.GUI.setVisible(true);
		}
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
		int result = -1;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(tempFilePath));
			String str = reader.readLine();
			if (str.trim() != null) {
				result = openAddressBook(str);
			}
			reader.close();
		} catch (IOException e) {
			return false;
		}
		
		if (result < 0) {
			return false;
		}
		return true;
	}
	
	private void saveLastAddressBook(File file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tempFilePath));
			writer.append(file.getAbsolutePath());	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
				if (field.trim().isEmpty()) {
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
	public SimpleEntry<Integer, Integer> importAddressBook(String fileName) {
		File file = new File(fileName);
		
		BufferedReader reader = null;
		int successes = 0;
		int failures = 0;
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
					failures++;
					System.err.println("line parse failure");
				}
			} 
			modified = true;
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
		
		if (successes > 0) {
			int total = successes + failures;
			GUI.notice(Integer.toString(successes)+ " out of " + Integer.toString(total) +" contacts successfully imported from file: " + file.getName(), 0);
		} else {
			GUI.notice("Failed to import from file: " + file.getName(), 2);
		}
		return new SimpleEntry<Integer, Integer>(successes, failures);
	}
	
	public int openAddressBook(String fileName) {
		File file = new File(fileName);
		
		// create a new one just to be sure not to use any old one
		addressBook = new AddressBook();
		
		BufferedReader reader = null;
		int successes = 0;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String str;
			str = reader.readLine(); // scrumthing file type check
			if (!str.contains("sthsabv")) successes = -2;
			
			str = reader.readLine(); // read address book name first
			setAddressBookName(str);
			
			while (successes >= 0 && (str = reader.readLine()) != null) {
				Entry e = createEntryFromLine(str);
				if (e != null) {
					e.toTabString();
					addressBook.addEntry(e);
					successes++;
				} else {
					System.err.println("line parse failure");
				}
			} 
			modified = false;
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

		if (successes >= 0) {
			GUI.notice("Opened file: " + file.getName(), 0);
		}
		else {
			GUI.notice("Failed to open file: " + file.getName(), 2);
		}
		
		return successes;
	}
	
	@Override
	public boolean saveCurrentAddressBook() {
		if (filePath == null || filePath.trim().length() <= 0) {
			return createSaveDialog();
		}
		else {
			return saveAddressBook(filePath);
		}
	}
	
	private void writeContactsToFile(BufferedWriter writer) throws IOException {
		String body;
		for (Entry contact : addressBook.getAll()) {
			body = contact.toTabString() + "\n";
			writer.append(body);
		}
	}

	@Override
	public boolean saveAddressBook(String filePath) {
		File file = new File(filePath);
		BufferedWriter writer = null;
		String header = "";
		boolean success = false;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.append("");
			writer.close();
			writer = new BufferedWriter(new FileWriter(file, true));
			header = "sthsabv1\n" + getAddressBookName( )+ "\n";
			writer.append(header);
			
			writeContactsToFile(writer);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			GUI.notice("Failed to save file: " + e.getMessage(), 2);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (success) {
			modified = false;
			this.filePath = filePath;
			GUI.notice("Saved file: " + file.getName(), 0);
		}
		return success;
	}
	
	@Override
	public boolean exportAddressBook(String fileName) {
		File file = new File(fileName);
		BufferedWriter writer = null;
		String header = "";
		boolean success = false;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.append("");
			writer.close();
			writer = new BufferedWriter(new FileWriter(file, true));
			for (String fieldName : parsingFields) {
				header += fieldName.toUpperCase() + "\t";
			}
			header += "\n";
			writer.append(header);
			
			writeContactsToFile(writer);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			GUI.notice("Failed to export file: " + e.getMessage(), 2);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (success) {
			GUI.notice("Exported file: " + file.getName(), 0);
		}
		
		return false;
	}

	@Override
	public boolean closeAddressBook() {
		if (modified) {
			int n = JOptionPane.showConfirmDialog(
					GUI,
					"Do you want to save the address book?",
					this.getAddressBookName(),
					JOptionPane.YES_NO_CANCEL_OPTION);
			
			switch(n) {
				case JOptionPane.YES_OPTION:
					if (!this.saveCurrentAddressBook()) return false;
					break;
				case JOptionPane.CANCEL_OPTION:
					return false;
				}
		}

		if (filePath != null && !filePath.isEmpty())
			saveLastAddressBook(new File(filePath));
		
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
		selectedItem = newEntry;
		GUI.notice("New contact added", 0);
		addressBook.sort();
	}

	@Override
	public void updateEntry(int index, HashMap<String, String> details) {
		modified = true;
		addressBook.getEntry(index).updateDetails(details);
		GUI.notice("Contact updated", 0);
		addressBook.sort();
	}

	@Override
	public void deleteEntry(int index) {
		modified = true;
		addressBook.deleteEntry(index);
		GUI.notice("Selected contact deleted", 0);
	}

	@Override
	public List<SimpleEntry<String, String>> itemSelected(int index) {
		selectedIndex = index;
		selectedItem = addressBook.getEntry(index);
		return selectedItem.getDetailList();
	}

	@Override
	public List<Entry> getEntryList() {
		List<Entry> all = addressBook.getAll();
		selectedIndex = all.indexOf(selectedItem);
		return all;
	}
	
	@Override 
	public List<Entry> getEntryList(String searchTerm) {
		List<Entry> all = addressBook.getAll(searchTerm);
		selectedIndex = all.indexOf(selectedItem);
		return all;
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

				if (newApp.openAddressBook(filename) < 0) {
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
		else if (returnVal == JFileChooser.CANCEL_OPTION) {
			GUI.notice("User canceled the operation.", 0);
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
		else if (returnVal == JFileChooser.CANCEL_OPTION) {
			GUI.notice("User canceled the operation.", 0);
		}
		return false;
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
		JFileChooser fileDiag = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tab Separated Value File (*."+ImportFileExtension+")", ImportFileExtension);
		fileDiag.setFileFilter(filter);
		int returnVal = fileDiag.showOpenDialog(GUI);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
				String filename = fileDiag.getSelectedFile().getAbsolutePath();
				SimpleEntry<Integer, Integer> result = importAddressBook(filename);
				if (result.getKey() > 0) {
					return true;
				}
		}
		return false;
	}

	@Override
	public boolean createExportDialog() {
		JFileChooser fileDiag = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tab Separated Value File (*."+ImportFileExtension+")", ImportFileExtension);
		fileDiag.setFileFilter(filter);
		fileDiag.setSelectedFile(new File(getAddressBookName() + "." + ImportFileExtension));
		int returnVal = fileDiag.showSaveDialog(GUI);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
				// Add a extension if there isn't one
				String filename = fileDiag.getSelectedFile().getAbsolutePath();
				if (!filename .endsWith("."+ImportFileExtension)) filename += "."+ImportFileExtension;
				return exportAddressBook(filename);
		}
		return false;
	}

	@Override
	public int getSelectedIndex() {
		return selectedIndex;
	}
}
