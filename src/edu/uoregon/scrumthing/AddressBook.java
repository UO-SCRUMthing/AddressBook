package edu.uoregon.scrumthing;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AddressBook extends EntryContainer<Entry> {
//	private HashMap<String, Contact> contacts = new HashMap<>();
	private List<Entry> sortedContactList = new ArrayList<Entry>();
	private String sortedOn;
	private String addressBookName;

	public AddressBook(String name) {
		// TODO finish contractor 
		this.setAddressBookName(name);
	}
	
	public AddressBook() {
		// TODO Auto-generated constructor stub
	}

	public String getAddressBookName() {
		return addressBookName;
	}

	public void setAddressBookName(String addressBookName) {
		this.addressBookName = addressBookName;
	}
	
	@Override
	public boolean sortBy(String field) {
		if (field == "name") {
			Collections.sort(sortedContactList);
			sortedOn = field;
			return true;
			
		} else if (field == "zip") {
			Collections.sort(sortedContactList, Entry.entryZip); 
			sortedOn = field;	
			return true;
		} 
		return false;
	}
	
	@Override
	public Entry getEntry(int index) {
		return sortedContactList.get(index);
	}
	
	@Override
	public int getSize() {
		return sortedContactList.size();
	}

	@Override
	public List<Entry> getAll() {
		return sortedContactList;
	}

	@Override
	public Entry getTemplate() {
		return new Contact();
	}

	@Override
	public void deleteEntry(int index) {
		sortedContactList.remove(index);
	}

	@Override
	public boolean setSortKey(String sortField) {
		if (sortField == "name" || sortField == "zip") {
			sortedOn = sortField;
			return true;	
		} 
		return false;		
	}

	@Override
	public boolean sort() {
		return this.sortBy(sortedOn);		
	}

	@Override
	public void addEntry(Entry entry) {
		sortedContactList.add(entry);
		this.sort();
	}
}