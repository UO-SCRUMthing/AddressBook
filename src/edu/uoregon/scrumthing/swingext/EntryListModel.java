package edu.uoregon.scrumthing.swingext;

import java.util.List;

import javax.swing.AbstractListModel;

import edu.uoregon.scrumthing.Entry;
import edu.uoregon.scrumthing.EntryContainer;

public class EntryListModel extends AbstractListModel<Entry> {
	private static final long serialVersionUID = 2260775709941977100L;
	
	private EntryContainer<?> data;
	@SuppressWarnings("unchecked")
	public EntryListModel(EntryContainer<?> data) {
		this.data = data;
	}

	@Override
	public Entry getElementAt(int index) {
		return data.getEntry(index);
	}

	@Override
	public int getSize() {
		return data.getSize();
	}

}
