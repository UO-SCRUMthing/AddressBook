package edu.uoregon.scrumthing.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uoregon.scrumthing.Contact;

class ContactTest {
	Contact contact1;
	Contact contact2;

	@BeforeEach
	void setUp() throws Exception {
		contact1 = new Contact();
		contact2 = new Contact("Clayton", "Kilmer", "1234 E Adams Ave", "#5", "Eugene", "OR", "97401", "541", "kilmer@uoregon.edu");
	}

	@Test
	void test() {
		HashMap<String, String> detailMap = new HashMap<>();
		detailMap.put("firstName", "Clayton");
		detailMap.put("lastName", "Kilmer");
		detailMap.put("address1", "1234 E Adams Ave");
		detailMap.put("address2", "#5");
		detailMap.put("city", "Eugene");
		detailMap.put("state", "OR");
		detailMap.put("zip", "97401");
		detailMap.put("phoneNumber", "541");
		detailMap.put("email", "kilmer@uoregon.edu");
		contact1.updateDetails(detailMap);
		
		if (contact1.toTabString().compareTo(contact2.toTabString()) != 0) {
			fail("Contacts do not match!");
		}
	}

}
