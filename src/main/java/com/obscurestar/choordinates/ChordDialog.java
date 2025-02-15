package com.obscurestar.choordinates;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;

import java.util.ArrayList;

public class ChordDialog extends JDialog
{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList<String> mListChords;
	private JTextField mTextName;
	private JTextField mTextAliases;
	private JTextField mTextIntervals;
	private JTextField mTextSymbol;
	
	private boolean mRefreshing = false;
	
	/**
	 * Create the dialog.
	 */
		
	public ChordDialog() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow( false );
            }
            
            @Override
            public void windowActivated(WindowEvent e) {
                refresh();
            }
        });
		//setUndecorated(true);

		setTitle("Chord Dictionary");
		
		//setBounds(150, 150, 490, 258);
		int[] bounds = ChoordData.getInstance().getPreferences().getChordRect();
		setBounds( bounds[0], bounds[1], bounds[2], bounds[3] );

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel panelChordDict = new JPanel();
			contentPanel.add(panelChordDict);
			GridBagLayout gbl_panelChordDict = new GridBagLayout();
			gbl_panelChordDict.columnWidths = new int[] {150, 45, 0, 10};
			gbl_panelChordDict.rowHeights = new int[]{16, 0, 0,0, 34,0,0};
			gbl_panelChordDict.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0};
			gbl_panelChordDict.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
			panelChordDict.setLayout(gbl_panelChordDict);
			{
				JLabel lblChordList = new JLabel("Chords");
				GridBagConstraints gbc_lblChordList = new GridBagConstraints();
				gbc_lblChordList.gridwidth = 2;
				gbc_lblChordList.insets = new Insets(0, 0, 5, 5);
				gbc_lblChordList.gridx = 0;
				gbc_lblChordList.gridy = 0;
				panelChordDict.add(lblChordList, gbc_lblChordList);
			}
			{
				mListChords = new JList<>();
				mListChords.setToolTipText("Select a chord to view its details and edit it. ");
				GridBagConstraints gbc_mListChords = new GridBagConstraints();
				gbc_mListChords.gridheight = 5;
				gbc_mListChords.gridwidth = 2;
				gbc_mListChords.insets = new Insets(0, 0, 5, 5);
				gbc_mListChords.fill = GridBagConstraints.BOTH;
				gbc_mListChords.gridx = 0;
				gbc_mListChords.gridy = 1;
				mListChords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	            mListChords.addListSelectionListener(new ListSelectionListener() {
	                @Override
	                public void valueChanged(ListSelectionEvent e) {
	                	if (!mRefreshing)
	                	{
		                    if (!e.getValueIsAdjusting()) { // Check if selection is finalized
		                    	ChoordData choord_data = ChoordData.getInstance();
		                    	choord_data.setCurrentChord( mListChords.getSelectedIndex());
		                    	refresh();
		                    	refreshMain();
		                    }
	                	}
	                }
	            });
			 	JLabel lblName = new JLabel("Name");
			 	GridBagConstraints gbc_lblName = new GridBagConstraints();
			 	gbc_lblName.insets = new Insets(0, 0, 5, 5);
			 	gbc_lblName.gridx = 2;
			 	gbc_lblName.gridy = 1;
			 	panelChordDict.add(lblName, gbc_lblName);
	
			 	mTextName = new JTextField();
			 	GridBagConstraints gbc_mTextName = new GridBagConstraints();
			 	gbc_mTextName.insets = new Insets(0, 0, 5, 0);
			 	gbc_mTextName.gridx = 3;
			 	gbc_mTextName.gridy = 1;
			 	panelChordDict.add(mTextName, gbc_mTextName);
			 	mTextName.setToolTipText("Name of the chord eg: Major, minor, dominant, 7th");
			 	mTextName.setColumns(10);
			 	
		        JScrollPane scrollListChords = new JScrollPane(mListChords);
				 panelChordDict.add(scrollListChords, gbc_mListChords);
			}
			{
				JLabel lblSymbol = new JLabel("Symbol");
				GridBagConstraints gbc_lblSymbol = new GridBagConstraints();
				gbc_lblSymbol.insets = new Insets(0, 0, 5, 5);
				gbc_lblSymbol.gridx = 2;
				gbc_lblSymbol.gridy = 2;
				panelChordDict.add(lblSymbol, gbc_lblSymbol);
			}
			{
				mTextSymbol = new JTextField();
				mTextSymbol.setToolTipText("Shorthand symbol for this chord. ex m for minor, 7 for 7th, m7 minor 7th,  + for augmented and so on.  Use \"\" for no name as in Major chords.");
				GridBagConstraints gbc_mTextSymbol = new GridBagConstraints();
				gbc_mTextSymbol.insets = new Insets(0, 0, 5, 0);
				gbc_mTextSymbol.gridx = 3;
				gbc_mTextSymbol.gridy = 2;
				panelChordDict.add(mTextSymbol, gbc_mTextSymbol);
				mTextSymbol.setColumns(10);
			}
			{
				JLabel lblAlias = new JLabel("Aliases");
				GridBagConstraints gbc_lblAlias = new GridBagConstraints();
				gbc_lblAlias.insets = new Insets(0, 0, 5, 5);
				gbc_lblAlias.gridx = 2;
				gbc_lblAlias.gridy = 3;
				panelChordDict.add(lblAlias, gbc_lblAlias);
				lblAlias.setToolTipText("Comma delimited list.  EG");
			}
						{
							mTextAliases = new JTextField();
							mTextAliases.setToolTipText("Alternate names for this chord.");
							GridBagConstraints gbc_mTextAliases = new GridBagConstraints();
							gbc_mTextAliases.insets = new Insets(0, 0, 5, 0);
							gbc_mTextAliases.gridx = 3;
							gbc_mTextAliases.gridy = 3;
							panelChordDict.add(mTextAliases, gbc_mTextAliases);
							mTextAliases.setColumns(10);
						}
						{
							JLabel lblInterval = new JLabel("Intervals");
							GridBagConstraints gbc_lblInterval = new GridBagConstraints();
							gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
							gbc_lblInterval.gridx = 2;
							gbc_lblInterval.gridy = 4;
							panelChordDict.add(lblInterval, gbc_lblInterval);
						}
						{
							mTextIntervals = new JTextField();
							mTextIntervals.setToolTipText("A string of intervals.  EG: 1 3 5 is a Major chord, 1 3b 5 is a minor chord.   Inversions may be specified as negative intervals.  .");
							GridBagConstraints gbc_mTextIntervals = new GridBagConstraints();
							gbc_mTextIntervals.insets = new Insets(0, 0, 5, 0);
							gbc_mTextIntervals.gridx = 3;
							gbc_mTextIntervals.gridy = 4;
							panelChordDict.add(mTextIntervals, gbc_mTextIntervals);
							mTextIntervals.setColumns(10);
						}
					
					
						JButton btnDelete = new JButton("Delete");
						btnDelete.setToolTipText("Delete the currently selected chord.");
						GridBagConstraints gbc_btnDelete = new GridBagConstraints();
						gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
						gbc_btnDelete.gridx = 0;
						gbc_btnDelete.gridy = 6;
						panelChordDict.add(btnDelete, gbc_btnDelete);
						btnDelete.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								ChoordData choord_data = ChoordData.getInstance();
								int id  = choord_data.getCurrentChord();
		            	
								if (id == -1)
								{
									return;
								}
		            	
								String name = choord_data.getChord(id).getName();
		            	
								if (Choordinates.confirm("Delete Chord", name))
								{
									choord_data.deleteChord(id);
									choord_data.setCurrentChord(-1); 
									choord_data.write();
									refresh();
								}
							}
						});
					
					
					JButton btnNew = new JButton("New");
					btnNew.setToolTipText("Create a new chord from the d fields on the right.");
					GridBagConstraints gbc_btnNew = new GridBagConstraints();
					gbc_btnNew.insets = new Insets(0, 0, 0, 5);
					gbc_btnNew.gridx = 1;
					gbc_btnNew.gridy = 6;
					panelChordDict.add(btnNew, gbc_btnNew);
					btnNew.addActionListener(new ActionListener()
					{
		            @Override
		            public void actionPerformed(ActionEvent e)
		            {
		            	if (saveChord(-1) )
		            	{
		            		ChoordData choord_data = ChoordData.getInstance();
		            		choord_data.setCurrentChord(choord_data.getNumChords() - 1);
		            		refresh();
		            		refreshMain();
		            	}
		            }
		        });
				
				
					JButton btnSave = new JButton("Save");
					btnSave.setToolTipText("Replace the current chord.  If no chdefined, acts like new.");
					GridBagConstraints gbc_btnSave = new GridBagConstraints();
					gbc_btnSave.insets = new Insets(0, 0, 0, 5);
					gbc_btnSave.gridx = 2;
					gbc_btnSave.gridy = 6;
					panelChordDict.add(btnSave, gbc_btnSave);
							
							
								JButton btnClose = new JButton("Close");
								btnClose.setToolTipText("Close the window.");
								GridBagConstraints gbc_btnClose = new GridBagConstraints();
								gbc_btnClose.gridx = 3;
								gbc_btnClose.gridy = 6;
								panelChordDict.add(btnClose, gbc_btnClose);
								btnClose.setActionCommand("Cancel");
						btnClose.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e) {
		                closeWindow( true );
		            }
						});
				btnSave.addActionListener(new ActionListener()
				{
		            @Override
		            public void actionPerformed(ActionEvent e)
		            {
		            	ChoordData choord_data = ChoordData.getInstance();
		            	int id = choord_data.getCurrentChord();
		            	
		            	if (saveChord(id))
		            	{
		            		refresh();
		            		refreshMain();
		            	}
		            }
		        });
		}
		refresh();
		setVisible(true);
	}

	private boolean changed(int id)
	{
		ChoordData choord_data = ChoordData.getInstance();

    	String name = mTextName.getText();
    	String aliases = mTextAliases.getText();
    	String symbol = mTextSymbol.getText();
    	String intervals = mTextIntervals.getText();
    	
    	boolean result = false;
    	
    	if (id == -1)
    	{
    		if (name.compareTo("") != 0
    				|| intervals.compareTo("") != 0
    				|| symbol.compareTo("") != 0
    				|| aliases .compareTo("") != 0)
    		{
    			result = true;
    		}
    	}
    	else
    	{
    		if (name.compareTo( choord_data.getChord(id).getName() ) != 0 
    				|| intervals.compareTo(makeIntervalsText(id)) != 0
    				|| symbol.compareTo(choord_data.getChord(id).getSymbol()) != 0
    				|| aliases.compareTo( choord_data.getChord(id).getAliasesString()) != 0)
    		{
    			result = true;
    		}
    	}
		return result;
	}
	
	private void refreshMain()
	{
		/*
		//Tells the main window to update its data
		if (mOwner != null)
		{
			mOwner.refresh();
		}*/
	}
	
	private void closeWindow(boolean from_button)
	{
		ChoordData choord_data = ChoordData.getInstance();

		//Handle window closing from button or window.
    	int id = choord_data.getCurrentChord();
		boolean confirm_write = false;
    	if ( changed(id) )
    	{
    		if (from_button)
    		{
    			confirm_write = Choordinates.confirm("Unsaved Changes", "Confirm Close");
    		}
    		else
    		{
    			confirm_write = Choordinates.confirm("Window closing", "Save changes?");
    		}
    	}
    	else if (from_button)
    	{
    		this.setVisible(false);
    	}
    	
    	choord_data.getPreferences().setChordRect( getBounds() );
    	
    	if (confirm_write && !ChoordData.CLOSING) //Don't double-save on app close.
    	{
    		choord_data.write();
    		refreshMain();
    		this.setVisible(false);
    	}
	}
	
	private String makeIntervalsText(int id)
	{
		ChoordData choord_data = ChoordData.getInstance();

		//Generates a space-delimited set of string names from chord id
		ArrayList<String> string_names= choord_data.getChord(id).getAllNoteNames();
		
		return String.join(" ", string_names);
	}
	

	public void refresh()
	{
		ChoordData choord_data = ChoordData.getInstance();

		//This list probably won't be that long.
		//Be lazy, just wipe it out and rebuild it.
		
		mRefreshing = true; //Combobox y u suk?
		
		mListChords.removeAll();
		
		//We could just return the arraylist from choord_data
		//and convert it to a list, but why do anything the
		//easy way?
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (int i=0;i < choord_data.getNumChords(); ++i) {
            listModel.addElement(choord_data.getChord(i).getLongName());
        }

        // Set the list model to the JList
        mListChords.setModel(listModel);
		
		//Set Chord to current in data.
		int id = choord_data.getCurrentChord();
		if (id > -1)
		{
			mListChords.setSelectedIndex(id);
			mTextName.setText(choord_data.getChord(id).getName());
			mTextSymbol.setText(choord_data.getChord(id).getSymbol());
			mTextAliases.setText(choord_data.getChord(id).getAliasesString());
			mTextIntervals.setText(makeIntervalsText(id));
		}
		else
		{
			mTextName.setText("");
			mTextSymbol.setText("");
			mTextAliases.setText("");
			mTextIntervals.setText("");
		}
	
		mRefreshing = false;
	}
	
	private boolean saveChord(int chord_id)
	{
		ChoordData choord_data = ChoordData.getInstance();

    	String name = mTextName.getText();
    	String symbol = mTextSymbol.getText();
    	String aliases = mTextAliases.getText();
    	String intervals = mTextIntervals.getText();

    	if (name.length() ==0)
    	{
    		JOptionPane.showMessageDialog(null, "Chord name is required.");
    		return false;
    	}
    	
       IntervalChord chord;
        
        try
        {
        	chord = new IntervalChord(intervals);
        }
        catch (IllegalArgumentException exception)
        {
        	JOptionPane.showMessageDialog(null, exception.getMessage());
        	return false;
        }
        
        chord.setName(name);
        chord.setSymbol(symbol);
        chord.setAliasesFromString(aliases);
        
        if (chord_id == -1)
        {
        	//Add a new chord if using Add or save no chords present.
        	choord_data.addChord(chord);
        	choord_data.setCurrentChord(0);
        }
        else
        {        	
        	if (changed(chord_id))
    		{
        		if (Choordinates.confirm("Confirm", "Update " + choord_data.getChord(chord_id).getName() + "?"))
        		{
        			choord_data.updateChord(chord_id,  chord);
        		}
        		else
        		{
        			return false;
        		}
    		}
        }
        choord_data.write();
        return true;
	}
}
