package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import edu.uoregon.scrumthing.Entry;

public class AddressDetailPanel extends JPanel {
	private static final long serialVersionUID = -7042296921092780861L;
	private static final int NEW = -1;
	private static final HashMap<String, String> fieldName = new HashMap<>();
	static {
		fieldName.put("city", "City: ");
		fieldName.put("state", "State: ");
		fieldName.put("zip", "ZIP Code: ");
		fieldName.put("address1", "Address Line 1: ");
		fieldName.put("address2", "Address Line 2: ");
		fieldName.put("lastName", "Last Name: ");
		fieldName.put("firstName", "First Name: ");
		fieldName.put("phoneNumber", "Phone Number: ");
		fieldName.put("email", "E-mail: ");
	}
	
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
		this.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
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
				if (namePlate.getFirstName().isEmpty() && namePlate.getLastName().isEmpty()) {
					gui.notice("Please provide a name for this contact.", 1);
					return;
				}
				
				if (!TrimFieldAndCheckRequirements()) {
					gui.notice("Please provide the whole name or at least one field of information for this contact.", 1);
					return;
				}
				if (!highlightWarningFields().isEmpty()) {
					int n = JOptionPane.showConfirmDialog(
							namePlate,
						    "One or more fields contains non-standard input. Would you like to proceed?\r\n" + 
						    "\r\n" + 
						    "See the User Guide for details about input standards.",
						    gui.controller.getAddressBookName(),
						    JOptionPane.YES_NO_OPTION);
					
					if (n == JOptionPane.NO_OPTION) {
						return;
					}
				}
				saveChange();
			}
		});
	
		discardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChange();
				gui.notice("Change discarded", 0);
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
			newTextField.setDisabledTextColor(Color.BLACK);
			newTextField.setEnabled(false);
			newTextField.setBackground(fieldPane.getBackground());
			newTextField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					// To remove any notice
					gui.notice("", 0);
					newTextField.setBackground(Color.WHITE);
				}

				@Override
				public void focusLost(FocusEvent e) {
					// Do nothing
				}
			});
			
			if (key.equals("zip")) {
				((AbstractDocument)newTextField.getDocument()).setDocumentFilter(new RegexDocumentFilter("[0-9\\-A-Za-z]*"));
			} else if (key.equals("phoneNumber")){
				((AbstractDocument)newTextField.getDocument()).setDocumentFilter(new RegexDocumentFilter("[\\(\\)\\+0-9\\- ]*"));
			} else {
				((AbstractDocument)newTextField.getDocument()).setDocumentFilter(new RegexDocumentFilter("[^\\t]*"));
			}
			
			fields.put(key, newTextField);
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			String fieldNameText = fieldName.containsKey(key) ? fieldName.get(key) : key;
			JLabel fieldLabel = new JLabel(fieldNameText);
			fieldLabel.setHorizontalAlignment(JLabel.TRAILING);
			fieldLabel.setPreferredSize(new Dimension(120, 25));
			panel.add(fieldLabel);
			panel.add(newTextField);
			
			panel.setPreferredSize(new Dimension(300, 40));
			panel.setMinimumSize(new Dimension(80, 40));
			panel.setMaximumSize(new Dimension(450, 40));
			
			fieldPane.add(panel);
		}
		
	}
	
	private JPanel createEditButtonSet() {
		JPanel margin = new JPanel(new FlowLayout());
		editButtonSet = new JPanel(new FlowLayout());;
		margin.add(editButtonSet);
		
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
		HashMap<String, String> fields = getAllFields();
		
		if (index == NEW && emptyEntry != null) {
			emptyEntry.updateDetails(fields);
			gui.controller.addNewEntry(emptyEntry);
		} else if (index >= 0){
			gui.controller.updateEntry(index, fields);
		}
		exitEditMode();
	}
	
	private void discardChange() {
		exitEditMode();
		if (index == NEW) gui.removeDetailPanel();
		else updateFieldFromDetail();

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
		this.highlightWarningFields();
	}
	
	@SuppressWarnings("rawtypes")
	private boolean TrimFieldAndCheckRequirements() {
		String firstName = namePlate.getFirstName().trim();
		String lastName = namePlate.getLastName().trim();
		boolean valid = false;
		if (!firstName.isEmpty() && !lastName.isEmpty()) {
			valid = true;
		}
		namePlate.setPlateName(firstName, lastName);
		
	    Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        JTextField field = (JTextField)pair.getValue();
	        String fieldText = field.getText().trim();
			field.setText(fieldText);
			if ((!firstName.isEmpty() || !lastName.isEmpty()) && !fieldText.isEmpty()) valid = true;
	    }
	    return valid;
	}
	
	@SuppressWarnings("rawtypes")
	private List<String> highlightWarningFields() {
		ArrayList<String> warningFields = new ArrayList<String>();
	    Iterator it = fields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        JTextField field = (JTextField)pair.getValue();
			if (field.getText().length() > 0 && !Entry.FieldCheck((String)pair.getKey(), field.getText())) {
				warningFields.add((String)pair.getKey());
				field.setBackground(Color.yellow);
			}
	    }

		return warningFields;
	}
}
