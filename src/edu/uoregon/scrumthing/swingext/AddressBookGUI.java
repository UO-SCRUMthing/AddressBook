package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.uoregon.scrumthing.Controller;
import edu.uoregon.scrumthing.Entry;

public class AddressBookGUI extends JFrame {
	private static final long serialVersionUID = 3229404744050899834L;
	private static final String SaveFileExtension = "tsv";
	private static final String ImportFileExtension = "tsv";
	
	protected Controller controller;
	private AddressDetailPanel detailPane;
	@SuppressWarnings("rawtypes")
	private JList contactList;
	private JScrollPane splitRight;
	private int selectedIndex;
	private boolean editing;
	
	private JPanel status;
	private JLabel statusText;
	
	private JComboBox<String> sortCombo;
	
	private ArrayList<JComponent> disableComponents;
	
	// TODO: need controller
	public AddressBookGUI(Controller controller) {
		super("Address Book");
		// this.data = data;
		this.disableComponents = new ArrayList<JComponent>();
		this.editing = false;
		this.controller = controller;
		this.selectedIndex = -1;
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setJMenuBar(createMenuBar());
		this.setContentPane(createContentPane());
 
        //Display
		this.setSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(320, 240));
		this.setVisible(true);
		
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            		controller.closeAddressBook();
            }
        });
        
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		{
			/* FileMenu */ 
			fileMenu.setMnemonic(KeyEvent.VK_F);
			
			JMenuItem itemNew = new JMenuItem("New...");
			itemNew.setMnemonic(KeyEvent.VK_N);
			itemNew.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO: name input
					controller.createNewAddressBook();
				}
			});
			
			JMenuItem itemOpen = new JMenuItem("Open...");
			itemOpen.setMnemonic(KeyEvent.VK_O);
			itemOpen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileDiag = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Address Book File (*."+SaveFileExtension+")", SaveFileExtension);
					fileDiag.setFileFilter(filter);
				    int returnVal = fileDiag.showOpenDialog(AddressBookGUI.this);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    		// Open file and notice any failure
				    		Controller newApp = controller.createNewAddressBook();
				    		if (newApp.openAddressBook(fileDiag.getSelectedFile().getAbsolutePath()) <= 0) {
				    			notice("Failed to open file: " + fileDiag.getSelectedFile().getName(), 2);
				    			newApp.closeAddressBook();
				    		} else {
				    			notice("New window opened.", 0);
				    		}
				    }
				}
			});
			
			JMenuItem itemSave = new JMenuItem("Save...");
			itemSave.setMnemonic(KeyEvent.VK_S);
			itemSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.saveAddressBook();
				}
			});
			
			JMenuItem itemSaveAs = new JMenuItem("Save As...");
			itemSaveAs.setMnemonic(KeyEvent.VK_A);
			itemSaveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileDiag = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Address Book File (*."+SaveFileExtension+")", SaveFileExtension);
					fileDiag.setFileFilter(filter);
				    int returnVal = fileDiag.showSaveDialog(AddressBookGUI.this);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    		// Add a extension if there isn't one
				    	   	String filename = fileDiag.getSelectedFile().getAbsolutePath();
				    	   	if (!filename .endsWith("."+SaveFileExtension)) filename += "."+SaveFileExtension;
				    	   	controller.saveAsAddressBook(filename);
				    }
				}
			});
			
			JMenuItem itemClose = new JMenuItem("Close");
			itemClose.setMnemonic(KeyEvent.VK_C);
			itemClose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Trigger window closing events
					close();
				}
			});
			
			JMenuItem itemQuit = new JMenuItem("Close all and quit");
			itemQuit.setMnemonic(KeyEvent.VK_Q);
			itemQuit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.closeAllAddressBook();
				}
			});
		
			
			fileMenu.add(itemNew);
			fileMenu.add(itemOpen);
			fileMenu.add(itemSave);
			fileMenu.add(itemSaveAs);
			fileMenu.addSeparator();
			fileMenu.add(itemClose);
			fileMenu.add(itemQuit);
		}

		menuBar.add(fileMenu);
		return menuBar;
	}
	
	private JPanel createToolbar() {
		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEADING));
		sortCombo = new JComboBox<String>();
		sortCombo.setPreferredSize(new Dimension(100,20));
		sortCombo.addItem("name");
		sortCombo.addItem("zip");
		sortCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		        int state = e.getStateChange();
		        if (state == ItemEvent.SELECTED) {
		        	controller.sortBy((String)e.getItem());
		        	// TODO: Add warning to user when editing. or disable sorting when editing
		        	updateList();
		        }
			}
		});
		toolbar.add(sortCombo);
		// Disable sort while editing
		disableComponents.add(sortCombo);
		
		JButton newEntryButton = new JButton("+");
		newEntryButton.setToolTipText("Create new contact");
		JButton removeEntryButton = new JButton("x");
		removeEntryButton.setToolTipText("Remove selected contact");
		
		newEntryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				splitRight.remove(detailPane);
				selectedIndex = -1;
				contactList.clearSelection();

				Entry template = controller.createEmptyEntry();
				detailPane = new AddressDetailPanel(AddressBookGUI.this, template);
				splitRight.setViewportView(detailPane);
				
				splitRight.revalidate();
				splitRight.repaint();
			}			
		});
		
		removeEntryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int deletingIndex = selectedIndex;
				// TODO: notify user nothing changed
				if (deletingIndex < 0) {
					notice("Please select a contact to delete", 1);
					return;
				}
				contactList.clearSelection();
				selectedIndex = -1;
				controller.deleteEntry(deletingIndex);
				updateList();
			}
		});
		
		toolbar.add(newEntryButton);
		toolbar.add(removeEntryButton);
		disableComponents.add(newEntryButton);
		disableComponents.add(removeEntryButton);
		return toolbar;
	}

	private JPanel createContentPane() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		contactList = new JList<>();
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateList();
		JScrollPane splitLeft = new JScrollPane(contactList);
		splitLeft.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		splitLeft.setMinimumSize(new Dimension(100, 0));
		splitLeft.setPreferredSize(new Dimension(150, 0));
		
		splitRight = new JScrollPane();
		splitRight.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		splitRight.setMinimumSize(new Dimension(250, 0));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeft, splitRight);
		
		contactList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				splitRight.remove(detailPane);
				selectedIndex = contactList.getMinSelectionIndex();
				if (selectedIndex < 0) {
					selectedIndex = -1;
					detailPane = new AddressDetailPanel(null);
					splitRight.setViewportView(detailPane);
				} else {
					detailPane = new AddressDetailPanel(AddressBookGUI.this, selectedIndex);
					splitRight.setViewportView(detailPane);
				}
				splitRight.revalidate();
				splitRight.repaint();
				
				
			}
		});
		
		JPanel toolbar = createToolbar();
		contentPanel.add(toolbar, BorderLayout.PAGE_START);
		
		detailPane = new AddressDetailPanel(null);
		splitRight.add(detailPane);
		
		contentPanel.add(splitPane, BorderLayout.CENTER);
		
		status = new JPanel(new BorderLayout());
		status.setPreferredSize(new Dimension(0, 25));
		status.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		statusText = new JLabel("Opened");
		status.add(statusText, BorderLayout.LINE_START);
		notice("Opened", 0);
		contentPanel.add(status, BorderLayout.PAGE_END);
		
		return contentPanel;
	}
	
	public void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	protected void enterEditMode() {
		for (JComponent comp : disableComponents) {
			comp.setEnabled(false);
		}
		contactList.setEnabled(false);
		editing = true;
		notice(" ", 0);
	}
	
	protected void exitEditMode(boolean selectNew) {
		for (JComponent comp : disableComponents) {
			comp.setEnabled(true);
		}
		editing = false;
		contactList.setEnabled(true);
		updateList();
		if (selectNew) {
			if (!controller.getEntryList().isEmpty())
				contactList.setSelectedIndex(controller.getEntryList().size() - 1);
		}
	}
	
	protected void removeDetailPanel() {
		detailPane = new AddressDetailPanel(null);
		splitRight.setViewportView(detailPane);
		splitRight.revalidate();
		splitRight.repaint();
	}
	
	@SuppressWarnings("unchecked")
	public void updateList() {
		int lastSelected = selectedIndex;
		contactList.setListData(controller.getEntryList().toArray());
		contactList.revalidate();
		contactList.repaint();
		contactList.setSelectedIndex(lastSelected);
	}
	
	public void notice(String text, int warning_level) {
		statusText.setText(text);
		switch (warning_level) {
		case 0:
			status.setBackground(Color.WHITE);
			break;
		case 1:
			status.setBackground(Color.YELLOW);
			break;
		case 2:
			status.setBackground(new Color(255,80,80));
			break;
		default:
			status.setBackground(Color.WHITE);
			break;
		}
		status.revalidate();
		status.repaint();
		
	}
	
	public void setNoSorting() {
		sortCombo.setSelectedIndex(-1);
	}
}
