package edu.uoregon.scrumthing.swingext;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.uoregon.scrumthing.Entry;
import edu.uoregon.scrumthing.EntryContainer;

public class AddressBookGUI extends JFrame {
	private static final long serialVersionUID = 3229404744050899834L;
	private EntryContainer<?> data;

	private EntryListModel list;
	private AddressDetailPanel detailPane;
	
	public AddressBookGUI(EntryContainer<?> data) {
		super("Address Book");
		this.data = data;
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setJMenuBar(createMenuBar());
		this.setContentPane(createContentPane());
 
        //Display
		this.setSize(640, 480);
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
					try {
						EntryContainer<?> newEmptyData = data.getClass().newInstance();
						new AddressBookGUI(newEmptyData);
					} catch (InstantiationException | IllegalAccessException e1) {
						// TODO: Warn user
						e1.printStackTrace();
					}
		
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
	
	private JPanel createContentPane() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		list = new EntryListModel(data);
		JList<Entry> nameList = new JList<Entry>(list);
		nameList.setFixedCellWidth(150);
		contentPanel.add(nameList, BorderLayout.LINE_START);
		
		detailPane = new AddressDetailPanel(null);
		contentPanel.add(detailPane, BorderLayout.CENTER);
		
		nameList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				contentPanel.remove(detailPane);
				if (e.getFirstIndex() < 0) {
					detailPane = new AddressDetailPanel(null);
					contentPanel.add(detailPane, BorderLayout.CENTER);
				} else {
					detailPane = new AddressDetailPanel(list.getElementAt(e.getFirstIndex()));
					contentPanel.add(detailPane, BorderLayout.CENTER);
				}
				contentPanel.revalidate();
				contentPanel.repaint();
				
				
			}
		} );
		
		return contentPanel;
	}
	
	public boolean saveChange() {
		return true;
	}
	
	public void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
}
