package edu.uoregon.scrumthing;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public abstract class Entry implements Serializable{
	private static final long serialVersionUID = 4741635829877668887L;
	
	//public abstract Image getImage();
	public abstract String getName();
	public abstract List<SimpleEntry<String, String>> getDetailList();
	
	@Override
	public String toString() {
		return getName();
	}

}
