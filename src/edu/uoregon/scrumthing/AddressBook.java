package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;

public class AddressBook extends EntryContainer<Entry> {
//	private HashMap<String, Contact> contacts = new HashMap<>();
	private List<Entry> sortedContactList = new ArrayList<Entry>();
	private String sortedOn;
	private String addressBookName;
	
	AddressBook() {
		
	}
	
	@Override
	public boolean sortBy(String field) {
		if (field == "name") {
			Collections.sort(sortedContactList);
			sortedOn = field;
			return true;
			
		} else if (field == "zip") {
			// according https://www.tutorialspoint.com/java/java_using_comparator.htm this should sort the array based on the compare(Entry e1, Entry e2) method which compares based on zip 
			Collections.sort(sortedContactList, new Contact()); 
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
	public void removeEntry(int index) {
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
	public void addEntry(HashMap<String, String> details) {
		// TODO Auto-generated method stub
		
	}
}