package edu.uoregon.scrumthing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.uoregon.scrumthing.Entry;

class FieldCheckerTest {

	@Test
	void test() {
		if (!Entry.FieldCheck("zip", "97403")) fail("ZIP: Five digits failed");
	}
	
	@Test
	void test2() {
		if (!Entry.FieldCheck("zip", "97403-3000")) fail("ZIP: Nine digits failed");
	}
	
	@Test
	void test3() {
		if (Entry.FieldCheck("zip", "97403-")) fail("ZIP: Invalid (97403-) failed");
	}
	
	@Test
	void test4() {
		if (!Entry.FieldCheck("phoneNumber", "5555555555")) fail("Phone: 7 digits failed");
	}
	
	@Test
	void test5() {
		if (!Entry.FieldCheck("phoneNumber", "+1 5499999999")) fail("Phone: +1 5499999999 failed");
	}
	
	@Test
	void test6() {
		if (!Entry.FieldCheck("phoneNumber", "+15499999999")) fail("Phone: 5499999999 failed");
	}
	
	@Test
	void test7() {
		if (Entry.FieldCheck("email", "email")) fail("Email: \"email\" accepted");
	}

	@Test
	void test8() {
		if (!Entry.FieldCheck("email", "hey@s.com")) fail("Email: hey@s.com failed");
	}
	
	@Test
	void test9() {
		if (Entry.FieldCheck("email", "some@")) fail("Phone: \"some@\" accepted");
	}


}
