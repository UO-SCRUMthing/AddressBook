package edu.uoregon.scrumthing;

import javax.swing.UIManager;

import edu.uoregon.scrumthing.swingext.AddressBookGUI;
import edu.uoregon.scrumthing.test.TestContainer;

public class Application {
	public static void main(String[] args) {
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch(Exception ex) {
	        ex.printStackTrace();
	    }
		TestContainer testData = new TestContainer();
		AddressBookGUI gui = new AddressBookGUI(testData);
	}
}
