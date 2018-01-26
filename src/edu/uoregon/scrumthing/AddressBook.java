package edu.uoregon.scrumthing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddressBook extends EntryContainer<Contact> {
//	private HashMap<String, Contact> contacts = new HashMap<>();
	private List<Contact> sortedContactList = new ArrayList<Contact>();
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
			sortedContactList.sort(new Comparator<Contact>() {
				@Override
				public int compare(Contact o1, Contact o2) {
					return o1.getLastName().compareTo(o2.getLastName());
				}
			});
			sortedOn = field;
			return true;
			
		} else if (field == "zip") {
			// according https://www.tutorialspoint.com/java/java_using_comparator.htm this should sort the array based on the compare(Entry e1, Entry e2) method which compares based on zip 
			sortedContactList.sort(new Comparator<Contact>() {
				@Override
				public int compare(Contact o1, Contact o2) {
					return o1.getZip().compareTo(o2.getZip());
				}
			});
			sortedOn = field;	
			return true;
		} 
		return false;
	}
	
	@Override
	public Contact getEntry(int index) {
		return sortedContactList.get(index);
	}
	
	@Override
	public int getSize() {
		return sortedContactList.size();
	}

	@Override
	public List<Contact> getAll() {
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
	public void addEntry(Contact entry) {
		sortedContactList.add(entry);
		this.sort();
	}
}