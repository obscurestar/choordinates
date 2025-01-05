package choordinates;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class TuningDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField mTextTuning;
	private JTextField mTextStrings;
	private JComboBox<String> mComboTunings;
	private boolean mRefreshing = false;
	
	private ChoordData mChoordData;
	
	/**
	 * Create the frame.
	 */

	public TuningDialog() {
		mChoordData = ChoordData.read();

		setTitle("Tunings");
		setBounds(180, 180, 320, 211);
		//setUndecorated(true);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTunings = new JLabel("Tunings");
		lblTunings.setBounds(135, 6, 61, 16);
		contentPane.add(lblTunings);
		
		mComboTunings = new JComboBox<>();
		mComboTunings.setToolTipText("Selected a tuning to see its settings and edit or delete it.");
		mComboTunings.setBounds(6, 30, 308, 27);
		// Add an ActionListener to handle item selection
        mComboTunings.addActionListener(e -> {
        	if (!mRefreshing)
        	{
        		mChoordData.setCurrentTuning( mComboTunings.getSelectedIndex());
        		refresh();
        	}
        });
		contentPane.add(mComboTunings);
		
		JLabel lblTuningName = new JLabel("Tuning Name");
		lblTuningName.setBounds(12, 94, 100, 16);
		contentPane.add(lblTuningName);
		
		mTextTuning = new JTextField();
		mTextTuning.setToolTipText("Name of this tuning.");
		mTextTuning.setBounds(106, 89, 200, 26);
		contentPane.add(mTextTuning);
		mTextTuning.setColumns(10);
		
		JLabel lblStrings = new JLabel("Strings");
		lblStrings.setBounds(51, 122, 61, 16);
		contentPane.add(lblStrings);
		
		mTextStrings = new JTextField();
		mTextStrings.setToolTipText("A list of 1 or more notes in the form o of note  name,  followed by sharp or flat characters and delimited by space, comma or tab.  EX:  E A D G e  Note that the upper register is selected by using lowercase.  e is an octave about E.  This allows tunings like Uke gCEA to display chords effectively. ");
		mTextStrings.setBounds(106, 117, 200, 26);
		contentPane.add(mTextStrings);
		mTextStrings.setColumns(10);
		
		JButton btnNew = new JButton("New");
		btnNew.setBounds(6, 150, 80, 29);
		btnNew.addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (saveTuning(-1) )
            	{
            		mChoordData.setCurrentTuning(mChoordData.getNumTunings() - 1);
            		refresh();
            	}
            }
        });
		contentPane.add(btnNew);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(79, 150, 80, 29);
		btnSave.addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	int id = mChoordData.getCurrentTuning();
            	
            	if (saveTuning(id))
            	{
            		refresh();
            	}
            }
        });
		contentPane.add(btnSave);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(152, 150, 80, 29);
		btnDelete.addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	int id  = mChoordData.getCurrentTuning();
            	
            	if (id == -1)
            	{
            		return;
            	}
            	
            	String name = mChoordData.getTuning(id).getName();
            	
            	if (confirm("Delete Tuning", name))
            	{
            		mChoordData.deleteTuning(id);
            		mChoordData.setCurrentTuning(-1); 
            		refresh();
            	}
            }
		});
		contentPane.add(btnDelete);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(226, 150, 80, 29);
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
		});
		contentPane.add(btnCancel);
		refresh();
		setVisible(true);
	}
	
	private void closeWindow()
	{
    	String tuning_name = mTextTuning.getText();
    	String string_names = mTextStrings.getText();
    	int id = mChoordData.getCurrentTuning();
    	
    	boolean do_dialog = false;
    	
    	if (id == -1)
    	{
    		System.out.println("ID is -1");
    		if (tuning_name.compareTo("") != 0 || string_names.compareTo("") != 0)
    		{
    			do_dialog = true;
    		}
    	}
    	else
    	{
    		if (tuning_name.compareTo( mChoordData.getTuning(id).getName() ) != 0 
    				|| string_names.compareTo(makeStringsText(id)) != 0)
    		{
    			do_dialog = true;
    		}
    	}
    	
    	if ( do_dialog )
    	{
    		//Ugly variable overloading!
    		do_dialog = !confirm("Unsaved Changes", "Confirm Close");
    	}
    	
    	if (!do_dialog)
    	{
    		mChoordData.write();
    		this.dispose();
    	}
	}
	
	private boolean confirm(String title, String message)
	{
		ImageIcon icon = new ImageIcon("choordinates64.png");
        int result = JOptionPane.showConfirmDialog(null, 
                                                  message, 
                                                  title, 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.QUESTION_MESSAGE,
                                                  icon);

        if (result == JOptionPane.OK_OPTION)
        {
           return true;
        }
        /*else if (result == JOptionPane.CANCEL_OPTION)
        {
        	return false;
	    }*/
        return false;
	}
		
	private String makeStringsText(int id)
	{
		//Generates a space-delimited set of string names from chord id
		ArrayList<String> string_names= mChoordData.getTuning(id).getAllNoteNames();
		
		return String.join(" ", string_names);
	}
	
	private void refresh()
	{
		//This list probably won't be that long.
		//Be lazy, just wipe it out and rebuild it.
		
		mRefreshing = true; //Combobox y u suk?
		
		mComboTunings.removeAllItems();
		for (int i=0;i < mChoordData.getNumTunings(); ++i)
		{
			mComboTunings.addItem(mChoordData.getTuning(i).getName());
		}
		
		//Set tuning to current in data.
		int id = mChoordData.getCurrentTuning();
		if (id > -1)
		{
			mComboTunings.setSelectedIndex(id);
			mTextTuning.setText(mChoordData.getTuning(id).getName());
			mTextStrings.setText(makeStringsText(id));
		}
		else
		{
			mTextTuning.setText("");
			mTextStrings.setText("");
		}
	
		mRefreshing = false;
	}
	
	private boolean saveTuning(int tuning_id)
	{
    	String tuning_name = mTextTuning.getText();
    	String string_names = mTextStrings.getText();

    	if (tuning_name.length() ==0)
    	{
    		JOptionPane.showMessageDialog(null, "Tuning name is required.");
    		return false;
    	}
    	
       ToneChord chord;
        
        try
        {
        	chord = ToneChord.parse(string_names);
        }
        catch (IllegalArgumentException exception)
        {
        	JOptionPane.showMessageDialog(null, exception.getMessage());
        	return false;
        }
        
        chord.setName(tuning_name);
        
        if (tuning_id == -1)
        {
        	//Add a new tuning if using Add or save no tunings present.
        	mChoordData.addTuning(chord);
        	mChoordData.setCurrentTuning(0);
        }
        else
        {
        	mChoordData.updateTuning(tuning_id,  chord);
        }
        return true;
	}
}

