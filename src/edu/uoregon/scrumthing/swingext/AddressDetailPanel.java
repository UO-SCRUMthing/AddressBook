package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uoregon.scrumthing.Entry;

public class AddressDetailPanel extends JPanel {
	private static final long serialVersionUID = -7042296921092780861L;

	private HashMap<String, JComponent> fields;
	private JPanel fieldPane;
	
	public AddressDetailPanel(Entry entry) {
		super(new BorderLayout());
		if (entry == null) return;
		fields = new HashMap<String, JComponent>();
		
		fieldPane = new JPanel();
		fieldPane.setLayout(new BoxLayout(fieldPane, BoxLayout.Y_AXIS));
		
		JLabel titleName = new JLabel(entry.getName());
		titleName.setFont(new Font(titleName.getFont().getName(), Font.PLAIN, 24));
		add(titleName, BorderLayout.PAGE_START);
		add(fieldPane, BorderLayout.CENTER);
		
		
		List<SimpleEntry<String, String>> details = entry.getDetailList();
		for(SimpleEntry<String, String> field : details) {
			String key = field.getKey();
			String value = field.getValue();		
			
			addField(key, value);
		}
		
		fieldPane.add(Box.createVerticalGlue());
		
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
