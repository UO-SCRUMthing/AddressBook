package edu.uoregon.scrumthing.test;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import edu.uoregon.scrumthing.Entry;
import edu.uoregon.scrumthing.EntryContainer;
import edu.uoregon.scrumthing.EntryFactory;
import edu.uoregon.scrumthing.test.TestContainer.TestEntry;

public class TestContainer extends EntryContainer<TestEntry>{
	public class TestEntry extends Entry {
		private static final long serialVersionUID = 6928546805640342117L;

		@Override
		public String getName() {
			return "Test Name";
		}

		@Override
		public List<SimpleEntry<String, String>> getDetailList() {
			ArrayList<SimpleEntry<String, String>> testDetail = new ArrayList<SimpleEntry<String, String>>();
			testDetail.add(new SimpleEntry<String, String>("TestKey", "TestValue"));
			return testDetail;
		}

	}
	
	public class TestFactory extends EntryFactory<TestEntry> {

		@Override
		public TestEntry newInstance(String[] args) throws IllegalArgumentException {
			return new TestEntry();
		}

		@Override
		public String[] getFieldKeys() {
			return new String[] {"Test Field", "Test Field 2"};
		}

	}
	
	private static final long serialVersionUID = 8415373039514981719L;
	
	private ArrayList<TestEntry> testList;
	
	public TestContainer() {
		super();
		System.out.println("TestContainer created!");
		testList = new ArrayList<TestEntry>();
		for (int i = 0; i < 10; i++) {
			testList.add(new TestEntry());
		}
	}
	
	@Override
	public boolean isChanged() {
		return false;
	}
	@Override
	public boolean sortBy(String field) {
		return true;
		
	}
	@Override
	public void addEntry(TestEntry entry) {
		testList.add(entry);
		return;
	}
	@Override
	public List<TestEntry> getEntryList() {
		return testList;
	}

	@Override
	public EntryFactory<TestEntry> getFactory() {
		return new TestFactory();
	}

	
}
