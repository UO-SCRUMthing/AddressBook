package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public abstract class Entry implements Comparable<Entry>{
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
	public abstract String toString();
	
	public static Comparator<Entry> entryZip = new Comparator<Entry>() {

		@Override
		public int compare(Entry e1, Entry e2) {
			return e1.getZip().compareToIgnoreCase(e2.getZip());
		}
		 
	};
	
    public abstract String getLastName(); 
    
    public abstract String getZip();

	@Override
	public int compareTo(Entry other) {
		return this.getLastName().compareToIgnoreCase(other.getLastName());
	}
	
}
