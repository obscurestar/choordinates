package com.obscurestar.choordinates;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.Taskbar;

import com.obscurestar.choordinates.FretPanel.Select;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.EventQueue;
import java.awt.Dimension; 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.ArrayList;
import java.util.UUID;
import javax.swing.BoxLayout;

public class Choordinates extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean mRefreshing;
	
	private TuningDialog mTuningDialog;
	private ChordDialog mChordDialog;
	private PreferencesDialog mPreferencesDialog;
	
	private JPanel contentPane;
	private JComboBox<String> mComboTuning;
	private JTextField mTextNotesNotes;
	private JList<String> mListChordChord;
	private FretPanel mPanelFretSelect;
	private FretPanel mPanelNeck;
	private JTextField mTextRootNote;
	private JList<String> mListMatches;
	private JTabbedPane mTabbedPane;
	private FavPanel mPanelFav;
	
	private ArrayList<String> mMatchList = new ArrayList<String>();
	private JTextField mTextFretsNum;
	private UUID mCurrentFavorites = UUID.randomUUID(); //Odds are good this won't bite me.
    private FavHandler mFavHandler;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty(
            "com.apple.mrj.application.apple.menu.about.name", "Name");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Choordinates frame = new Choordinates();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void searchByChord(boolean alert)
	{
		ChoordData choord_data = ChoordData.getInstance();
		String root_name = mTextRootNote.getText();
		int chord_id = choord_data.getCurrentChord();
		
		ToneNote root_note;
		
		try
		{
			root_note = new ToneNote( root_name );
		}
        catch (IllegalArgumentException exception)
        {
        	if(alert)
        	{
        		alert(null, exception.getMessage());
        	}
        	return;
		}
		
		ToneChord match_chord = new ToneChord( root_note, choord_data.getChord(chord_id) );
		flushMatchList();
		addMatch(match_chord.getName());
		refreshMatches();
		
		mPanelNeck.setRootAndChord(root_note, choord_data.getChord(chord_id));	
		updateFavorites();
		refreshFretPanels();
	}
	
	private IntervalChord addMatches( ToneNote root_note, IntervalChord chord )
	{
		ChoordData choord_data = ChoordData.getInstance();
		boolean found_any=false;
		IntervalChord first_match=chord;
		
		flushMatchList();
		
		ToneChord perm_tone = new ToneChord( root_note, chord );
		perm_tone.reduceNotes();  //Get rid of duplicate notes.

		ToneNote perm_note = new ToneNote(root_note);
		IntervalChord perm_interval = new IntervalChord(perm_tone);
			
		ArrayList<String> perm_names = perm_tone.getAllNoteNames();
		int perm_count = perm_names.size();
		
		//Big fat O(n^2) permutations loop to find chord names.
		for ( int perm=0;perm < perm_count;++perm )
		{
			for (int dict_id=0; dict_id<choord_data.getNumChords(); ++dict_id )
			{
				if ( choord_data.getChord(dict_id).similar( perm_interval ) )
				{
					ToneChord tone_chord = new ToneChord( perm_note, choord_data.getChord(dict_id) );
					
					addMatch( tone_chord.getName() );
					
					if (!found_any)
					{
						first_match = new IntervalChord(tone_chord);
					}
					found_any = true;
				}
				
				//Rotate the letters in the chord name
				String perm_str = perm_names.get(0);
				perm_names.remove(0);
				perm_names.add(perm_str);
				
				perm_str = "";
				for (int i=0;i<perm_count;++i)
				{
					perm_str += perm_names.get(i) + " ";
				}
				perm_tone = new ToneChord( perm_str );
				perm_note = perm_tone.getNote(0);
				perm_interval = new IntervalChord( perm_tone );
			}
		}
		
		if (!found_any)
		{
			ArrayList<String> string_names= chord.getAllNoteNames();
			String name = root_note.getName() + String.join(" ", string_names) + " <UNMATCHED> ";
			addMatch( name );
		}

		refreshMatches();
		return first_match;
	}
	
	private void searchByNotes(boolean alert)
	{
		ToneChord tone_chord;
		
		try
		{
			tone_chord = new ToneChord( mTextNotesNotes.getText() );
        }
        catch (IllegalArgumentException exception)
        {
        	if (alert)
        	{
        		alert(null, exception.getMessage());
        	}
        	return;
        }
				
		IntervalChord interval_chord = new IntervalChord( tone_chord );
		interval_chord.reduceNotes();  //Get rid of duplicate notes.

		interval_chord = addMatches( tone_chord.getNote(0), interval_chord );
		
		mPanelNeck.setRootAndChord(tone_chord.getNote(0), interval_chord);
		updateFavorites();
	}
	
	private void searchByFrets(boolean alert)
	{
		/*TODO really should override insertString and replace on the text box
		 * And build in a validator but let's do the easy thing for V 1.
		 */
		String fret_num_str = mTextFretsNum.getText();
		
		int max_allowed = 18; 
		/*TODO max_allowed should be derived from number of frets in fret
		 * selection panel and number of frets on fretboard display.
		 * fix this when adding preferences.
		 */
		int fret_num = 0;
		
		if (!fret_num_str.matches(""))
		{
			try {
			    fret_num = Integer.parseInt(fret_num_str);
			} catch (NumberFormatException e) {
				if( alert )
				{
					alert(null,  "Fret number must be between 0 and 18");
				}
			}
		}
		
		fret_num = Math.min( Math.max(0, fret_num), max_allowed );
		mPanelFretSelect.setFirstFret(fret_num);
		
		ToneChord tone_chord = mPanelFretSelect.getSelections(true);
		if (tone_chord == null)
		{
			refreshFretPanel( mPanelFretSelect );
			return;
		}
	
		IntervalChord interval_chord = new IntervalChord( tone_chord );
		interval_chord.reduceNotes();  //Get rid of duplicate notes.
		interval_chord = addMatches( tone_chord.getNote(0), interval_chord );

		mPanelNeck.setRootAndChord(tone_chord.getNote(0), interval_chord);		

		//refreshFretPanel(mPanelNeck);

		//refreshFretPanel(mPanelFretSelect);
		updateFavorites();
	}
	
	private void search(boolean alert)
	{
		int tab_no = mTabbedPane.getSelectedIndex();
			//TODO WARNING!  Just raw associating numbers of tabs.  Fix this!
		
		if (tab_no <= 0)
		{
			searchByChord(alert);
		}
		else if (tab_no == 1)
		{
			searchByNotes(alert);
		}
		else
		{
			searchByFrets(alert);
		}
	}
	
	private void updateFavorites()
	{
		ChoordData choord_data = ChoordData.getInstance();
		ToneChord tuning = choord_data.getCurrentTuning();
		IntervalChord intervals = mPanelNeck.getSearchChord();
		
		if ( ! intervals.hasNotes() )
		{
			return; //Shouldn't ever get here.
		}
		
		ToneNote root_note = mPanelNeck.getRootNote();
		
		UUID current_id = FavGroup.generateUUID( tuning, intervals);
		
		if ( current_id.compareTo( mCurrentFavorites ) == 0)
		{
			//return; //No update required.
		}
		
		mCurrentFavorites = current_id;
		mPanelFav.loadFavorites(mCurrentFavorites, root_note, intervals);
	}
	
	private void addFavorite()
	{	
		ChordShape chord_shape;
		ChoordData choord_data = ChoordData.getInstance();
		
		try
		{
			chord_shape = mPanelNeck.getSelectionShape();
		}
		catch (IllegalArgumentException exception)
		{
			alert("Add favorite", exception.getMessage());
			return;
		}
		
		IntervalChord interval_chord = mPanelNeck.getSearchChord();
				
		choord_data.addFavorite( chord_shape, interval_chord );
		
		setPanelFavSize();
		mPanelFav.addFavorite(mPanelNeck.getRootNote(),
								mPanelNeck.getSearchChord(),
								chord_shape);
		
		choord_data.write();
	}
	
	public void refreshFretPanel( FretPanel panel )
	{
		panel.updateTuning();
		panel.flushSelections();
		panel.refresh();
	}
	
	private void refreshFretPanels()
	{
		/*Update all the fret panes when we do stuff 
		 * like changing the tuning or doing a search.
		 */
		refreshFretPanel( mPanelNeck );
		refreshFretPanel( mPanelFretSelect );
	}
	
	private void refreshAll()
	{
		refresh();
		
		if (mTuningDialog != null)
		{
			mTuningDialog.refresh();
		}
		if (mChordDialog != null)
		{
			mChordDialog.refresh();
		}
	}
	
	public void refreshMatches()
	{
		mListMatches.removeAll();
		
        DefaultListModel<String> listModel = new DefaultListModel<>();
        
        for (int i=0;i<mMatchList.size();++i)
        {
        	listModel.addElement(mMatchList.get(i));
        }

        mListMatches.setModel(listModel);
        mListMatches.setSelectedIndex(0);
	}
	
	public void flushMatchList()
	{
		mMatchList.clear();
	}
	
	public void addMatch(String match_name)
	{
		mMatchList.add(match_name);
	}
	
	public void handleDelete( ChordShape chord )
	{
	 	mPanelFav.deleteFavorite( chord, mPanelNeck.getSearchChord() );
	}
	
	public void refresh()
	{
		ChoordData choord_data = ChoordData.getInstance();
		int tuning_id=choord_data.getCurrentTuningID();

		mRefreshing = true; //Combobox y u suk?
		
		/*
		 * TODO removing everything from the combo and list boxes
		 * is probably the wrong way to do things.
		 * It'll work for now but let's improve this.
		 */
		mComboTuning.removeAllItems();
		for (int i=0;i < choord_data.getNumTunings(); ++i)
		{
			mComboTuning.addItem(choord_data.getTuning(i).getName());
		}

		if (tuning_id > -1)
		{
			mComboTuning.setSelectedIndex(tuning_id);
		}
	
		mListChordChord.removeAll();
		
		//We could just return the arraylist from choord_data
		//and convert it to a list, but why do anything the
		//easy way?
        DefaultListModel<String> listModel = new DefaultListModel<>();
        int chord_id = choord_data.getCurrentChord();

        for (int i=0;i < choord_data.getNumChords(); ++i) {
            listModel.addElement(choord_data.getChord(i).getName());
        }

        // Set the list model to the JList
        mListChordChord.setModel(listModel);
        if (tuning_id > -1)
        {
        	mListChordChord.setSelectedIndex(chord_id);
        }
        
		//updateFretDisplays();
		mRefreshing = false;
	}
	
	private void setPanelFavSize()
	{
		Dimension list_size = mListMatches.getSize();
		
		//Favs is just a tiny bit taller than matches.
		list_size.height = (int) (list_size.getHeight() * 1.1);
		mPanelFav.adjustLayout(list_size); 		
	}
	
	public static void alert(String title, String message)
	{
		JOptionPane.showMessageDialog(null,
				message, 
				title, 
				JOptionPane.INFORMATION_MESSAGE,
				ChoordData.getInstance().getPreferences().getIcon());
	}
	
	
	public static boolean confirm(String title, String message)
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

        return false;
	}
	
	/**
	 * Create the frame.
	 */
	public Choordinates() {
		/*DANGER!  Do not get an instance of chorddata here.
		 * The lambdas will make you cry.
		 */
		ChoordData.read();   //Initialize data structures from JSON file.
		
        //this is new since JDK 9
		/*
        final Taskbar taskbar = Taskbar.getTaskbar();

        try {
            taskbar.setIconImage(ChoordData.getInstance().getPreferences().getIcon().getImage());
        } catch (final UnsupportedOperationException e) {
        } catch (final SecurityException e) {
        }*/

		setIconImage(ChoordData.getInstance().getPreferences().getIcon().getImage());

		//SPATTERS debug TODO put in real test harness.
		//new Tests();
		
    	mFavHandler = new FavHandler()
		{
			@Override
			public void favCallback( ChordShape chord, boolean right_click )
			{
				if (right_click)
				{
					handleDelete( chord );
				}
				else
				{
					mPanelNeck.setSelectionShape(chord);
				}
			}
		};
		
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	ChoordData.CLOSING = true;
            	ChoordData.getInstance().getPreferences().setMainRect( getBounds() );
            	ChoordData.getInstance().write();
            }
        });
        
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	setPanelFavSize();
            }
		});
		
		//Set up the window.
		setTitle("Choordinates");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//Add a Menubar.
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
	//   Window Menu
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmTuning = new JMenuItem("Tunings");
		mnWindow.add(mntmTuning);
		
		mntmTuning.addActionListener(e -> {
			if (mTuningDialog == null)
			{
				mTuningDialog = new TuningDialog();
			}
			else
			{
				refreshAll();
				mTuningDialog.toFront();
				mTuningDialog.setVisible(true);
			}
        });		
		
		JMenuItem mntmChord = new JMenuItem("Chords");
		mnWindow.add(mntmChord);
		
		mntmChord.addActionListener(e -> {
			if (mChordDialog == null)
			{
            	mChordDialog = new ChordDialog();
			}
			else
			{
				refreshAll();
				mChordDialog.toFront();
				mChordDialog.setVisible(true);
			}
        });	
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences");
		mnWindow.add(mntmPreferences);
		
		mntmPreferences.addActionListener(e -> {
			if (mPreferencesDialog == null)
			{
				mPreferencesDialog = new PreferencesDialog();
			}
			else
			{
				mPreferencesDialog.toFront();
				mPreferencesDialog.setVisible(true);
			}
        });	
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
	//HELP menu
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(e -> {
			Choordinates.alert("Choordinates", "1.0");
		});
		mnHelp.add(mntmAbout);
		
		JMenuItem mntmReport = new JMenuItem("Report issue");
		mntmReport.addActionListener(e -> {
			new BrowserWindow("https://github.com/obscurestar/choordinates/issues");
		});
		mnHelp.add(mntmReport);

		JMenuItem mntmHelpfile = new JMenuItem("Choordinates Help");
		mntmHelpfile.addActionListener(e -> {
			new BrowserWindow("https://raw.githubusercontent.com/obscurestar/choordinates/refs/heads/main/", 
					 System.getProperty("user.dir") + "choordinates.html");
		});
		mnHelp.add(mntmHelpfile);

