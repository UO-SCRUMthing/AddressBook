package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uoregon.scrumthing.Controller;

public class AddressDetailPanel extends JPanel {
	private static final long serialVersionUID = -7042296921092780861L;
	private static final int NEW = -1;
	
	private Controller controller;
	private List<SimpleEntry<String, String>> details;
	private HashMap<String, JComponent> fields;
	private HashMap<String, String> changedFields;
	private JPanel fieldPane;
	private NamePlatePanel namePlate;
	private boolean editMode;
	private int index;
	
	public AddressDetailPanel(Controller controller, int index) {
		this(controller);
		this.index = index;
		this.details = controller.itemSelected(index);
		RefreshPanel();
	}
	
	public AddressDetailPanel(Controller controller, List<SimpleEntry<String, String>> details) {
		this(controller);
		this.details = details;
		this.index = NEW;
		RefreshPanel();
	}
	
	public AddressDetailPanel(Controller controller) {
		super(new BorderLayout());
		this.controller = controller;
		changedFields = new HashMap<>();
		namePlate = new NamePlatePanel();
	}
	
	public void RefreshPanel() {
		this.removeAll();
		
		if (details == null || controller == null) return;
		
		fields = new HashMap<String, JComponent>();
		
		fieldPane = new JPanel();
		fieldPane.setLayout(new BoxLayout(fieldPane, BoxLayout.Y_AXIS));
		
		JPanel firstBar = new JPanel(new BorderLayout());
		firstBar.add(Box.createVerticalStrut(15), BorderLayout.PAGE_START);
		firstBar.add(Box.createVerticalStrut(20), BorderLayout.PAGE_END);
		firstBar.add(namePlate, BorderLayout.LINE_START);
		firstBar.add(createEditButtonSet(), BorderLayout.LINE_END);
		
		add(firstBar, BorderLayout.PAGE_START);
		add(fieldPane, BorderLayout.CENTER);
		
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
				addField(key, value);
			}
		}
		namePlate.setPlateName(firstName, lastName);
	}
	
	private void addField(String key, String value) {
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
	
	private JPanel createEditButtonSet() {
		JPanel margin = new JPanel(new FlowLayout());
		JPanel set = new JPanel(new FlowLayout());
		margin.add(set);
		margin.add(Box.createHorizontalStrut(15));
		
		JButton editButton = new JButton("Edit");
		JButton confirmButton = new JButton("Confirm");
		JButton discardButton = new JButton("Discard");
		
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMode = true;
				set.removeAll();
				set.add(confirmButton);
				set.add(discardButton);
				namePlate.setEditMode(editMode);
				set.revalidate();
				set.repaint();
			}
		});
		
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMode = false;
				set.removeAll();
				set.add(editButton);
				namePlate.setEditMode(false);
				set.revalidate();
				set.repaint();
			}
		});
	
		discardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMode = false;
				set.removeAll();
				set.add(editButton);
				namePlate.setEditMode(false);
				set.revalidate();
				set.repaint();
			}
		});
		set.add(editButton);
		return margin;
	}
	
	public void enterEditMode() {
		
	}
	
	public void saveChange() {
		
	}
	
	public void discardChange() {
		
	}
	
	public void submitChange() {
		
	}
	
}
