package edu.uoregon.scrumthing.swingext;

import java.util.List;

import javax.swing.AbstractListModel;

import edu.uoregon.scrumthing.Entry;
import edu.uoregon.scrumthing.EntryContainer;

@SuppressWarnings("rawtypes")
public class ObjectListModel extends AbstractListModel<String> {
	private static final long serialVersionUID = 2260775709941977100L;
	
	// Need controller
	private List data;
	public ObjectListModel(List data) {
		this.data = data;
	}

	@Override
	public String getElementAt(int index) {
		return data.get(index).toString();
	}

	@Override
	public int getSize() {
		return data.size();
	}

}
