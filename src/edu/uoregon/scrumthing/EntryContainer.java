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
	 * Return a entry by index.
	 * The index will be determine the order of the entry which will be displayed on the correct spot on screen.
	 */
	public abstract T getEntry(int index);
	/**
	 * Return the size of the container
	 */
	public abstract int getSize();
}
