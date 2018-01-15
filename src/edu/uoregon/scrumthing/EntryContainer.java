package edu.uoregon.scrumthing;

import java.io.Serializable;
import java.util.List;

public abstract class EntryContainer<T extends Entry> implements Serializable{
	private static final long serialVersionUID = 3638021010917001236L;
	
	public abstract boolean isChanged();
	public abstract void addEntry(T entry);
	public abstract boolean sortBy(String field);
	public abstract List<T> getEntryList();
	public abstract EntryFactory<T> getFactory();
}

