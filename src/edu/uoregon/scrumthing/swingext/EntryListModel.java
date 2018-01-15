package edu.uoregon.scrumthing.swingext;

import java.util.List;

import javax.swing.AbstractListModel;

import edu.uoregon.scrumthing.Entry;
import edu.uoregon.scrumthing.EntryContainer;

public class EntryListModel extends AbstractListModel<Entry> {
	private static final long serialVersionUID = 2260775709941977100L;
	
	private List<Entry> data;
	@SuppressWarnings("unchecked")
	public EntryListModel(EntryContainer<?> data) {
		this.data = (List<Entry>) data.getEntryList();
	}

	@Override
	public Entry getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public int getSize() {
		return data.size();
	}

}
