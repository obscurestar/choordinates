package choordinates;

import java.awt.EventQueue;

import java.awt.Dimension; 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Choordinates extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean mRefreshing;
	
	private TuningDialog mTuningDialog;
	private ChordDialog mChordDialog;
	
	private JPanel contentPane;
	private JComboBox<String> comboRootNote;
	private JComboBox<String> mComboTuning;
	private JTextField textNotesNotes;
	
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

	private void refreshAll()
	{
		//WARNING this does a read from the file.
		ChoordData.read();
		
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
	
	public void refresh()
	{
		ChoordData choord_data = ChoordData.getInstance();
		int id=choord_data.getCurrentTuning();

		mRefreshing = true; //Combobox y u suk?
		
		//This list probably won't be that long.
		//Be lazy, just wipe it out and rebuild it.
		mComboTuning.removeAllItems();
		for (int i=0;i < choord_data.getNumTunings(); ++i)
		{
			mComboTuning.addItem(choord_data.getTuning(i).getName());
		}

		if (id > -1)
		{
			mComboTuning.setSelectedIndex(id);
		}
	
		mRefreshing = false;
	}

	/**
	 * Create the frame.
	 */
	public Choordinates() {
		/*DANGER!  Do not get an instance of chorddata here.
		 * The lambdas will make you cry.
		 */
		ChoordData.read();   //Initialize data structures from JSON file.
		
		setTitle("Choordinates");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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

		setBounds(100, 100, 499, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.5, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 2.0, 2.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Tuning");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
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

        		refresh();
        	}
        });
		contentPane.add(mComboTuning, gbc_mComboTuning);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 4;
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		JPanel panelChordSelect = new JPanel();
		tabbedPane.addTab("Chord", null, panelChordSelect, null);
		GridBagLayout gbl_panelChordSelect = new GridBagLayout();
		gbl_panelChordSelect.columnWidths = new int[]{0, 0, 0};
		gbl_panelChordSelect.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelChordSelect.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelChordSelect.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		//panelChordSelect.setPreferredSize(new Dimension(100, 100));
		panelChordSelect.setLayout(gbl_panelChordSelect);
		
		JLabel lblNewLabel_4 = new JLabel("Root Note");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 0;
		panelChordSelect.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		JComboBox<String> comboRootNote = new JComboBox<String>();
		GridBagConstraints gbc_comboRootNote = new GridBagConstraints();
		gbc_comboRootNote.insets = new Insets(0, 0, 5, 0);
		gbc_comboRootNote.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboRootNote.gridx = 1;
		gbc_comboRootNote.gridy = 0;
		//comboRootNote.setPreferredSize(new Dimension(10,20));
		panelChordSelect.add(comboRootNote, gbc_comboRootNote);
		
		JLabel lblNewLabel_5 = new JLabel("Chord");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.gridwidth = 2;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 1;
		panelChordSelect.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		JList listChordChord = new JList();
		GridBagConstraints gbc_listChordChord = new GridBagConstraints();
		gbc_listChordChord.gridwidth = 2;
		gbc_listChordChord.fill = GridBagConstraints.BOTH;
		gbc_listChordChord.gridx = 0;
		gbc_listChordChord.gridy = 2;
		panelChordSelect.add(listChordChord, gbc_listChordChord);
		
		JPanel panelNotesSelect = new JPanel();
		tabbedPane.addTab("Notes", null, panelNotesSelect, null);
		
		JLabel lblNotesNotes = new JLabel("Notes");
		panelNotesSelect.add(lblNotesNotes);
		
		textNotesNotes = new JTextField();
		panelNotesSelect.add(textNotesNotes);
		textNotesNotes.setColumns(10);
		
		FretPanel panelFretSelect = new FretPanel();
		panelFretSelect.setOrientation(false);
		panelFretSelect.configureFretboard(7, 6);
		tabbedPane.addTab("Frets", null, panelFretSelect, null);

		JLabel lblNewLabel_2 = new JLabel("Matches");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.gridwidth = 2;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 0;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		JList listMatches = new JList();
		GridBagConstraints gbc_listMatches = new GridBagConstraints();
		gbc_listMatches.gridwidth = 2;
		gbc_listMatches.gridheight = 2;
		gbc_listMatches.insets = new Insets(0, 0, 5, 0);
		gbc_listMatches.fill = GridBagConstraints.BOTH;
		gbc_listMatches.gridx = 2;
		gbc_listMatches.gridy = 1;
		contentPane.add(listMatches, gbc_listMatches);
		
		JLabel lblNewLabel_3 = new JLabel("Favorite Shapes");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.gridwidth = 2;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 3;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
				
		JScrollPane scrollFavorites = new JScrollPane();
		GridBagConstraints gbc_scrollFavorites = new GridBagConstraints();
		gbc_scrollFavorites.gridwidth = 2;
		gbc_scrollFavorites.insets = new Insets(0, 0, 5, 0);
		gbc_scrollFavorites.gridheight = 2;
		gbc_scrollFavorites.fill = GridBagConstraints.BOTH;
		gbc_scrollFavorites.gridx = 2;
		gbc_scrollFavorites.gridy = 4;
		contentPane.add(scrollFavorites, gbc_scrollFavorites);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.gridwidth = 2;
		gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 5;
		contentPane.add(btnSearch, gbc_btnSearch);
		
		FretPanel panelNeck = new FretPanel();
		GridBagConstraints gbc_panelNeck = new GridBagConstraints();
		gbc_panelNeck.gridheight = 5;
		gbc_panelNeck.gridwidth = 4;
		gbc_panelNeck.fill = GridBagConstraints.BOTH;
		gbc_panelNeck.gridx = 0;
		gbc_panelNeck.gridy = 6;
		contentPane.add(panelNeck, gbc_panelNeck);
		refresh();
	}
}
