package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public abstract class Entry implements Comparator<Entry>, Comparable<Entry> {
	List<SimpleEntry<String, String>> detailList;
	
	/**
	 * Return a string as the entry's display name
	 * This string will be shown in the GUI contact list
	 */
	public abstract String getName();
	
	/**
	 * Return a list of key-value pairs
	 * GUI will display each field in the order of returned list.
	 */
	public abstract List<SimpleEntry<String, String>> getDetailList();
	
	/**
	 * Update detail. 
	 * 
	 * @param detailMap  A hashmap contains changed fields.
	 *                   The map does not necessary contains all required fields. 
	 * @return false when passed unsupported fields, otherwise apply the change and return true.
	 */
	public abstract boolean updateDetails(HashMap<String, String> detailMap);
	
	@Override
	public String toString() {
		return getName();
	}
	
    public abstract String getLastName(); 
    
    public abstract String getZip(); 
	
	public abstract int compareTo(Entry e);
	
	public abstract int compare(Entry e1, Entry e2);
}
