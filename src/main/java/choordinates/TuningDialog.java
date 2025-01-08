package choordinates;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;

import java.util.ArrayList;

public class TuningDialog extends JDialog
{

	private static final long serialVersionUID = 1L;
	private JTextField mTextTuning;
	private JTextField mTextStrings;
	private JComboBox<String> mComboTunings;
	private boolean mRefreshing = false;
		
	/**
	 * Create the frame.
	 */
	
	public TuningDialog() {
		//BEWARE of lambdas and instance variables.
		
		//setUndecorated(true);
		
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

		setTitle("Tunings");
		setBounds(180, 180, 320, 211);
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
        		ChoordData.getInstance().setCurrentTuning( mComboTunings.getSelectedIndex());
        		refresh();
        		refreshMain();
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
            		ChoordData choord_data = ChoordData.getInstance();
            		choord_data.setCurrentTuning(choord_data.getNumTunings() - 1);
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
            	int id = ChoordData.getInstance().getCurrentTuning();
            	
            	if (saveTuning(id))
            	{
            		refresh();
            		refreshMain();
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
        		ChoordData choord_data = ChoordData.getInstance();

            	int id  = choord_data.getCurrentTuning();
            	
            	if (id == -1)
            	{
            		return;
            	}
            	
            	String name = choord_data.getTuning(id).getName();
            	
            	if (confirm("Delete Tuning", name))
            	{
            		choord_data.deleteTuning(id);
            		choord_data.setCurrentTuning(-1); 
            		choord_data.write();
            		refresh();
            		refreshMain();
            	}
            }
		});
		contentPane.add(btnDelete);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(226, 150, 80, 29);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
                closeWindow( true );
            }
		});
		contentPane.add(btnClose);
		refresh();
		setVisible(true);
	}
	
	private boolean changed(int id)
	{
		//check for unsaved changes.
		ChoordData choord_data = ChoordData.getInstance();

		boolean result = false;
    	String tuning_name = mTextTuning.getText();
    	String string_names = mTextStrings.getText();
    	    	
    	if (id == -1)
    	{
    		if (tuning_name.compareTo("") != 0 || string_names.compareTo("") != 0)
    		{
    			result = true;
    		}
    	}
    	else
    	{
    		if (tuning_name.compareTo( choord_data.getTuning(id).getName() ) != 0 
    				|| string_names.compareTo(makeStringsText(id)) != 0)
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
		//Handle window closing from button or window.
		ChoordData choord_data = ChoordData.getInstance();

    	int id = choord_data.getCurrentTuning();
    	boolean confirm_write = false;
    	if ( changed(id) )
    	{
    		if (from_button)
    		{
    			confirm_write = confirm("Unsaved Changes", "Confirm Close");
    		}
    		else
    		{
    			confirm_write = confirm("Window closing", "Save changes?");
    		}
    	}
    	else if (from_button)
    	{
    		this.setVisible(false);
    	}
    	
    	if (confirm_write)
    	{
    		choord_data.write();
    		refreshMain();
    		this.setVisible(false);
    	}
	}
	
	private boolean confirm(String title, String message)
	{
        int result = JOptionPane.showConfirmDialog(null, 
                                                  message, 
                                                  title, 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.QUESTION_MESSAGE,
                                                  ChoordData.getInstance().getPreferences().getIcon());

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
		ChoordData choord_data = ChoordData.getInstance();

		//Generates a space-delimited set of string names from chord id
		ArrayList<String> string_names= choord_data.getTuning(id).getAllNoteNames();
		
		return String.join(" ", string_names);
	}
	
	public void refresh()
	{	
		ChoordData choord_data = ChoordData.getInstance();

		mRefreshing = true; //Combobox y u suk?
		
		//This list probably won't be that long.
		//Be lazy, just wipe it out and rebuild it.
		mComboTunings.removeAllItems();
		for (int i=0;i < choord_data.getNumTunings(); ++i)
		{
			mComboTunings.addItem(choord_data.getTuning(i).getName());
		}
		
		//Set tuning to current in data.
		int id = choord_data.getCurrentTuning();
		if (id > -1)
		{
			mComboTunings.setSelectedIndex(id);
			mTextTuning.setText(choord_data.getTuning(id).getName());
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
		ChoordData choord_data = ChoordData.getInstance();

    	String tuning_name = mTextTuning.getText();
    	String string_names = mTextStrings.getText();

    	if (tuning_name.length() ==0)
    	{
    		JOptionPane.showMessageDialog(null, "Tuning name is required.");
    		return false;
    	}
    	
       ToneChord chord = new ToneChord();
        
        try
        {
        	chord = new ToneChord(string_names);
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
        	choord_data.addTuning(chord);
        	choord_data.setCurrentTuning(0);
        }
        else
        {
    		if (tuning_name.compareTo( choord_data.getTuning(tuning_id).getName() ) != 0 
    				|| string_names.compareTo(makeStringsText(tuning_id)) != 0)
    		{
    			if(confirm("Confirm", "Update " +  choord_data.getTuning(tuning_id).getName() + "?"))
    			{
    				choord_data.updateTuning(tuning_id,  chord);
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