//Create the content pane
		//setBounds(100, 100, 499, 449);
		int[] bounds = ChoordData.getInstance().getPreferences().getMainRect();
		setBounds( bounds[0], bounds[1], bounds[2], bounds[3] );
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.5, 0.0, 2.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 2.0, 2.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
//Tuning select		
		JLabel lblTuning = new JLabel("Tuning");
		GridBagConstraints gbc_lblTuning = new GridBagConstraints();
		gbc_lblTuning.anchor = GridBagConstraints.EAST;
		gbc_lblTuning.insets = new Insets(0, 0, 5, 5);
		gbc_lblTuning.gridx = 0;
		gbc_lblTuning.gridy = 0;
		contentPane.add(lblTuning, gbc_lblTuning);
		
		mComboTuning = new JComboBox<String>();
		GridBagConstraints gbc_mComboTuning = new GridBagConstraints();
		gbc_mComboTuning.insets = new Insets(0, 0, 5, 5);
		gbc_mComboTuning.fill = GridBagConstraints.HORIZONTAL;
		gbc_mComboTuning.gridx = 1;
		gbc_mComboTuning.gridy = 0;
		mComboTuning.setPreferredSize(new Dimension(10,20));
        mComboTuning.addActionListener(e -> {
        	if (!mRefreshing)
        	{
        		int id = mComboTuning.getSelectedIndex();
        		ChoordData.getInstance().setCurrentTuning(id);
        		ChoordData.getInstance().write();
        		searchByChord(false);
        		refreshFretPanels();
        		refresh();
        	}
        });
		contentPane.add(mComboTuning, gbc_mComboTuning);
