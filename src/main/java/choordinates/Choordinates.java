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

public class Choordinates extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textSelectChord;
	private JTextField textSelectRoot;
	private JTextField textSelectNotes;
	private JTextField textField;

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
		setBounds(100, 100, 584, 454);
		
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
		gbl_panelSelectChordTab.rowHeights = new int[]{0, 0, 0};
		gbl_panelSelectChordTab.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelSelectChordTab.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
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
		gbc_lblSelectChord.insets = new Insets(0, 0, 0, 5);
		gbc_lblSelectChord.gridx = 0;
		gbc_lblSelectChord.gridy = 1;
		panelSelectChordTab.add(lblSelectChord, gbc_lblSelectChord);
		
		textSelectRoot = new JTextField();
		GridBagConstraints gbc_textSelectRoot = new GridBagConstraints();
		gbc_textSelectRoot.fill = GridBagConstraints.BOTH;
		gbc_textSelectRoot.gridx = 1;
		gbc_textSelectRoot.gridy = 1;
		panelSelectChordTab.add(textSelectRoot, gbc_textSelectRoot);
		textSelectRoot.setColumns(10);
		
		//SELECT BY NOTES tab
		JPanel panelSelectNotes = new JPanel();
		tabbedSelectBy.addTab("Notes", null, panelSelectNotes, null);
		
		JLabel lblSelectNotes = new JLabel("Notes");
		panelSelectNotes.add(lblSelectNotes);
		
		textSelectNotes = new JTextField();
		panelSelectNotes.add(textSelectNotes);
		textSelectNotes.setColumns(16);
		
		//SELECT BY FRETS tab
		int num_strings = 6;
		int num_frets = 6;
		
		int cols = num_strings + 1; //Add a column for first fret label
		int rows = num_frets + 1;   //Add a row for first fret selection
		
		Dimension win_dimensions = new Dimension( 32*cols, 23*rows );
		
		JPanel panelSelectFret = new JPanel() {
			 @Override
	            protected void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                
	                int width = getWidth();
	                int height = getHeight();
	                
	                //Dimension cell_dimensions = new Dimension ( width/(cols+1), height/(rows+1) );
	                Dimension cell_dimensions = new Dimension(20, 20);
	                // Draw horizontal lines
	                for (int i = 1; i < rows; i++) {
	                    int y = i * cell_dimensions.height;
	                    g.drawLine(cell_dimensions.width, y, width - cell_dimensions.width/2, y);
	                }

	                // Draw vertical lines
	                for (int i = 0; i < cols; i++) {
	                    int x = i * cell_dimensions.width;
	                    g.drawLine(x, cell_dimensions.height, x, height - cell_dimensions.height/2);
	                }
	            }
	        };
			
		panelSelectFret.setMinimumSize(win_dimensions);
		tabbedSelectBy.addTab("Frets", null, panelSelectFret, null);
		GridBagLayout gbl_panelSelectFret = new GridBagLayout();
		
		gbl_panelSelectFret.columnWidths = new int[cols + 1];
		gbl_panelSelectFret.rowHeights = new int[rows];
		gbl_panelSelectFret.columnWeights = new double[cols+1];
		gbl_panelSelectFret.columnWeights[cols] = Double.MIN_VALUE;
		gbl_panelSelectFret.rowWeights = new double[rows+1];
		gbl_panelSelectFret.rowWeights[rows] = Double.MIN_VALUE;
		panelSelectFret.setLayout(gbl_panelSelectFret);
		
		JLabel lblFirstFret = new JLabel("First Fret");
		GridBagConstraints gbc_lblFirstFret = new GridBagConstraints();
		gbc_lblFirstFret.gridwidth = 2;
		gbc_lblFirstFret.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstFret.anchor = GridBagConstraints.EAST;
		gbc_lblFirstFret.gridx = 1;
		gbc_lblFirstFret.gridy = 0;
		panelSelectFret.add(lblFirstFret, gbc_lblFirstFret);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 0;
		panelSelectFret.add(textField, gbc_textField);
		textField.setColumns(3);
		
		JLabel lblFret = new JLabel("-");
		lblFret.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblFret = new GridBagConstraints();
		gbc_lblFret.insets = new Insets(0, 0, 5, 5);
		gbc_lblFret.gridx = 0;
		gbc_lblFret.gridy = 1;
		panelSelectFret.add(lblFret, gbc_lblFret);
		
		ButtonGroup[] groupByString = new ButtonGroup[num_strings];
		int x,y=0;
		for( x=1; x<=num_strings; x++)
		{
			groupByString[x-1] = new ButtonGroup();
			for (y=1; y<=num_frets; y++)
			{
				JRadioButton rdbtn = new JRadioButton("");
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.gridx = x;
				gbc.gridy = y;
				groupByString[x-1].add(rdbtn);
				panelSelectFret.add(rdbtn, gbc);
			}
		}
		
		//FAVORITE SHAPES panel
		JPanel panelFavShapes = new JPanel();
		panelFavShapes.setLayout(null);
		panelUpper.add(panelFavShapes);
		
		JLabel lblFavShapes = new JLabel("Favorite Shapes");
		lblFavShapes.setBounds(10, 0, 90, 16);
		panelFavShapes.add(lblFavShapes);
		
		JScrollPane scrollFavShapes = new JScrollPane();
		scrollFavShapes.setBounds(0, 16, 100, 4);
		panelFavShapes.add(scrollFavShapes);
		
		//LOWER PANEL FOR GUITAR NECK
		FretPanel panelNeck = new FretPanel();
		contentPane.add(panelNeck);

	}

}
