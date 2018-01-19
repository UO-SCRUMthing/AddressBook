package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contact extends Entry {
    List<SimpleEntry<String, String>> detailList = new ArrayList<SimpleEntry<String, String>>();  
    List<String> fieldNames = new ArrayList<String>();

    public Contact(String _firstName, String _lastName, String _address, String _city, String _state, String _zip, String _email, String _phoneNumber, List<SimpleEntry<String, String>> _userDefined) {
        detailList.add(new SimpleEntry<String, String>("firstName", _firstName));
        detailList.add(new SimpleEntry<String, String>("lastName", _lastName));
        detailList.add(new SimpleEntry<String, String>("address", _address));
        detailList.add(new SimpleEntry<String, String>("city", _city));
        detailList.add(new SimpleEntry<String, String>("state", _state));
        detailList.add(new SimpleEntry<String, String>("zip", _zip));
        detailList.add(new SimpleEntry<String, String>("email", _email));
        detailList.add(new SimpleEntry<String, String>("phoneNumber", _phoneNumber));
        detailList.addAll(_userDefined);
    }
    
    public Contact(String _firstName, String _lastName, String _address, String _city, String _state, String _zip, String _email, String _phoneNumber) {
        detailList.add(new SimpleEntry<String, String>("firstName", _firstName));
        detailList.add(new SimpleEntry<String, String>("lastName", _lastName));
        detailList.add(new SimpleEntry<String, String>("address", _address));
        detailList.add(new SimpleEntry<String, String>("city", _city));
        detailList.add(new SimpleEntry<String, String>("state", _state));
        detailList.add(new SimpleEntry<String, String>("zip", _zip));
        detailList.add(new SimpleEntry<String, String>("email", _email));
        detailList.add(new SimpleEntry<String, String>("phoneNumber", _phoneNumber));
    }
    
    public String getLastName() {
    	return detailList.get(1).getValue();
    }
    
    public String getZip() {
    	return detailList.get(5).getValue();
    }

	@Override
	public String getName() {
		return detailList.get(0).getValue() + " " + detailList.get(1).getValue();
	}

	@Override
	public List<SimpleEntry<String, String>> getDetailList() {
		return detailList;
	}

	@Override
	public boolean updateDetails(HashMap<String, String> detailMap) {
		boolean changed = false;
		for (SimpleEntry<String, String> entry : detailList) {
			String key = entry.getKey();
			if (detailMap.containsKey(key)) {
				entry.setValue(detailMap.get(key));
				changed = true;
			}
		}
		return changed;
	}   
	
	@Override
	public String toString() {
		return this.getName();
	}
	@Override
	public Entry buildTemplate() {
		// TODO Auto-generated method stub
		return null;
	}
}