package edu.uoregon.scrumthing;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AddressBook extends EntryContainer<Entry> {
	private List<Entry> sortedContactList = new ArrayList<Entry>();
	private List<Entry> searchResults = null;
	private String sortedOn = "name";
	private String addressBookName;

	public AddressBook(String name) {
		addressBookName = name;
	}
	
	public AddressBook() {
		addressBookName = "Unnamed Address Book";
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
			if (searchResults != null)
				Collections.sort(searchResults, Entry.compareName);
			Collections.sort(sortedContactList, Entry.compareName);
			sortedOn = field;
			return true;
			
		} else if (field == "zip") {
			if (searchResults != null)
				Collections.sort(searchResults, Entry.compareName);
			Collections.sort(sortedContactList, Entry.compareZip); 
			sortedOn = field;	
			return true;
		} 
		return false;
	}
	
	@Override
	public Entry getEntry(int index) {
		if (searchResults != null) {
			return searchResults.get(index);
		}
		return sortedContactList.get(index);
	}
	
	@Override
	public int getSize() {
		return sortedContactList.size();
	}

	@Override
	public List<Entry> getAll() {
		searchResults = null;
		return sortedContactList;
	}
	
	@Override
	public List<Entry> getAll(String searchTerm) {
		String[] parts = searchTerm.toLowerCase().split(" ");
		searchResults = new ArrayList<Entry>();
		for (Entry entry : sortedContactList) {
			boolean contains = true;
			for (String part : parts) {
				if (!entry.toTabString().toLowerCase().contains(part)) {
					contains = false;
					break;
				}
			}
			if (contains) {
				searchResults.add(entry);
			}
		}
		return searchResults;
	}

	@Override
	public Entry getTemplate() {
		return new Contact();
	}

	@Override
	public void deleteEntry(int index) {
		deleteEntry(getEntry(index));
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
	}
}