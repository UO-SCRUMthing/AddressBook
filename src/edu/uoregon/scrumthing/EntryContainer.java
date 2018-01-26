package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

public abstract class EntryContainer<T extends Entry>{
	
	public abstract List<T> getAll();
	
	// returns template for T class 
	public abstract Entry getTemplate();
	
	/**
	 * Remove an entry
	 * 
	 * @param index of  The entry object that needs to be removed from the container.
	 */
	public abstract void deleteEntry(int index);
	
	public abstract boolean setSortKey(String sortField);
	
	public abstract boolean sort();
	
	/**
	 * Sort the data.
	 * 
	 * @param field  Sorting key. Valid keys: name and zip.
	 */
	public abstract boolean sortBy(String sortField);
	
	/**
	 * Return a entry by index.
	 * The index will be determine the order of the entry which will be displayed on the correct spot on screen.
	 */
	public abstract T getEntry(int index);
	
	/**
	 * Return the size of the container
	 */
	public abstract int getSize();

	/**
	 * Add an entry to the container.
	 * @param <K>
	 * 
	 * @param entry  The entry needs to be added to the container.
	 */
	public abstract void addEntry(T entry); 
}
