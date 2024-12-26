package choordinates;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;

public class Choordinates extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textSelectChord;
	private JTextField textSelectRoot;
	private JTextField textSelectNotes;
	private JTextField textFirstFret;

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
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//   Window Menu
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmTuning = new JMenuItem("Tuning");
		mnWindow.add(mntmTuning);
		
		mntmTuning.addActionListener(e -> {
            TuningWindow tuningWindow = new TuningWindow();
        });		
		
		JMenuItem mntmChord = new JMenuItem("Chord");
		mnWindow.add(mntmChord);
		
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
		panelUpper.add(panelSelect);
		panelSelect.setLayout(new BoxLayout(panelSelect, BoxLayout.Y_AXIS));
		
		JLabel lblSelectBy = new JLabel("Select By");
		panelSelect.add(lblSelectBy);
		
		//SELECT BY tabbed panel
		JTabbedPane tabbedSelectBy = new JTabbedPane(JTabbedPane.TOP);
		panelSelect.add(tabbedSelectBy);
		
		//SELECT BY CHORD NAME tab
		JPanel panelSelectChordTab = new JPanel();
		tabbedSelectBy.addTab("Chord", null, panelSelectChordTab, null);
		panelSelectChordTab.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblSelectRoot = new JLabel("Root Note");
		panelSelectChordTab.add(lblSelectRoot);
		
		textSelectChord = new JTextField();
		panelSelectChordTab.add(textSelectChord);
		textSelectChord.setColumns(10);
		
		JLabel lblSelectChord = new JLabel("Chord");
		panelSelectChordTab.add(lblSelectChord);
		
		textSelectRoot = new JTextField();
		panelSelectChordTab.add(textSelectRoot);
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
		JPanel panelSelectFret = new JPanel();
		tabbedSelectBy.addTab("Frets", null, panelSelectFret, null);
		panelSelectFret.setLayout(new BoxLayout(panelSelectFret, BoxLayout.Y_AXIS));
		
		JLabel lblFirstFret = new JLabel("First Fret");
		panelSelectFret.add(lblFirstFret);
		
		textFirstFret = new JTextField();
		panelSelectFret.add(textFirstFret);
		textFirstFret.setColumns(5);
		
		JPanel panelChordFrets = new JPanel();
		panelSelectFret.add(panelChordFrets);
		
		JRadioButton rdbtnSelFret00 = new JRadioButton("|");
		panelChordFrets.add(rdbtnSelFret00);
		
		//FAVORITE SHAPES panel
		JPanel panelFavShapes = new JPanel();
		panelUpper.add(panelFavShapes);
		panelFavShapes.setLayout(new BoxLayout(panelFavShapes, BoxLayout.Y_AXIS));
		
		JLabel lblFavShapes = new JLabel("Favorite Shapes");
		panelFavShapes.add(lblFavShapes);
		
		JScrollPane scrollFavShapes = new JScrollPane();
		panelFavShapes.add(scrollFavShapes);
		
		//LOWER PANEL FOR GUITAR NECK
		JPanel panelLower = new JPanel();
		contentPane.add(panelLower);
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.Y_AXIS));
		
		JLabel lblNewLabel = new JLabel("<PLACEHOLDER>");
		panelLower.add(lblNewLabel);
		
		JPanel panelNeck = new JPanel();
		panelLower.add(panelNeck);

	}

}