//TABBED Pane		
		mTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 4;
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		contentPane.add(mTabbedPane, gbc_tabbedPane);

//Chord select Pane
		JPanel panelChordSelect = new JPanel();
		mTabbedPane.addTab("Chord", null, panelChordSelect, null);
		GridBagLayout gbl_panelChordSelect = new GridBagLayout();
		gbl_panelChordSelect.columnWidths = new int[]{0, 0, 0};
		gbl_panelChordSelect.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelChordSelect.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelChordSelect.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		//panelChordSelect.setPreferredSize(new Dimension(100, 100));
		panelChordSelect.setLayout(gbl_panelChordSelect);
		
		JLabel lblRootNote = new JLabel("Root Note");
		GridBagConstraints gbc_lblRootNote = new GridBagConstraints();
		gbc_lblRootNote.insets = new Insets(0, 0, 5, 5);
		gbc_lblRootNote.anchor = GridBagConstraints.EAST;
		gbc_lblRootNote.gridx = 0;
		gbc_lblRootNote.gridy = 0;
		panelChordSelect.add(lblRootNote, gbc_lblRootNote);
		
		mTextRootNote = new JTextField();
		GridBagConstraints gbc_mTextRootNote = new GridBagConstraints();
		gbc_mTextRootNote.insets = new Insets(0, 0, 5, 0);
		gbc_mTextRootNote.fill = GridBagConstraints.HORIZONTAL;
		gbc_mTextRootNote.gridx = 1;
		gbc_mTextRootNote.gridy = 0;
		mTextRootNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchByChord( true );
			}
		});
		mTextRootNote.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
          
            @Override
            public void focusLost(FocusEvent e) {
                searchByChord( false );
            }
        });
		panelChordSelect.add(mTextRootNote, gbc_mTextRootNote);
		
		JLabel lblChordChord = new JLabel("Chord");
		GridBagConstraints gbc_lblChordChord = new GridBagConstraints();
		gbc_lblChordChord.gridwidth = 2;
		gbc_lblChordChord.insets = new Insets(0, 0, 5, 5);
		gbc_lblChordChord.gridx = 0;
		gbc_lblChordChord.gridy = 1;
		panelChordSelect.add(lblChordChord, gbc_lblChordChord);
		
		mListChordChord = new JList<String>();
		GridBagConstraints gbc_mListChordChord = new GridBagConstraints();
		gbc_mListChordChord.gridwidth = 2;
		gbc_mListChordChord.fill = GridBagConstraints.BOTH;
		gbc_mListChordChord.gridx = 0;
		gbc_mListChordChord.gridy = 2;
		mListChordChord.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mListChordChord.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            	if (!mRefreshing)
            	{
                    if (!e.getValueIsAdjusting()) { // Check if selection is finalized
                		ChoordData choord_data = ChoordData.getInstance();

                    	choord_data.setCurrentChord( mListChordChord.getSelectedIndex());
                		choord_data.write();
                		searchByChord( false );
                    	refresh();
                    }
            	}
            }
        });
		panelChordSelect.add(mListChordChord, gbc_mListChordChord);
