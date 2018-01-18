package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AddressBook extends EntryContainer {
//	private HashMap<String, Contact> contacts = new HashMap<>();
	private List<Entry> sortedContactList = new ArrayList<Entry>();
	
	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addEntry(Entry entry) {
		sortedContactList.add(entry);
		
	}
	
	@Override
	public void removeEntry(Entry entry) {
		sortedContactList.remove(entry);		
	}
	
	@Override
	public boolean sortBy(String field) {
		if (field == "name") {
			Collections.sort(sortedContactList, c);
			
		} else if (field == "zip") {
			
		} 
		return false;
	}
	
	@Override
	public Entry getEntry(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}