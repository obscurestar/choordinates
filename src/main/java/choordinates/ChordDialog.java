package choordinates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import net.miginfocom.swing.MigLayout;

public class ChordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textChordName;
	private JTextField textChordAliases;

	/**
	 * Create the dialog.
	 */
	public ChordDialog() {
		setTitle("Chord Dictionary");
		setBounds(100, 100, 455, 219);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel panelChordDict = new JPanel();
			contentPanel.add(panelChordDict);
			GridBagLayout gbl_panelChordDict = new GridBagLayout();
			gbl_panelChordDict.columnWidths = new int[]{150	, 45};
			gbl_panelChordDict.rowHeights = new int[]{16, 0};
			gbl_panelChordDict.columnWeights = new double[]{1.0, 0.0};
			gbl_panelChordDict.rowWeights = new double[]{0.0, 1.0};
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
				JList list = new JList();
				GridBagConstraints gbc_list = new GridBagConstraints();
				gbc_list.gridwidth = 2;
				gbc_list.insets = new Insets(0, 0, 0, 5);
				gbc_list.fill = GridBagConstraints.BOTH;
				gbc_list.gridx = 0;
				gbc_list.gridy = 1;
				panelChordDict.add(list, gbc_list);
			}
		}
		{
			JPanel panelChordEntry = new JPanel();
			contentPanel.add(panelChordEntry);
			GridBagLayout gbl_panelChordEntry = new GridBagLayout();
			gbl_panelChordEntry.columnWidths = new int[]{0, 0, 0, 0};
			gbl_panelChordEntry.rowHeights = new int[]{0, 0, 0, 0};
			gbl_panelChordEntry.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
			gbl_panelChordEntry.rowWeights = new double[]{0.0, 0.0, 0.0};
			panelChordEntry.setLayout(gbl_panelChordEntry);
			{
				JLabel lblChordName = new JLabel("Name");
				GridBagConstraints gbc_lblChordName = new GridBagConstraints();
				gbc_lblChordName.insets = new Insets(0, 0, 5, 5);
				gbc_lblChordName.anchor = GridBagConstraints.EAST;
				gbc_lblChordName.gridx = 0;
				gbc_lblChordName.gridy = 0;
				panelChordEntry.add(lblChordName, gbc_lblChordName);
			}
			{
				textChordName = new JTextField();
				textChordName.setToolTipText("Name of the chord eg: Major, minor, dominant, 7th");
				GridBagConstraints gbc_textChordName = new GridBagConstraints();
				gbc_textChordName.gridwidth = 2;
				gbc_textChordName.insets = new Insets(0, 0, 5, 0);
				gbc_textChordName.fill = GridBagConstraints.HORIZONTAL;
				gbc_textChordName.gridx = 1;
				gbc_textChordName.gridy = 0;
				panelChordEntry.add(textChordName, gbc_textChordName);
				textChordName.setColumns(10);
			}
			{
				JLabel lblChordAlias = new JLabel("Aliases");
				lblChordAlias.setToolTipText("Comma delimited list.  EG");
				GridBagConstraints gbc_lblChordAlias = new GridBagConstraints();
				gbc_lblChordAlias.anchor = GridBagConstraints.EAST;
				gbc_lblChordAlias.insets = new Insets(0, 0, 5, 5);
				gbc_lblChordAlias.gridx = 0;
				gbc_lblChordAlias.gridy = 1;
				panelChordEntry.add(lblChordAlias, gbc_lblChordAlias);
			}
			{
				textChordAliases = new JTextField();
				GridBagConstraints gbc_textChordAliases = new GridBagConstraints();
				gbc_textChordAliases.gridwidth = 2;
				gbc_textChordAliases.insets = new Insets(0, 0, 5, 0);
				gbc_textChordAliases.fill = GridBagConstraints.HORIZONTAL;
				gbc_textChordAliases.gridx = 1;
				gbc_textChordAliases.gridy = 1;
				panelChordEntry.add(textChordAliases, gbc_textChordAliases);
				textChordAliases.setColumns(10);
			}
			{
				JButton btnAddChord = new JButton("New");
				GridBagConstraints gbc_btnAddChord = new GridBagConstraints();
				gbc_btnAddChord.insets = new Insets(0, 0, 0, 5);
				gbc_btnAddChord.gridx = 0;
				gbc_btnAddChord.gridy = 3;
				panelChordEntry.add(btnAddChord, gbc_btnAddChord);
			}
			{
				JButton btnChordSave = new JButton("Save");
				GridBagConstraints gbc_btnChordSave = new GridBagConstraints();
				gbc_btnChordSave.insets = new Insets(0, 0, 0, 5);
				gbc_btnChordSave.gridx = 1;
				gbc_btnChordSave.gridy = 3;
				panelChordEntry.add(btnChordSave, gbc_btnChordSave);
			}
			{
				JButton btnChordDelete = new JButton("Delete");
				GridBagConstraints gbc_btnChordDelete = new GridBagConstraints();
				gbc_btnChordDelete.insets = new Insets(0, 0, 0, 5);
				gbc_btnChordDelete.gridx = 2;
				gbc_btnChordDelete.gridy = 3;
				panelChordEntry.add(btnChordDelete, gbc_btnChordDelete);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setVisible(true);
	}

}