//Select by NOTES pane		
		JPanel panelNotesSelect = new JPanel();
		mTabbedPane.addTab("Notes", null, panelNotesSelect, null);
		
		mTextNotesNotes = new JTextField();
		mTextNotesNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchByNotes( true );
			}
		});
		mTextNotesNotes.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
          
            @Override
            public void focusLost(FocusEvent e) {
                searchByNotes( false );
            }
        });
		panelNotesSelect.add(mTextNotesNotes);
		mTextNotesNotes.setColumns(10);
		
//Select by FRETS pane

		JPanel panelFretsSelect = new JPanel();
		mTabbedPane.addTab("Frets", null, panelFretsSelect, null);
		panelFretsSelect.setLayout(new BoxLayout(panelFretsSelect, BoxLayout.X_AXIS));
		
		JPanel panelFretsNum = new JPanel();
		panelFretsSelect.add(panelFretsNum);
		GridBagLayout gbl_panelFretsNum = new GridBagLayout();
		gbl_panelFretsNum.columnWidths = new int[]{40, 60};
		gbl_panelFretsNum.rowHeights = new int[]{16, 0, 0, 0};
		gbl_panelFretsNum.columnWeights = new double[]{0.0, 3.0};
		gbl_panelFretsNum.rowWeights = new double[]{0.0, 0.0, 2.0, Double.MIN_VALUE};
		panelFretsNum.setLayout(gbl_panelFretsNum);
		
		JLabel lblFretsNum = new JLabel("Fret#");
		GridBagConstraints gbc_lblFretsNum = new GridBagConstraints();
		gbc_lblFretsNum.insets = new Insets(0, 0, 5, 5);
		gbc_lblFretsNum.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblFretsNum.gridx = 0;
		gbc_lblFretsNum.gridy = 0;
		panelFretsNum.add(lblFretsNum, gbc_lblFretsNum);
		
		mTextFretsNum = new JTextField();
		GridBagConstraints gbc_mTextFretsNum = new GridBagConstraints();
		gbc_mTextFretsNum.insets = new Insets(0, 0, 5, 5);
		gbc_mTextFretsNum.fill = GridBagConstraints.HORIZONTAL;
		gbc_mTextFretsNum.gridx = 0;
		gbc_mTextFretsNum.gridy = 1;
		mTextFretsNum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchByFrets( true );
			}
		});
		mTextFretsNum.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
          
            @Override
            public void focusLost(FocusEvent e) {
                searchByFrets( false );
            }
        });
		panelFretsNum.add(mTextFretsNum, gbc_mTextFretsNum);
		mTextFretsNum.setColumns(3);
		
		mPanelFretSelect = new FretPanel();
		mPanelFretSelect.selectMode(Select.ANY);
		mPanelFretSelect.setOrientation(false);
		mPanelFretSelect.setNumFrets(  ChoordData.getInstance().getPreferences().getPanelLength() );
		GridBagConstraints gbc_panelFretSelect = new GridBagConstraints();
		gbc_panelFretSelect.gridheight = 3;
		gbc_panelFretSelect.fill = GridBagConstraints.BOTH;
		gbc_panelFretSelect.gridx = 1;
		gbc_panelFretSelect.gridy = 0;
		panelFretsNum.add(mPanelFretSelect, gbc_panelFretSelect);
		

