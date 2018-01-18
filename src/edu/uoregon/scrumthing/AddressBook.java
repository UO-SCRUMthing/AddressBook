package edu.uoregon.scrumthing;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class AddressBook extends EntryContainer {
	private HashMap<String, Contact> contacts = new HashMap<>();
	private ArrayList <Contact> sortedContactList = new ArrayList<>();
	
	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void addEntry(Entry entry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeEntry(Entry entry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean sortBy(String field) {
		// TODO Auto-generated method stub
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