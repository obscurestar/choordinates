package com.obscurestar.choordinates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField mTextNeck;
	private JTextField mTextPanes;
	private JCheckBox mChckbxLefty;

	/**
	 * Create the dialog.
	 */
	public PreferencesDialog() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		//setBounds(100, 100, 187, 261);
		int[] bounds = ChoordData.getInstance().getPreferences().getPrefRect();
		setBounds( bounds[0], bounds[1], bounds[2], bounds[3] );

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChoordData.getInstance().getPreferences().setPrefRect(getBounds());
			}
		});

		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Lefty");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			mChckbxLefty = new JCheckBox("");
			mChckbxLefty.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_mChckbxLefty = new GridBagConstraints();
			gbc_mChckbxLefty.anchor = GridBagConstraints.WEST;
			gbc_mChckbxLefty.insets = new Insets(0, 0, 5, 0);
			gbc_mChckbxLefty.gridx = 2;
			gbc_mChckbxLefty.gridy = 0;
			contentPanel.add(mChckbxLefty, gbc_mChckbxLefty);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Neck Length");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 2;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			mTextNeck = new JTextField();
			mTextNeck.setText(String.valueOf(ChoordData.getInstance().getPreferences().getNeckLength()));
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 2;
			gbc_textField.gridy = 2;
			contentPanel.add(mTextNeck, gbc_textField);
			mTextNeck.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Chord Frames");
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewLabel_2.gridx = 0;
			gbc_lblNewLabel_2.gridy = 4;
			contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		}
		{
			mTextPanes = new JTextField();
			mTextPanes.setText(String.valueOf(ChoordData.getInstance().getPreferences().getPanelLength()));
			GridBagConstraints gbc_textField_1 = new GridBagConstraints();
			gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_1.gridx = 2;
			gbc_textField_1.gridy = 4;
			contentPanel.add(mTextPanes, gbc_textField_1);
			mTextPanes.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closeWindow(true);
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ChoordData.read();
						closeWindow(false);
					}
				});
			}
		}
		setVisible(true);
	}

	private void closeWindow(boolean save) {
		if (save) {
			ChoordData choord_data = ChoordData.getInstance();
			Preferences prefs = choord_data.getPreferences();

			boolean changed = false;
			boolean l_orig = choord_data.getPreferences().getLefty();
			boolean lefty = mChckbxLefty.isSelected();

			if (l_orig != lefty)
				changed = true;

			prefs.setLefty(lefty);

			try {
				int orig = prefs.getNeckLength();
				int got = (int) Integer.valueOf(mTextNeck.getText());

				if (orig != got)
					changed = true;
				prefs.setNeckLength(got);

				orig = prefs.getPanelLength();
				got = (int) Integer.valueOf(mTextPanes.getText());

				if (orig != got)
					changed = true;
				prefs.setPanelLength(got);
			} catch (NumberFormatException err) {
				// Silently fail
			}
			if (changed && !ChoordData.CLOSING) {
				Choordinates.alert("Preferences Changed", "Restart required to apply changes.");
			}

			choord_data.write();
		}
		setVisible(false);
	}
}