//Matches components.
		JLabel lblMatches = new JLabel("Matches");
		GridBagConstraints gbc_lblMatches = new GridBagConstraints();
		gbc_lblMatches.gridwidth = 2;
		gbc_lblMatches.insets = new Insets(0, 0, 5, 0);
		gbc_lblMatches.gridx = 2;
		gbc_lblMatches.gridy = 0;
		contentPane.add(lblMatches, gbc_lblMatches);
		
		mListMatches = new JList<String>();
		GridBagConstraints gbc_listMatches = new GridBagConstraints();
		gbc_listMatches.gridwidth = 2;
		gbc_listMatches.gridheight = 2;
		gbc_listMatches.insets = new Insets(0, 0, 5, 0);
		gbc_listMatches.fill = GridBagConstraints.BOTH;
		gbc_listMatches.gridx = 2;
		gbc_listMatches.gridy = 1;
		contentPane.add(mListMatches, gbc_listMatches);

//Favorite shape components.
		JLabel lblFavorites = new JLabel("Favorite Shapes");
		GridBagConstraints gbc_lblFavorites = new GridBagConstraints();
		gbc_lblFavorites.gridwidth = 2;
		gbc_lblFavorites.insets = new Insets(0, 0, 5, 0);
		gbc_lblFavorites.gridx = 2;
		gbc_lblFavorites.gridy = 3;
		contentPane.add(lblFavorites, gbc_lblFavorites);
				
		mPanelFav = new FavPanel();
		GridBagConstraints gbc_mFavPanel = new GridBagConstraints();
		gbc_mFavPanel.gridwidth = 2;
		gbc_mFavPanel.insets = new Insets(0, 0, 5, 0);
		gbc_mFavPanel.gridheight = 2;
		gbc_mFavPanel.fill = GridBagConstraints.BOTH;
		gbc_mFavPanel.gridx = 2;
		gbc_mFavPanel.gridy = 4;
		contentPane.add(mPanelFav, gbc_mFavPanel);

//Search button		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search( true );
			}
		});
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 5;
		contentPane.add(btnSearch, gbc_btnSearch);
		
//Fretboard display
		mPanelNeck = new FretPanel();
		GridBagConstraints gbc_mPanelNeck = new GridBagConstraints();
		gbc_mPanelNeck.gridheight = 5;
		gbc_mPanelNeck.gridwidth = 4;
		gbc_mPanelNeck.fill = GridBagConstraints.BOTH;
		gbc_mPanelNeck.gridx = 0;
		gbc_mPanelNeck.gridy = 6;
		mPanelNeck.selectMode(Select.VALID);
		
		JButton btnAddFavorite = new JButton("Add Favorite");
		btnAddFavorite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFavorite();
			}
		});
		GridBagConstraints gbc_mBtnAddFavorite = new GridBagConstraints();
		gbc_mBtnAddFavorite.insets = new Insets(0, 0, 5, 5);
		gbc_mBtnAddFavorite.gridx = 1;
		gbc_mBtnAddFavorite.gridy = 5;
		contentPane.add(btnAddFavorite, gbc_mBtnAddFavorite);
		contentPane.add(mPanelNeck, gbc_mPanelNeck);

		mPanelFav.setFavHandler( mFavHandler );

		refresh();
	}
}
