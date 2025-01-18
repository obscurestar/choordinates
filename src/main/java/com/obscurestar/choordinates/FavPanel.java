package com.obscurestar.choordinates;

import java.util.UUID;
import javax.swing.JPanel;
import java.util.HashMap;

import com.obscurestar.choordinates.FretPanel.Select;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FavPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	//ArrayList<FretPanel> mFavList = new ArrayList<FretPanel>();
	HashMap<UUID, FretPanel> mFavList = new HashMap<UUID, FretPanel>();
	FavHandler mCallback;
	JPanel mFavPanel;

	public void setFavHandler(FavHandler callback)
	{	
		mCallback = callback;

	}
	
	private void setFavCallback(FretPanel panel)
	{
		if (mCallback == null)
		{
			return;
		}
		panel.setCallback(mCallback);
	}
	
	private void setFavCallbacks()
	{
		for ( UUID key : mFavList.keySet() )
		{
			mFavList.get(key).setCallback(mCallback);
		}
	}
	
	public void adjustLayout(Dimension size) {
		double panel_aspect_ratio = size.getWidth() / size.getHeight();

		int cols = 1;

		if (mFavList.size() > 1) {
			cols = (int) Math.sqrt(mFavList.size() * panel_aspect_ratio);
		}

		mFavPanel.setLayout(new GridLayout(0, cols, 0, 0));
		mFavPanel.revalidate();
		mFavPanel.repaint();
	}

	public void flush() {
		if (mFavList.size() > 0) {
			for (FretPanel fav : mFavList.values()) {
				mFavPanel.remove(fav);
			}
		}
		mFavList.clear();
	}

	public void loadFavorites( UUID group_id, ToneNote roote_note, IntervalChord interval_chord )
	{
		flush();
		HashMap<UUID, ChordShape>  favs = ChoordData.getInstance().getFavoriteGroup( group_id ).getFavorites();
		for (ChordShape chord_shape : favs.values()) 
		{
			addFavorite( roote_note, interval_chord, chord_shape );
		}
	}
	
	public void addFavorite(ToneNote root_note, IntervalChord chord, ChordShape chord_shape) {
		ChoordData choord_data = ChoordData.getInstance();

		ToneChord tuning = choord_data.getCurrentTuning();

		int lowest = chord_shape.getLowestString();
		ToneNote lowest_note = new ToneNote(root_note, chord_shape.getFirstNote().getOctaveSemitone());

		int tuning_semitones = tuning.getNote(lowest).getOctaveSemitone();

		ToneNote fav_tone = new ToneNote(lowest_note.getOctaveSemitone() - tuning_semitones);

		int fav_semitones = AbstractNote.mod( fav_tone.getOctaveSemitone(), 12);

		FretPanel fret_panel = new FretPanel();
		fret_panel.setOrientation(false);
		fret_panel.setRootAndChord(root_note, chord);
		fret_panel.selectMode(Select.NONE);
		fret_panel.setSelectionShape(chord_shape);
		fret_panel.setNumFrets(   ChoordData.getInstance().getPreferences().getPanelLength() );
		fret_panel.setFirstFret(fav_semitones); //should happen after setNumFrets.
		fret_panel.markFrets();
		setFavCallback(fret_panel);

		mFavList.put(chord_shape.getUUID(),fret_panel);
		mFavPanel.add(fret_panel);

		adjustLayout(mFavPanel.getSize());
	}
	
	public void deleteFavorite( UUID fav_id, IntervalChord key )
	{
		//Handling dialog and save here because why do either if it's a bad selection.
		if (mFavList.containsKey( fav_id ) )
		{
			if( Choordinates.confirm( "Delete Favorite", "This operation cannot be undone.") )
			{
				ChoordData choord_data = ChoordData.getInstance();
				
				mFavPanel.remove ( mFavList.get(fav_id) );
				mFavList.remove( fav_id );
				adjustLayout(mFavPanel.getSize());
				choord_data.getFavoriteGroup( key ).deleteFavorite(fav_id);
				choord_data.write();
			}
		}
	}
	
	public void deleteFavorite( ChordShape chord, IntervalChord key )
	{
		deleteFavorite( chord.getUUID(), key );
	}

	FavPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Favorite Shapes");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		add(lblNewLabel, BorderLayout.NORTH);
		
		mFavPanel = new JPanel();
		add(mFavPanel, BorderLayout.CENTER);
		setVisible(true);
	}
}
