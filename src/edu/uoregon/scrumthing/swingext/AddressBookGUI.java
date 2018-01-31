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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.uoregon.scrumthing.Controller;
import edu.uoregon.scrumthing.Entry;

public class AddressBookGUI extends JFrame {
	private static final long serialVersionUID = 3229404744050899834L;
	
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
			itemSaveAs.setMnemonic(KeyEvent.VK_I);
			itemSaveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.createImportDialog();
				}
			});
			
			JMenuItem itemExport = new JMenuItem("Export...");
			itemSaveAs.setMnemonic(KeyEvent.VK_E);
			itemSaveAs.addActionListener(new ActionListener() {
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
			
			JMenuItem itemAddContact = new JMenuItem("Add a contact");
			itemAddContact.setMnemonic(KeyEvent.VK_A);
			itemAddContact.addActionListener(addContactAction);
			
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
		leftToolbar.add(sortCombo);
		// Disable sort while editing
		disableComponents.add(sortCombo);
		
		JButton newEntryButton = new JButton("+");
		newEntryButton.setToolTipText("Create new contact");
		JButton removeEntryButton = new JButton("x");
		removeEntryButton.setToolTipText("Remove selected contact");
		
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
				contactList.clearSelection();
				selectedIndex = -1;
				controller.deleteEntry(deletingIndex);
				updateList();
			}
		});
		
		leftToolbar.add(newEntryButton);
		leftToolbar.add(removeEntryButton);
		
		disableComponents.add(newEntryButton);
		disableComponents.add(removeEntryButton);
		
		JTextField searchField = new JTextField();
		searchField.setMinimumSize(new Dimension(100, 25));
		searchField.setPreferredSize(new Dimension(200, 25));
		JButton searchButton = new JButton("Go");
		JButton clearButton = new JButton("X");
		
		ActionListener searchAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchTerm = searchField.getText();
				if (searchTerm != null && searchTerm.length() > 0)
					updateList(searchTerm);
				else
					updateList();
			}
		};
		
		searchField.addActionListener(searchAction);
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (e.getDocument().getLength() == 0) {
					updateList();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (e.getDocument().getLength() == 0) {
					updateList();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (e.getDocument().getLength() == 0) {
					updateList();
				}
			}
		});
		
		searchButton.addActionListener(searchAction);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.setText("");
			}
		});
		
		disableComponents.add(searchField);
		disableComponents.add(clearButton);
		disableComponents.add(searchButton);
		
		rightToolbar.add(searchField);
		rightToolbar.add(clearButton);
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
	
	@SuppressWarnings("unchecked")
	public void updateList(String searchTerm) {
		int lastSelected = selectedIndex;
		contactList.setListData(controller.getEntryList(searchTerm).toArray());
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
