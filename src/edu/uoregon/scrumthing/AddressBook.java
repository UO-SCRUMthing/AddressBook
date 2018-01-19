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
	
	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean sortBy(String field) {
		if (field == "name") {
//			Collections.sort(sortedContactList, c);
			sortedOn = field;
			return true;
			
		} else if (field == "zip") {
//			Collections.sort(sortedContactList, c);
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
	public List<SimpleEntry<String, String>> getTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeEntry(int index) {
		// TODO Auto-generated method stub
		sortedContactList.remove(index);
	}

	@Override
	public void setSortKey(String sortField) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sort() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntry(HashMap<String, String> details) {
		// TODO Auto-generated method stub
		
	}
}