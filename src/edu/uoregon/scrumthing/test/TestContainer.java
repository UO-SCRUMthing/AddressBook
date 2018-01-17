package edu.uoregon.scrumthing.test;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import edu.uoregon.scrumthing.Entry;
import edu.uoregon.scrumthing.EntryContainer;
import edu.uoregon.scrumthing.test.TestContainer.TestEntry;

public class TestContainer extends EntryContainer<TestEntry>{
	public class TestEntry extends Entry {

		@Override
		public String getName() {
			return "Test Name";
		}

		@Override
		public List<SimpleEntry<String, String>> getDetailList() {
			ArrayList<SimpleEntry<String, String>> testDetail = new ArrayList<SimpleEntry<String, String>>();
			Random rand = new Random();
			int cField = rand.nextInt(6);
			for (int i = 0; i < cField; i++) {
				testDetail.add(new SimpleEntry<String, String>("TestKey" + Integer.toString(i), "TestValue"));
			}
			return testDetail;
		}

		@Override
		public boolean updateDetail(HashMap<String, String> detailMap) {
			return true;
		}

	}
	
	private ArrayList<TestEntry> testList;
	
	public TestContainer() {
		super();
		System.out.println("TestContainer created!");
		testList = new ArrayList<TestEntry>();
		for (int i = 0; i < 50; i++) {
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
	}
	

	@Override
	public void removeEntry(TestEntry entry) {
		testList.remove(entry);
	}

	@Override
	public TestEntry getEntry(int index) {
		return testList.get(index);
	}

	@Override
	public int getSize() {
		return testList.size();
	}
	
}
