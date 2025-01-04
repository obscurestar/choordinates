package choordinates;

import java.awt.EventQueue;

import java.awt.Dimension; 
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import choordinates.FretPanel;
import javax.swing.JList;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Choordinates extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textSelectChord;
	private JTextField textSelectRoot;
	private JTextField textSelectNotes;
	private JTextField textField;
	private JButton mBtnSearchNotes;

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

	/**
	 * Create the frame.
	 */
	public Choordinates() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 473, 454);
		
		Data test = new Data();
		test.write();
		test.read();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//   Window Menu
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmTuning = new JMenuItem("Tunings");
		mnWindow.add(mntmTuning);
		
		mntmTuning.addActionListener(e -> {
            TuningDialog tuningDialog = new TuningDialog();
        });		
		
		JMenuItem mntmChord = new JMenuItem("Chords");
		mnWindow.add(mntmChord);
		
		mntmChord.addActionListener(e -> {
            ChordDialog chordDialog = new ChordDialog();
        });	
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		//HELP menu
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		//MAIN pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		//UPPER panel for label and the select by tabbed pane.
		JPanel panelUpper = new JPanel();
		contentPane.add(panelUpper);
		panelUpper.setLayout(new BoxLayout(panelUpper, BoxLayout.X_AXIS));
		
		//SELECT panel.
		JPanel panelSelect = new JPanel();
		panelSelect.setMinimumSize(new Dimension(100,300));
		panelSelect.setMaximumSize(new Dimension(100,300));
		panelSelect.setBounds(getBounds());
		panelUpper.add(panelSelect);
		panelSelect.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSelectBy = new JLabel("Select By");
		panelSelect.add(lblSelectBy);
		
		//SELECT BY tabbed panel
		JTabbedPane tabbedSelectBy = new JTabbedPane(JTabbedPane.TOP);
		panelSelect.add(tabbedSelectBy);
		
		//SELECT BY CHORD NAME tab
		JPanel panelSelectChordTab = new JPanel();
		tabbedSelectBy.addTab("Chord", null, panelSelectChordTab, null);
		GridBagLayout gbl_panelSelectChordTab = new GridBagLayout();
		gbl_panelSelectChordTab.columnWidths = new int[]{0,	0, 0};
		gbl_panelSelectChordTab.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelSelectChordTab.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panelSelectChordTab.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelSelectChordTab.setLayout(gbl_panelSelectChordTab);
		
		JLabel lblSelectRoot = new JLabel("Root Note");
		GridBagConstraints gbc_lblSelectRoot = new GridBagConstraints();
		gbc_lblSelectRoot.fill = GridBagConstraints.BOTH;
		gbc_lblSelectRoot.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectRoot.gridx = 0;
		gbc_lblSelectRoot.gridy = 0;
		panelSelectChordTab.add(lblSelectRoot, gbc_lblSelectRoot);
		
		textSelectChord = new JTextField();
		GridBagConstraints gbc_textSelectChord = new GridBagConstraints();
		gbc_textSelectChord.fill = GridBagConstraints.BOTH;
		gbc_textSelectChord.insets = new Insets(0, 0, 5, 0);
		gbc_textSelectChord.gridx = 1;
		gbc_textSelectChord.gridy = 0;
		panelSelectChordTab.add(textSelectChord, gbc_textSelectChord);
		textSelectChord.setColumns(10);
		
		JLabel lblSelectChord = new JLabel("Chord");
		GridBagConstraints gbc_lblSelectChord = new GridBagConstraints();
		gbc_lblSelectChord.fill = GridBagConstraints.BOTH;
		gbc_lblSelectChord.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectChord.gridx = 0;
		gbc_lblSelectChord.gridy = 1;
		panelSelectChordTab.add(lblSelectChord, gbc_lblSelectChord);
		
		textSelectRoot = new JTextField();
		GridBagConstraints gbc_textSelectRoot = new GridBagConstraints();
		gbc_textSelectRoot.insets = new Insets(0, 0, 5, 0);
		gbc_textSelectRoot.fill = GridBagConstraints.BOTH;
		gbc_textSelectRoot.gridx = 1;
		gbc_textSelectRoot.gridy = 1;
		panelSelectChordTab.add(textSelectRoot, gbc_textSelectRoot);
		textSelectRoot.setColumns(10);
		
		JButton btnSearchChord = new JButton("Search");
		GridBagConstraints gbc_btnSearchChord = new GridBagConstraints();
		gbc_btnSearchChord.gridwidth = 2;
		gbc_btnSearchChord.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearchChord.gridx = 0;
		gbc_btnSearchChord.gridy = 2;
		panelSelectChordTab.add(btnSearchChord, gbc_btnSearchChord);
		
		JList listChordSelect = new JList();
		GridBagConstraints gbc_listChordSelect = new GridBagConstraints();
		gbc_listChordSelect.gridwidth = 2;
		gbc_listChordSelect.fill = GridBagConstraints.BOTH;
		gbc_listChordSelect.gridx = 0;
		gbc_listChordSelect.gridy = 3;
		panelSelectChordTab.add(listChordSelect, gbc_listChordSelect);
		
		//SELECT BY NOTES tab
		JPanel panelSelectNotes = new JPanel();
		tabbedSelectBy.addTab("Notes", null, panelSelectNotes, null);
		panelSelectNotes.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("37px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("202px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("29px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblSelectNotes = new JLabel("Notes");
		panelSelectNotes.add(lblSelectNotes, "2, 2, left, center");
		
		textSelectNotes = new JTextField();
		panelSelectNotes.add(textSelectNotes, "4, 2, left, center");
		textSelectNotes.setColumns(16);
		
		mBtnSearchNotes = new JButton("Search");
		mBtnSearchNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panelSelectNotes.add(mBtnSearchNotes, "4, 6, left, top");
		
		//SELECT BY FRETS tab
		int num_strings = 6;
		int num_frets = 6;
		
		int cols = num_strings + 1; //Add a column for first fret label
		int rows = num_frets + 1;   //Add a row for first fret selection
		
		Dimension win_dimensions = new Dimension( 32*cols, 23*rows );
		
		JPanel panelSelectFret = new JPanel();
			
		panelSelectFret.setMinimumSize(win_dimensions);
		tabbedSelectBy.addTab("Frets", null, panelSelectFret, null);
		GridBagLayout gbl_panelSelectFret = new GridBagLayout();
		gbl_panelSelectFret.columnWidths = new int[]{75, 75, 46, 0};
		gbl_panelSelectFret.rowHeights = new int[]{26, 0, 0};
		gbl_panelSelectFret.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelSelectFret.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panelSelectFret.setLayout(gbl_panelSelectFret);
		
		JLabel lblFirstFret = new JLabel("First Fret");
		GridBagConstraints gbc_lblFirstFret = new GridBagConstraints();
		gbc_lblFirstFret.anchor = GridBagConstraints.WEST;
		gbc_lblFirstFret.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstFret.gridx = 0;
		gbc_lblFirstFret.gridy = 0;
		panelSelectFret.add(lblFirstFret, gbc_lblFirstFret);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panelSelectFret.add(textField, gbc_textField);
		textField.setColumns(3);
		
		JButton btnSearchFret = new JButton("Search");
		GridBagConstraints gbc_btnSearchFret = new GridBagConstraints();
		gbc_btnSearchFret.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchFret.gridx = 2;
		gbc_btnSearchFret.gridy = 0;
		panelSelectFret.add(btnSearchFret, gbc_btnSearchFret);
		
		FretPanel panelFrets = new FretPanel();
		panelFrets.setOrientation(false);
		panelFrets.configureFretboard(7, 6);
		GridBagConstraints gbc_panelFrets = new GridBagConstraints();
		gbc_panelFrets.gridwidth = 3;
		gbc_panelFrets.fill = GridBagConstraints.BOTH;
		gbc_panelFrets.gridx = 0;
		gbc_panelFrets.gridy = 1;
		panelSelectFret.add(panelFrets, gbc_panelFrets);
		
		
		//FAVORITE SHAPES panel
		JPanel panelRight = new JPanel();
		panelUpper.add(panelRight);
		panelRight.setLayout(null);
		
		JComboBox comboTuning = new JComboBox();
		comboTuning.setBounds(0, 0, 195, 27);
		comboTuning.setToolTipText("Select the Tuning");
		panelRight.add(comboTuning);
		
		JLabel lblFavShapes = new JLabel("Favorite Shapes");
		lblFavShapes.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavShapes.setBounds(10, 32, 185, 16);
		panelRight.add(lblFavShapes);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 53, 195, 204);
		panelRight.add(panel);
		
		//LOWER PANEL FOR GUITAR NECK
		FretPanel panelNeck = new FretPanel();
		contentPane.add(panelNeck);

	}
}
