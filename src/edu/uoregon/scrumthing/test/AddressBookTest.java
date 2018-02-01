package edu.uoregon.scrumthing.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uoregon.scrumthing.AddressBook;
import edu.uoregon.scrumthing.Contact;
import edu.uoregon.scrumthing.Entry;

class AddressBookTest {
	AddressBook addressbook1; 
	AddressBook addressbook2; 

	@BeforeEach
	void setUp() throws Exception {
		addressbook1 = new AddressBook();
		addressbook2 = new AddressBook();
		addressbook1.addEntry(new Contact("Sam", "Elliott", "", "", "", "", "97403", "", ""));
		addressbook1.addEntry(new Contact("Clayton", "Kilmer", "", "", "", "", "97401", "", ""));
		addressbook1.addEntry(new Contact("Kaela", "Schaefer", "", "", "", "", "97403", "", ""));
		// already sorted order on last name
		
		addressbook2.addEntry(new Contact("Kaela", "Schaefer", "", "", "", "", "97403", "", ""));
		addressbook2.addEntry(new Contact("Sam", "Elliott", "", "", "", "", "97403", "", ""));
		addressbook2.addEntry(new Contact("Clayton", "Kilmer", "", "", "", "", "97401", "", ""));		
	}

	@Test
	void test1() {
		addressbook2.sortBy("name");
		List<Entry> ab1 = addressbook1.getAll();
		List<Entry> ab2 = addressbook2.getAll();
		for (int i = 0; i < ab1.size(); i++) {
			if (ab1.get(i).toTabString().compareTo(ab2.get(i).toTabString()) != 0) {
				fail("Address books are not in the same order!");
			}
		}
	}
	
	@Test
	void test2() {
		addressbook1.sortBy("zip");
		addressbook2.sortBy("zip");
		List<Entry> ab1 = addressbook1.getAll();
		List<Entry> ab2 = addressbook2.getAll();
		for (int i = 0; i < ab1.size(); i++) {
			if (ab1.get(i).toTabString().compareTo(ab2.get(i).toTabString()) != 0) {
				fail("Address books are not in the same order!");
			}
		}
	}
}
