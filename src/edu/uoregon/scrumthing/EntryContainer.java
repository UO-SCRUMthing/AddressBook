package edu.uoregon.scrumthing;

import java.util.List;

public abstract class EntryContainer<T extends Entry>{
	/**
	 * Return true if the content have be changed after last save.
	 */
	public abstract boolean isChanged();
	/**
	 * Add an entry to the container.
	 * 
	 * @param entry  The entry needs to be added to the container.
	 */
	public abstract void addEntry(T entry);
	/**
	 * Remove an entry
	 * 
	 * @param entry  The entry object that needs to be removed from the container.
	 */
	public abstract void removeEntry(T entry);
	/**
	 * Sort the data.
	 * 
	 * @param field  Sorting key.
	 */
	public abstract boolean sortBy(String field);
	/**
	 * Return a list of entry.
	 * Each entry in the list will be displayed in the GUI.
	 */
	public abstract List<T> getEntryList();
}
