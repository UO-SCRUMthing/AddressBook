package edu.uoregon.scrumthing;

public abstract class EntryFactory<T extends Entry> {
	public abstract T newInstance(String[] args) throws IllegalArgumentException;
	public abstract String[] getFieldKeys();
}
