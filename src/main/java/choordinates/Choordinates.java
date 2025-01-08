package choordinates;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
//import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
//import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.EventQueue;
import java.awt.Dimension; 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

public class Choordinates extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean mRefreshing;
	
	private TuningDialog mTuningDialog;
	private ChordDialog mChordDialog;
	
	private JPanel contentPane;
	private JComboBox<String> mComboTuning;
	private JTextField mTextNotesNotes;
	private JList<String> mListChordChord;
	private FretPanel mPanelFretSelect;
	private FretPanel mPanelNeck;
	private JTextField mTextRootNote;
	private JList<String> mListMatches;
	private JTabbedPane mTabbedPane;
	
	private ArrayList<String> mMatchList = new ArrayList<String>();
	
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

	public void searchByChord()
	{
		ChoordData choord_data = ChoordData.getInstance();
		String root_name = mTextRootNote.getText();
		int chord_id = choord_data.getCurrentChord();
		
		ToneNote root_note = new ToneNote();
		
		if (!root_note.parse(root_name))
		{
			alert( "Search", "'" + root_name + "' is not a valid note name.");
			return;
		}
		
		ToneChord match_chord = new ToneChord( root_note, choord_data.getChord(chord_id) );
		flushMatchList();
		addMatch(match_chord.getName());
		refreshMatches();
		
		mPanelNeck.setRootAndChord(root_note, choord_data.getChord(chord_id));	
	}
	
	private void addMatches( ToneNote root_note, IntervalChord chord )
	{
		ChoordData choord_data = ChoordData.getInstance();

		
		boolean found_any=false;
		
		flushMatchList();
		
		for (int i=0;i<choord_data.getNumChords(); ++i)
		{
			if ( choord_data.getChord(i).similar( chord ) )
			{
				//The CTOR generates a nice name for us.
				ToneChord tone_chord = new ToneChord( root_note, choord_data.getChord(i) );

				addMatch( tone_chord.getName() );
				found_any = true;
			}
		}
		
		if (!found_any)
		{
			ArrayList<String> string_names= chord.getAllNoteNames();
			String name = root_note.getName() + String.join(" ", string_names) + " <UNMATCHED> ";
			addMatch( name );
		}

		refreshMatches();
	}
	
	private void searchByNotes()
	{
		ToneChord tone_chord = new ToneChord();
		
		try
		{
			tone_chord = ToneChord.parse( mTextNotesNotes.getText() );
        }
        catch (IllegalArgumentException exception)
        {
        	JOptionPane.showMessageDialog(null, exception.getMessage());
        	return;
        }
		
		if ( tone_chord.getNumNotes() < 1)
		{
			JOptionPane.showMessageDialog(null,  "Chord has no notes.");
			return;
		}
		
		IntervalChord interval_chord = new IntervalChord( tone_chord );
		
		addMatches( tone_chord.getNote(0), interval_chord );
		
		mPanelNeck.setRootAndChord(tone_chord.getNote(0), interval_chord);		
	}
	
	private void search()
	{
		int tab_no = mTabbedPane.getSelectedIndex();
			//TODO WARNING!  Just raw associating numbers of tabs.  Fix this!
		
		if (tab_no <= 0)
		{
			searchByChord();
		}
		else if (tab_no == 1)
		{
			searchByNotes();
		}
		else
		{
			
		}
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
	
	public void refresh()
	{
		ChoordData choord_data = ChoordData.getInstance();
		int tuning_id=choord_data.getCurrentTuning();

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

/*	public void updateFretDisplays()
	{
		ChoordData choords_data = ChoordData.getInstance();
		
		int num_frets = 24;
		int tuning_id = choords_data.getCurrentTuning();
		
		mPanelNeck.setNumFrets(num_frets);
	}*/
	
	private void alert(String title, String message)
	{
		JOptionPane.showMessageDialog(null,
				message, 
				title, 
				JOptionPane.INFORMATION_MESSAGE,
				ChoordData.getInstance().getPreferences().getIcon());
	}
	
	/**
	 * Create the frame.
	 */
	public Choordinates() {
		/*DANGER!  Do not get an instance of chorddata here.
		 * The lambdas will make you cry.
		 */
		ChoordData.read();   //Initialize data structures from JSON file.
		
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
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
	//HELP menu
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

//Create the content pane
		setBounds(100, 100, 499, 449);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.5, 0.0, 1.0, Double.MIN_VALUE};
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

        		mPanelNeck.updateTuning();
        		mPanelNeck.refresh();
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
            		ChoordData choord_data = ChoordData.getInstance();
                    if (!e.getValueIsAdjusting()) { // Check if selection is finalized
                    	choord_data.setCurrentChord( mListChordChord.getSelectedIndex());
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
		panelNotesSelect.add(mTextNotesNotes);
		mTextNotesNotes.setColumns(10);
		
//Select by FRETS pane
		mPanelFretSelect = new FretPanel();
		mPanelFretSelect.setOrientation(false);
		mPanelFretSelect.setNumFrets(7);
		mTabbedPane.addTab("Frets", null, mPanelFretSelect, null);
		

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
				
		JScrollPane scrollFavorites = new JScrollPane();
		GridBagConstraints gbc_scrollFavorites = new GridBagConstraints();
		gbc_scrollFavorites.gridwidth = 2;
		gbc_scrollFavorites.insets = new Insets(0, 0, 5, 0);
		gbc_scrollFavorites.gridheight = 2;
		gbc_scrollFavorites.fill = GridBagConstraints.BOTH;
		gbc_scrollFavorites.gridx = 2;
		gbc_scrollFavorites.gridy = 4;
		contentPane.add(scrollFavorites, gbc_scrollFavorites);

//Search button		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.gridwidth = 2;
		gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 5;
		contentPane.add(btnSearch, gbc_btnSearch);
		
		mPanelNeck = new FretPanel();
		GridBagConstraints gbc_mPanelNeck = new GridBagConstraints();
		gbc_mPanelNeck.gridheight = 5;
		gbc_mPanelNeck.gridwidth = 4;
		gbc_mPanelNeck.fill = GridBagConstraints.BOTH;
		gbc_mPanelNeck.gridx = 0;
		gbc_mPanelNeck.gridy = 6;
		contentPane.add(mPanelNeck, gbc_mPanelNeck);
		refresh();
	}
}
