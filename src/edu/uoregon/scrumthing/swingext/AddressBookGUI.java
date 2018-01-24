package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.uoregon.scrumthing.Controller;

public class AddressBookGUI<T> extends JFrame {
	private static final long serialVersionUID = 3229404744050899834L;
	
	private Controller controller;
	private AddressDetailPanel detailPane;
	
	// TODO: need controller
	public AddressBookGUI(Controller controller) {
		super("Address Book");
		// this.data = data;
		this.controller = controller;
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setJMenuBar(createMenuBar());
		this.setContentPane(createContentPane());
 
        //Display
		this.setSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(320, 240));
		this.setVisible(true);
		
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	if (!saveChange()) return;
            	AddressBookGUI.this.dispose();
            	
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
					/*
					try {
						// TODO: reference controller
						// EntryContainer<?> newEmptyData = data.getClass().newInstance();
						// new AddressBookGUI(newEmptyData);
					} catch (InstantiationException | IllegalAccessException e1) {
						// TODO: Warn user
						e1.printStackTrace();
					}*/
		
				}
			});
			
			JMenuItem itemOpen = new JMenuItem("Open...");
			itemOpen.setMnemonic(KeyEvent.VK_O);
			itemOpen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO: Implement open
				}
			});
			
			JMenuItem itemSave = new JMenuItem("Save...");
			itemSave.setMnemonic(KeyEvent.VK_S);
			itemSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO: Implement save
				}
			});
			
			JMenuItem itemSaveAs = new JMenuItem("Save As...");
			itemSaveAs.setMnemonic(KeyEvent.VK_A);
			itemSaveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO: Implement save as
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
					// TODO: close all
					System.exit(0);
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
		JComboBox<String> combo = new JComboBox<String>();
		combo.setPreferredSize(new Dimension(100,20));
		combo.addItem("Name");
		combo.addItem("ZIP");
		toolbar.add(combo);
		toolbar.add(new JButton("+"));
		toolbar.add(new JButton("x"));
		return toolbar;
	}

	private JPanel createContentPane() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		ObjectListModel listModel = new ObjectListModel(controller.getEntryList());
		JList<String> nameList = new JList<>(listModel);
		JScrollPane splitLeft = new JScrollPane(nameList);
		splitLeft.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		splitLeft.setMinimumSize(new Dimension(100, 0));
		splitLeft.setPreferredSize(new Dimension(150, 0));
		
		JScrollPane splitRight = new JScrollPane();
		splitRight.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		splitRight.setMinimumSize(new Dimension(250, 0));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeft, splitRight);
		
		nameList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				splitRight.remove(detailPane);
				if (e.getFirstIndex() < 0) {
					detailPane = new AddressDetailPanel(null);
					splitRight.setViewportView(detailPane);
				} else {
					detailPane = new AddressDetailPanel(controller, e.getFirstIndex());
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
		
		contentPanel.add(new JLabel("Opened"), BorderLayout.PAGE_END);
		
		return contentPanel;
	}
	
	public boolean saveChange() {
		return true;
	}
	
	public void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
}
