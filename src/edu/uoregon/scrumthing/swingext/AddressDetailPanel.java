package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uoregon.scrumthing.Entry;

public class AddressDetailPanel extends JPanel {
	private static final long serialVersionUID = -7042296921092780861L;
	private static final int NEW = -1;
	
	private AddressBookGUI gui;
	private List<SimpleEntry<String, String>> details;
	private HashMap<String, JTextField> fields;
	private JPanel fieldPane;
	private NamePlatePanel namePlate;
	private boolean editMode;
	private int index;
	private Entry emptyEntry;
	private JPanel editButtonSet;
	private JButton confirmButton;
	private JButton editButton;
	private JButton discardButton;
	
	public AddressDetailPanel(AddressBookGUI gui, int index) {
		this(gui);
		this.index = index;
		this.details = gui.controller.itemSelected(index);
		RefreshPanel();
	}
	
	public AddressDetailPanel(AddressBookGUI gui, Entry emptyEntry) {
		this(gui);
		this.details = emptyEntry.getDetailList();
		this.index = NEW;
		this.emptyEntry = emptyEntry;
		
		RefreshPanel();
		enterEditMode();
	}
	
	public AddressDetailPanel(AddressBookGUI gui) {
		super(new BorderLayout());
		this.gui = gui;
		this.emptyEntry = null;
		namePlate = new NamePlatePanel();
		
		// Reusable components
		editButton = new JButton("Edit");
		confirmButton = new JButton("Confirm");
		discardButton = new JButton("Discard");
		
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterEditMode();
			}
		});
		
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveChange();
			}
		});
	
		discardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChange();
			}
		});
		
	}
	
	public void RefreshPanel() {
		this.removeAll();
		
		if (details == null || gui == null) return;
		
		fields = new HashMap<String, JTextField>();
		
		fieldPane = new JPanel();
		fieldPane.setLayout(new BoxLayout(fieldPane, BoxLayout.Y_AXIS));
		
		JPanel firstBar = new JPanel(new BorderLayout());
		firstBar.add(Box.createVerticalStrut(15), BorderLayout.PAGE_START);
		firstBar.add(Box.createVerticalStrut(20), BorderLayout.PAGE_END);
		firstBar.add(namePlate, BorderLayout.LINE_START);
		firstBar.add(createEditButtonSet(), BorderLayout.LINE_END);
		
		add(firstBar, BorderLayout.PAGE_START);
		add(fieldPane, BorderLayout.CENTER);
		
		updateFieldFromDetail();
	}
	
	private void addOrUpdateField(String key, String value) {
		if (fields.containsKey(key)) {
			fields.get(key).setText(value);
		}
		else {
			JTextField newTextField = new JTextField(value);
			newTextField.setEnabled(false);
			fields.put(key, newTextField);
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			panel.add(new JLabel(key));
			panel.add(newTextField);
			
			panel.setPreferredSize(new Dimension(150, 40));
			panel.setMinimumSize(new Dimension(80, 40));
			panel.setMaximumSize(new Dimension(200, 40));
			
			fieldPane.add(panel);
		}
		
	}
	
	private JPanel createEditButtonSet() {
		JPanel margin = new JPanel(new FlowLayout());
		editButtonSet = new JPanel(new FlowLayout());;
		margin.add(editButtonSet);
		margin.add(Box.createHorizontalStrut(15));
		
		editButtonSet.add(editButton);
		return margin;
	}
	
	@SuppressWarnings("rawtypes")
	private void enterEditMode() {
		editMode = true;
		editButtonSet.removeAll();
		editButtonSet.add(confirmButton);
		editButtonSet.add(discardButton);
		namePlate.setEditMode(editMode);
		editButtonSet.revalidate();
		editButtonSet.repaint();
		
	    Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        JTextField field = (JTextField)pair.getValue();
	        field.setEnabled(true);
	    }
	    gui.enterEditMode();
	}
	
	private void saveChange() {
		if (index == NEW && emptyEntry != null) {
			emptyEntry.updateDetails(getAllFields());
			gui.controller.addNewEntry(emptyEntry);
		} else if (index >= 0){
			gui.controller.updateEntry(index, getAllFields());
		}
		exitEditMode();
	}
	
	private void discardChange() {
		exitEditMode();
		updateFieldFromDetail();
	}
	
	@SuppressWarnings("rawtypes")
	private void exitEditMode() {
		editMode = false;
		editButtonSet.removeAll();
		editButtonSet.add(editButton);
		namePlate.setEditMode(false);
		editButtonSet.revalidate();
		editButtonSet.repaint();
		
	    Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        JComponent field = (JComponent)pair.getValue();
	        field.setEnabled(false);
	    }
	    gui.exitEditMode();
	}
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, String> getAllFields() {
		HashMap<String, String> changes = new HashMap<String, String>();
		
	    Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        JTextField field = (JTextField)pair.getValue();
	        changes.put((String)pair.getKey(), field.getText());
	        System.out.println(field.getText());
	    }
	    
	    changes.put("firstName", namePlate.getFirstName());
	    changes.put("lastName", namePlate.getLastName());
	    
	    return changes;
	}
	
	
	private void updateFieldFromDetail() {
		String firstName = "";
		String lastName = "";
		for(SimpleEntry<String, String> field : details) {
			String key = field.getKey();
			String value = field.getValue();		
			if (key.equals("firstName")) {
				firstName = value;
			} else if (key.equals("lastName")) {
				lastName = value;
			} else {
				addOrUpdateField(key, value);
			}
		}
		namePlate.setPlateName(firstName, lastName);
	}
}
