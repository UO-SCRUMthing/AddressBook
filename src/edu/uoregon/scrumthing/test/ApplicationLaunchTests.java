package edu.uoregon.scrumthing.test;

import java.awt.event.WindowEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uoregon.scrumthing.swingext.AddressBookGUI;

class ApplicationLaunchTests {
	AddressBookGUI gui;
	@BeforeEach
	void setUp() throws Exception {
		gui = new AddressBookGUI(null);
	}

	@Test
	void LaunchAndClose() {
		gui.dispatchEvent(new WindowEvent(gui, WindowEvent.WINDOW_CLOSING));
	}
}
