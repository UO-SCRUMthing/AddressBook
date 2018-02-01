package edu.uoregon.scrumthing;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Contact extends Entry implements Comparable<Entry> {
    private List<SimpleEntry<String, String>> detailList = new ArrayList<SimpleEntry<String, String>>();  
    private static List<String> defaultFields = Arrays.asList("firstName", "lastName", "address1", "address2", "city", "state", "zip", "email", "phoneNumber");
    private ArrayList<String> allFieldNames = new ArrayList<String>(defaultFields);
    
    public Contact(String _firstName, String _lastName, String _address1, String _address2, String _city, String _state, String _zip, String _phoneNumber, String _email) {
        detailList.add(new SimpleEntry<String, String>("firstName", _firstName.trim()));
        detailList.add(new SimpleEntry<String, String>("lastName", _lastName.trim()));
        detailList.add(new SimpleEntry<String, String>("address1", _address1.trim()));
        detailList.add(new SimpleEntry<String, String>("address2", _address2.trim()));
        detailList.add(new SimpleEntry<String, String>("city", _city.trim()));
        detailList.add(new SimpleEntry<String, String>("state", _state.trim()));
        detailList.add(new SimpleEntry<String, String>("zip", _zip.trim()));
        detailList.add(new SimpleEntry<String, String>("email", _email.trim()));
        detailList.add(new SimpleEntry<String, String>("phoneNumber", _phoneNumber.trim()));
    }
    
    public Contact(String _firstName, String _lastName, String _address1, String _address2, String _city, String _state, String _zip, String _phoneNumber) {
        detailList.add(new SimpleEntry<String, String>("firstName", _firstName.trim()));
        detailList.add(new SimpleEntry<String, String>("lastName", _lastName.trim()));
        detailList.add(new SimpleEntry<String, String>("address1", _address1.trim()));
        detailList.add(new SimpleEntry<String, String>("address2", _address2.trim()));
        detailList.add(new SimpleEntry<String, String>("city", _city.trim()));
        detailList.add(new SimpleEntry<String, String>("state", _state.trim()));
        detailList.add(new SimpleEntry<String, String>("zip", _zip.trim()));
        detailList.add(new SimpleEntry<String, String>("email", ""));
        detailList.add(new SimpleEntry<String, String>("phoneNumber", _phoneNumber.trim()));
    }
    
    public Contact() {
    	for (String field : allFieldNames) {
    		detailList.add(new SimpleEntry<String, String>(field, ""));
    	}
    }
    
    @Override
	public String toTabString() {
		return  detailList.get(4).getValue() + "\t" + detailList.get(5).getValue() + "\t" + detailList.get(6).getValue() + "\t" + 
				detailList.get(2).getValue() + "\t" + detailList.get(3).getValue() + "\t" + detailList.get(1).getValue() + "\t" + 
				detailList.get(0).getValue() + "\t" + detailList.get(8).getValue() + "\t" + detailList.get(7).getValue();
	}
    
	public void addUserFields(ArrayList<SimpleEntry<String, String>> userFields) {
        for (SimpleEntry<String, String> field : userFields) {
        	allFieldNames.add(field.getKey());
        }
	}
    
    @Override
    public String getLastName() {
    	return detailList.get(1).getValue();
    }
    
    @Override
    public String getZip() {
    	return detailList.get(6).getValue();
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
				entry.setValue(detailMap.get(key).trim());
				changed = true;
			}
		}
		return changed;
	}   
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}