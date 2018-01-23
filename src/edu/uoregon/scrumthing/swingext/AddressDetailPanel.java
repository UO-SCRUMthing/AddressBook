package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddressDetailPanel extends JPanel {
	private static final long serialVersionUID = -7042296921092780861L;

	// TODO: Add controller reference
	private HashMap<String, JComponent> fields;
	private JPanel fieldPane;
	private int index;
	private boolean isNew;
	
	public AddressDetailPanel(List<SimpleEntry<String, String>> details, int index) {
		this(details);
		this.index = index;
		isNew = false;
	}
	
	public AddressDetailPanel(List<SimpleEntry<String, String>> details) {
		super(new BorderLayout());
		isNew = true;
		if (details == null) return;
		
		fields = new HashMap<String, JComponent>();
		
		fieldPane = new JPanel();
		fieldPane.setLayout(new BoxLayout(fieldPane, BoxLayout.Y_AXIS));
		
		// TODO: reference controller
		JLabel titleName = new JLabel("Need Controller");
		titleName.setFont(new Font(titleName.getFont().getName(), Font.PLAIN, 24));
		add(titleName, BorderLayout.PAGE_START);
		add(fieldPane, BorderLayout.CENTER);
		
		for(SimpleEntry<String, String> field : details) {
			String key = field.getKey();
			String value = field.getValue();		
			
			addField(key, value);
		}
	}
	
	private void addField(String key, String value) {
		JTextField newTextField = new JTextField(value);
		fields.put(key, newTextField);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		panel.add(new JLabel(key));
		panel.add(newTextField);
		
		panel.setPreferredSize(new Dimension(150, 40));
		panel.setMinimumSize(new Dimension(80, 40));
		panel.setMaximumSize(new Dimension(200, 40));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		fieldPane.add(panel);
		
	}
	
	public void submitChange() {
		
	}
	
}
