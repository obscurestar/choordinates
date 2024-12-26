package choordinates;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class TuningWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textTuning;
	private JLabel lblNewLabel_1;
	private JTextField textStrings;
	private JLabel lblNewLabel_2;

	/**
	 * Create the frame.
	 */
	public TuningWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 200);
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tuning Name");
		lblNewLabel.setBounds(12, 94, 100, 16);
		contentPane.add(lblNewLabel);
		
		textTuning = new JTextField();
		textTuning.setBounds(106, 89, 200, 26);
		contentPane.add(textTuning);
		textTuning.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Strings");
		lblNewLabel_1.setBounds(51, 122, 61, 16);
		contentPane.add(lblNewLabel_1);
		
		textStrings = new JTextField();
		textStrings.setBounds(106, 117, 200, 26);
		contentPane.add(textStrings);
		textStrings.setColumns(10);
		
		JButton btnAdd = new JButton("Add Tuning");
		btnAdd.setBounds(6, 150, 117, 29);
		contentPane.add(btnAdd);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(116, 150, 80, 29);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(226, 150, 80, 29);
		contentPane.add(btnCancel);
		
		lblNewLabel_2 = new JLabel("Tunings");
		lblNewLabel_2.setBounds(135, 6, 61, 16);
		contentPane.add(lblNewLabel_2);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(6, 30, 308, 27);
		contentPane.add(comboBox);
		setVisible(true);
	}
}
