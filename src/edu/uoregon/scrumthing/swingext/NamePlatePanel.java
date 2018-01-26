package edu.uoregon.scrumthing.swingext;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NamePlatePanel extends JPanel {
	private static final long serialVersionUID = -58544465589296740L;
	private JLabel namePlate;
	private JTextField firstField;
	private JTextField lastField;
	private boolean editMode;
	
	public NamePlatePanel() {
		this("", "");
	}
	
	public NamePlatePanel(String firstName, String lastName) {
		super();
		this.editMode = false;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		namePlate = new JLabel();
		firstField = new JTextField(firstName);
		firstField.setPreferredSize(new Dimension(120, 30));
		lastField = new JTextField(lastName);
		lastField.setPreferredSize(new Dimension(120, 30));
		
		namePlate.setAlignmentX(Component.LEFT_ALIGNMENT);
		namePlate.setFont(new Font(namePlate.getFont().getName(), Font.PLAIN, 24));
		add(Box.createHorizontalStrut(15));
		add(namePlate);
		updateNamePlate();
	}
	
	public void setPlateName(String firstName, String lastName) {
		firstField.setText(firstName);
		lastField.setText(lastName);
		updateNamePlate();
	}
	
	private void updateNamePlate() {
		String name;
		if (firstField.getText().isEmpty() && lastField.getText().isEmpty() ) {
			name = "New Contact";
		} else if (firstField.getText().isEmpty()) {
			name = lastField.getText();
		} else {
			name = firstField.getText() + " " + lastField.getText();
		}
		namePlate.setText(name);
	}
	
	public String getFirstName() {
		return firstField.getText();
	}
	
	public String getLastName() {
		return lastField.getText();
	}
	
	public void setEditMode(boolean edit) {
		if (editMode != edit) {
			if (edit) {
				this.removeAll();
				add(Box.createHorizontalStrut(15));
				add(firstField);
				add(lastField);
			} else if (!edit) {
				this.removeAll();
				add(Box.createHorizontalStrut(15));
				add(namePlate);
				updateNamePlate();
			}
			editMode = edit;
			this.revalidate();
			this.repaint();
		}
	}

}
