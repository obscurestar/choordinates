package choordinates;

//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.UUID;
import javax.swing.JPanel;
import java.util.HashMap;
import java.util.UUID;

import choordinates.FretPanel.Select;
import java.awt.Dimension;
import java.awt.GridLayout;

public class FavPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	//ArrayList<FretPanel> mFavList = new ArrayList<FretPanel>();
	HashMap<UUID, FretPanel> mFavList = new HashMap<UUID, FretPanel>();
	FavHandler mCallback;

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
		for( int i=0;i<mFavList.size();++i )
		{
			mFavList.get(i).setCallback(mCallback);
		}
	}
	
	public void adjustLayout(Dimension size) {
		double panel_aspect_ratio = size.getWidth() / size.getHeight();

		int cols = 1;

		if (mFavList.size() > 1) {
			cols = (int) Math.sqrt(mFavList.size() * panel_aspect_ratio);
		}

		setLayout(new GridLayout(0, cols, 0, 0));
		revalidate();
		repaint();
	}

	public void flush() {
		if (mFavList.size() > 0) {
			for (FretPanel fav : mFavList.values()) {
				remove(fav);
			}
		}
		mFavList.clear();
	}

	public void loadFavorites( UUID group_id, ToneNote roote_note, IntervalChord interval_chord )
	{
		ToneChord tuning = ChoordData.getInstance().getCurrentTuning();
		System.out.println("Loading favorites from group: " + tuning.getAllNoteNames() + " interval " + interval_chord.getAllNoteNames() + " groupd_id " + group_id.toString());

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
		fret_panel.setNumFrets(7);
		fret_panel.setFirstFret(fav_semitones); //should happen after setNumFrets.
		fret_panel.markFrets();
		setFavCallback(fret_panel);

		mFavList.put(chord_shape.getUUID(),fret_panel);
		add(fret_panel);

		adjustLayout(this.getSize());
	}
	
	public void deleteFavorite( UUID fav_id )
	{
		//Handling dialog and save here because why do either if it's a bad selection.
		if (mFavList.containsKey( fav_id ) )
		{
			if( Choordinates.confirm( "Delete Favorite", "This operation cannot be undone.") )
			{
				remove ( mFavList.get(fav_id) );
				mFavList.remove( fav_id );
				adjustLayout(this.getSize());
				//SPATTERS delete from ChordData and write.
			}
		}
	}
	
	public void deleteFavorite( ChordShape chord )
	{
		deleteFavorite( chord.getUUID() );
	}

	FavPanel() {
		setLayout(new GridLayout(0, 2, 0, 0));
		setVisible(true);
		// setSize(400, 400);
	}
}
