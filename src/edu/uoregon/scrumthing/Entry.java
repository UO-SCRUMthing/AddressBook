package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public abstract class Entry {
	public static boolean FieldCheck(String field, String value) {
		switch (field) {
		case "address1":
			if (!value.matches("^([\\d\\.-]+)\\s*(.*\\w+.*)+$")) return false;
			break;
		case "zip":
			if (!value.matches("^(\\d{4,5})(-\\d{4}|\\d{4}){0,1}$")) return false;
			break;
		case "phoneNumber":
			if (!value.matches("^\\+{0,1}\\s*1{0,1}\\s*(\\d{3}|\\(\\d{3}\\))(?:-{0,1}|\\s*)*(\\d{3})(?:-{0,1}|\\s*)(\\d{4})$")) {
				if (!value.matches("^\\+\\s*(\\d+\\s*)*$"))
					return false;
			}
			break;
		case "email":
			if (!value.matches("^.*@.*\\..+$")) return false;
			break;
		}
		return true;
	}
	
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
	
//	@Override
//	public boolean equals(Entry other) {
		
//	}
	
	public static Comparator<Entry> compareLastName = (e1, e2) -> {		
			
		if (e1.getLastName().length() == 0 && e2.getLastName().length() == 0) {
			return 0;
		} 
		if (e1.getLastName().length() == 0) {
			return 1;
		} 
		if (e2.getLastName().length() == 0) {
			return -1;
		}
		
		return e1.getLastName().compareToIgnoreCase(e2.getLastName());
		 
	};
	
	public static Comparator<Entry> compareFist = (e1, e2) -> {
		return e1.getName().compareToIgnoreCase(e2.getName());
	};
	
	public static Comparator<Entry> compareZipOnly = (e1, e2) -> {
			
		if (e1.getZip().length() == 0 && e2.getZip().length() == 0) {
			return 0;
		} 
		if (e1.getZip().length() == 0) {
			return 1;
		} 
		if (e2.getZip().length() == 0) {
			return -1;
		}
		
		return e1.getZip().compareToIgnoreCase(e2.getZip());
		 
	};
	
	public static Comparator<Entry> compareName = compareLastName.thenComparing(compareFist).thenComparing(compareZipOnly);
	
	public static Comparator<Entry> compareZip = compareZipOnly.thenComparing(compareLastName).thenComparing(compareName);
	
    public abstract String getLastName(); 
    
    public abstract String getZip();

//	@Override
//	public int compareTo(Entry other) {
//		return this.getLastName().compareToIgnoreCase(other.getLastName());
//	}
	
	public int detailCompare(Entry other) {
		return this.toTabString().compareToIgnoreCase(other.toTabString());
	}

	public abstract String toTabString();
	
}
