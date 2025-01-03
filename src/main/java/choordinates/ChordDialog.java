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
	private JTextField mTextName;
	private JTextField mTextAliases;
	private JTextField mTextIntervals;

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
			gbl_panelChordEntry.columnWidths = new int[]{0, 0, 0};
			gbl_panelChordEntry.rowHeights = new int[]{0, 0, 0};
			gbl_panelChordEntry.columnWeights = new double[]{0.0, 1.0, 0.0};
			gbl_panelChordEntry.rowWeights = new double[]{0.0, 0.0, 0.0};
			panelChordEntry.setLayout(gbl_panelChordEntry);
			{
				JLabel lblName = new JLabel("Name");
				GridBagConstraints gbc_lblName = new GridBagConstraints();
				gbc_lblName.insets = new Insets(0, 0, 5, 5);
				gbc_lblName.anchor = GridBagConstraints.EAST;
				gbc_lblName.gridx = 0;
				gbc_lblName.gridy = 0;
				panelChordEntry.add(lblName, gbc_lblName);
			}
			{
				mTextName = new JTextField();
				mTextName.setToolTipText("Name of the chord eg: Major, minor, dominant, 7th");
				GridBagConstraints gbc_mTextName = new GridBagConstraints();
				gbc_mTextName.gridwidth = 2;
				gbc_mTextName.insets = new Insets(0, 0, 5, 5);
				gbc_mTextName.fill = GridBagConstraints.HORIZONTAL;
				gbc_mTextName.gridx = 1;
				gbc_mTextName.gridy = 0;
				panelChordEntry.add(mTextName, gbc_mTextName);
				mTextName.setColumns(10);
			}
			{
				JLabel lblAlias = new JLabel("Aliases");
				lblAlias.setToolTipText("Comma delimited list.  EG");
				GridBagConstraints gbc_lblAlias = new GridBagConstraints();
				gbc_lblAlias.anchor = GridBagConstraints.EAST;
				gbc_lblAlias.insets = new Insets(0, 0, 5, 5);
				gbc_lblAlias.gridx = 0;
				gbc_lblAlias.gridy = 1;
				panelChordEntry.add(lblAlias, gbc_lblAlias);
			}
			{
				mTextAliases = new JTextField();
				GridBagConstraints gbc_mTextAliases = new GridBagConstraints();
				gbc_mTextAliases.gridwidth = 2;
				gbc_mTextAliases.insets = new Insets(0, 0, 5, 5);
				gbc_mTextAliases.fill = GridBagConstraints.HORIZONTAL;
				gbc_mTextAliases.gridx = 1;
				gbc_mTextAliases.gridy = 1;
				panelChordEntry.add(mTextAliases, gbc_mTextAliases);
				mTextAliases.setColumns(10);
			}
			{
				JLabel lblInterval = new JLabel("Intervals");
				GridBagConstraints gbc_lblInterval = new GridBagConstraints();
				gbc_lblInterval.anchor = GridBagConstraints.EAST;
				gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
				gbc_lblInterval.gridx = 0;
				gbc_lblInterval.gridy = 2;
				panelChordEntry.add(lblInterval, gbc_lblInterval);
			}
			{
				mTextIntervals = new JTextField();
				GridBagConstraints gbc_mTextIntervals = new GridBagConstraints();
				gbc_mTextIntervals.gridwidth = 2;
				gbc_mTextIntervals.insets = new Insets(0, 0, 5, 5);
				gbc_mTextIntervals.fill = GridBagConstraints.HORIZONTAL;
				gbc_mTextIntervals.gridx = 1;
				gbc_mTextIntervals.gridy = 2;
				panelChordEntry.add(mTextIntervals, gbc_mTextIntervals);
				mTextIntervals.setColumns(10);
			}
			{
				JButton btnNew = new JButton("New");
				GridBagConstraints gbc_btnNew = new GridBagConstraints();
				gbc_btnNew.insets = new Insets(0, 0, 0, 5);
				gbc_btnNew.gridx = 0;
				gbc_btnNew.gridy = 3;
				panelChordEntry.add(btnNew, gbc_btnNew);
			}
			{
				JButton btnSave = new JButton("Save");
				GridBagConstraints gbc_btnSave = new GridBagConstraints();
				gbc_btnSave.insets = new Insets(0, 0, 0, 5);
				gbc_btnSave.gridx = 1;
				gbc_btnSave.gridy = 3;
				panelChordEntry.add(btnSave, gbc_btnSave);
			}
			{
				JButton btnDelete = new JButton("Delete");
				GridBagConstraints gbc_btnDelete = new GridBagConstraints();
				gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
				gbc_btnDelete.gridx = 2;
				gbc_btnDelete.gridy = 3;
				panelChordEntry.add(btnDelete, gbc_btnDelete);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOk = new JButton("OK");
				btnOk.setActionCommand("OK");
				buttonPane.add(btnOk);
				getRootPane().setDefaultButton(btnOk);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
		}
		setVisible(true);
	}

}
