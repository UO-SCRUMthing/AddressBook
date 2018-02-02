package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.uoregon.scrumthing.Controller;
import edu.uoregon.scrumthing.Entry;

public class AddressBookGUI extends JFrame {
	private static final long serialVersionUID = 3229404744050899834L;
	// TODO: re-organize ui code
	
	
	protected Controller controller;
	private AddressDetailPanel detailPane;
	@SuppressWarnings("rawtypes")
	private JList contactList;
	private JScrollPane splitRight;
	private int selectedIndex;
	private boolean editing;
	
	private ArrayList<JComponent> disableComponents;
	
	// Status
	private JPanel status;
	private JLabel statusText;
	
	// Sort
	private JComboBox<String> sortCombo;
	
	// Search
	private JLabel listHeader;
	private JButton clearButton;
	private JTextField searchField;
	private JButton newEntryButton;
	private JMenuItem itemAddContact;
	
	private ActionListener addContactAction = new ActionListener() {
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
	};
	
	// TODO: need controller
	public AddressBookGUI(JFrame relative, Controller controller) {
		super(controller.getAddressBookName());
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
		this.setMinimumSize(new Dimension(480, 300));
		
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            		controller.closeAddressBook();
            }
        });
        
        this.setLocationRelativeTo(relative);
        if (relative != null)
        		this.setLocation(this.getLocation().x+20, this.getLocation().y+20);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		{
			/* FileMenu */ 
			fileMenu.setMnemonic(KeyEvent.VK_F);
			
			JMenuItem itemNew = new JMenuItem("New");
			itemNew.setMnemonic(KeyEvent.VK_N);
			itemNew.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO: name input
					controller.createWindowForNewAddressBook();
				}
			});
			
			JMenuItem itemOpen = new JMenuItem("Open...");
			itemOpen.setMnemonic(KeyEvent.VK_O);
			itemOpen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.createWindowForExistingAddressBook();
				}
			});
			
			JMenuItem itemSave = new JMenuItem("Save...");
			itemSave.setMnemonic(KeyEvent.VK_S);
			itemSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.saveCurrentAddressBook();
				}
			});
			
			JMenuItem itemSaveAs = new JMenuItem("Save As...");
			itemSaveAs.setMnemonic(KeyEvent.VK_A);
			itemSaveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.createSaveDialog();
				}
			});
			
			JMenuItem itemImport = new JMenuItem("Import...");
			itemImport.setMnemonic(KeyEvent.VK_I);
			itemImport.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.createImportDialog();
				}
			});
			
			JMenuItem itemExport = new JMenuItem("Export...");
			itemExport.setMnemonic(KeyEvent.VK_E);
			itemExport.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.createExportDialog();
				}
			});
			
			JMenuItem itemClose = new JMenuItem("Close");
			itemClose.setMnemonic(KeyEvent.VK_C);
			itemClose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
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
			fileMenu.add(itemImport);
			fileMenu.add(itemExport);
			fileMenu.addSeparator();
			fileMenu.add(itemClose);
			fileMenu.add(itemQuit);
		}

		JMenu editMenu = new JMenu("Edit");
		{
			/* EditMenu */ 
			editMenu.setMnemonic(KeyEvent.VK_E);
			
			itemAddContact = new JMenuItem("Add a contact");
			itemAddContact.setMnemonic(KeyEvent.VK_A);
			itemAddContact.addActionListener(addContactAction);
			disableComponents.add(itemAddContact);
			
			JMenuItem itemRenameAddressBook = new JMenuItem("Rename address book");
			itemRenameAddressBook.setMnemonic(KeyEvent.VK_R);
			itemRenameAddressBook.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.createRenameDialog();
				}
			});
			
			editMenu.add(itemAddContact);
			editMenu.addSeparator();
			editMenu.add(itemRenameAddressBook);
			
		}
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		return menuBar;
	}
	
	private JPanel createToolbar() {
		JPanel leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JPanel rightToolbar = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JPanel toolbar = new JPanel(new BorderLayout());
		toolbar.setPreferredSize(new Dimension(0, 30));
		
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
		leftToolbar.add(new JLabel("Sort by:"));
		leftToolbar.add(sortCombo);
		leftToolbar.setAlignmentY(CENTER_ALIGNMENT);
		// Disable sort while editing
		disableComponents.add(sortCombo);
		
		searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(100, 20));
		searchField.setPreferredSize(new Dimension(200, 20));
		JButton searchButton = new JButton("Go");

		searchButton.setPreferredSize(new Dimension(30, 20));
		searchButton.setMargin(new Insets(0,0,0,0));
		
		ActionListener searchAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchTerm = searchField.getText();
				if (searchTerm != null && searchTerm.length() > 0)
					enterSearchMode(searchTerm);
				else
					exitSearchMode();
			}
		};
		
		searchField.addActionListener(searchAction);
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (e.getDocument().getLength() == 0) {
					exitSearchMode();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (e.getDocument().getLength() == 0) {
					exitSearchMode();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (e.getDocument().getLength() == 0) {
					exitSearchMode();
				}
			}
		});
		
		searchButton.addActionListener(searchAction);
		
		disableComponents.add(searchField);
		disableComponents.add(searchButton);
		
		rightToolbar.add(searchField);
		rightToolbar.add(searchButton);
		
		toolbar.add(leftToolbar, BorderLayout.LINE_START);
		toolbar.add(rightToolbar, BorderLayout.LINE_END);
		
		return toolbar;
	}

	private JPanel createContentPane() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		contactList = new JList<>();
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateList();
		JPanel splitLeft = new JPanel(new BorderLayout());
		JPanel footer = new JPanel(new FlowLayout());
		
		// List footer buttons
		{
			// TODO: change button placement
			newEntryButton = new JButton("+");
			newEntryButton.setToolTipText("Create new contact");
			newEntryButton.setPreferredSize(new Dimension(20,20));
			newEntryButton.setMargin(new Insets(0, 0, 0, 0));
			JButton removeEntryButton = new JButton("-");
			removeEntryButton.setToolTipText("Remove selected contact");
			removeEntryButton.setPreferredSize(new Dimension(20,20));
			removeEntryButton.setMargin(new Insets(0, 0, 0, 0));
			
			newEntryButton.addActionListener(addContactAction);
			
			removeEntryButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int deletingIndex = selectedIndex;
					// TODO: notify user nothing changed
					if (deletingIndex < 0) {
						notice("Please select a contact to delete", 1);
						return;
					}
					
					int n = JOptionPane.showConfirmDialog(
						    AddressBookGUI.this,
						    "Are you sure you want to delete selected contact?",
						    controller.getAddressBookName(),
						    JOptionPane.YES_NO_OPTION);
					
					if (n == JOptionPane.YES_OPTION) {
						contactList.clearSelection();
						selectedIndex = -1;
						controller.deleteEntry(deletingIndex);
						updateList();
					}
				}
			});
			
			footer.add(newEntryButton);
			footer.add(removeEntryButton);
			
			disableComponents.add(newEntryButton);
			disableComponents.add(removeEntryButton);
		}
		
		JScrollPane listScroll = new JScrollPane(contactList);
		listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroll.setMinimumSize(new Dimension(100, 0));
		listScroll.setPreferredSize(new Dimension(150, 0));
		
		JPanel searchHeader = new JPanel(new BorderLayout());
		listHeader = new JLabel("");
		searchHeader.add(listHeader, BorderLayout.LINE_START);
		
		clearButton = new JButton("x");
		clearButton.setPreferredSize(new Dimension(20,20));
		clearButton.setMargin(new Insets(0, 0, 0, 0));
		// Make it red
		clearButton.setForeground(Color.RED);
		clearButton.setVisible(false);
		//clearButton.setToolTipText("Exit search");
		
		// Change clearButton
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.setText("");
			}
		});
		disableComponents.add(clearButton);
		searchHeader.add(clearButton, BorderLayout.LINE_END);
		
		splitLeft.add(searchHeader, BorderLayout.PAGE_START);
		splitLeft.add(listScroll, BorderLayout.CENTER);
		splitLeft.add(footer, BorderLayout.PAGE_END);
		
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
	
	protected void exitEditMode() {
		for (JComponent comp : disableComponents) {
			comp.setEnabled(true);
		}
		editing = false;
		contactList.setEnabled(true);
		updateList();
	}
	
	protected void removeDetailPanel() {
		detailPane = new AddressDetailPanel(null);
		splitRight.setViewportView(detailPane);
		splitRight.revalidate();
		splitRight.repaint();
	}
	
	public void updateList() {
		if (searchField == null) {
			exitSearchMode();
			return;
		}
		if (!searchField.getText().isEmpty())
			enterSearchMode(searchField.getText());
		else
			exitSearchMode();
	}
	
	@SuppressWarnings("unchecked")
	public void exitSearchMode() {
		if (listHeader != null) listHeader.setText("");
		if (clearButton != null) clearButton.setVisible(false);
		if (newEntryButton != null) newEntryButton.setVisible(true);
		if (itemAddContact != null) itemAddContact.setEnabled(true);
		
		contactList.setListData(controller.getEntryList().toArray());
		int lastSelected = controller.getSelectedIndex();
		contactList.revalidate();
		contactList.repaint();
		if (lastSelected >= 0)
			contactList.setSelectedIndex(lastSelected);
		else
			contactList.clearSelection();
	}
	
	@SuppressWarnings("unchecked")
	public void enterSearchMode(String searchTerm) {
		System.out.println("hey");
		if (listHeader != null) listHeader.setText("Search result:");
		if (clearButton != null) clearButton.setVisible(true);
		if (newEntryButton != null) newEntryButton.setVisible(false);
		if (itemAddContact != null) itemAddContact.setEnabled(false);
		contactList.setListData(controller.getEntryList(searchTerm).toArray());
		int lastSelected = controller.getSelectedIndex();
		contactList.revalidate();
		contactList.repaint();
		if (lastSelected >= 0)
			contactList.setSelectedIndex(lastSelected);
		else
			contactList.clearSelection();
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
}
