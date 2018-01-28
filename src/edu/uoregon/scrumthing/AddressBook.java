package edu.uoregon.scrumthing;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import static java.util.Comparator.*;

public class AddressBook extends EntryContainer<Entry> {
	private HashMap<String, ArrayList<Entry>> searchableContacts = new HashMap<String, ArrayList<Entry>>();
	private List<Entry> sortedContactList = new ArrayList<Entry>();
	private String sortedOn = "name";
	private String addressBookName;

	public AddressBook(String name) {
		// TODO finish constructor  
		addressBookName = name;	}
	
	public AddressBook() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Entry> search(String lastName) {
		List<Entry> searchResults = new ArrayList<Entry>();
		
		return searchResults;
		
	}
	
	@Override
	public List<Entry> search(String firstName, String lastName) {
		List<Entry> searchResults = new ArrayList<Entry>();
		
		return searchResults;
		
	}

	@Override
	public String getAddressBookName() {
		return addressBookName;
	}

	@Override
	public void setAddressBookName(String addressBookName) {
		this.addressBookName = addressBookName;
	}
	
	@Override
	public boolean sortBy(String field) {
		if (field == "name") {
			Collections.sort(sortedContactList, Entry.compareName);
//			sortedContactList.sort(nullsLast(comparing(Entry::getName, naturalOrder())));
			sortedOn = field;
			return true;
			
		} else if (field == "zip") {
			Collections.sort(sortedContactList, Entry.compareZip); 
//			sortedContactList.sort(comparing(Entry::getZip, nullsLast(Entry.compareZip)));
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
	public void deleteEntry(Entry entry) {
		sortedContactList.remove(entry);
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