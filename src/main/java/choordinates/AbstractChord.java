package choordinates;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

/*
 * Chord contains the ChordNotes relative the root tone while remaining
 * agnostic to the root tone
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ToneChord.class, name = "tone_chord"),
        @JsonSubTypes.Type(value = IntervalChord.class, name = "interval_chord")
})
public abstract class AbstractChord
{
	private UUID mUUID;  //Generate a UUID.
	@JsonProperty("notes")
	protected ArrayList<AbstractNote> mNotes = new ArrayList<AbstractNote>(); // List of notes comprising the chord.
	@JsonProperty("name")
	protected String mName; // Preferred name for chord
	
	@JsonCreator
	public AbstractChord() {}
	
	@JsonIgnore
	public AbstractChord( AbstractChord chord )
	{
		mUUID = chord.getUUID();
		mName = chord.getName();
		mNotes = new ArrayList<AbstractNote>();
		for (int i=0;i<chord.getNumNotes();++i)
		{
			mNotes.add(chord.getNote(i));
		}
	}
	
	@JsonProperty("UUID")
	public void setUUID(UUID uuid)
	{
		mUUID = uuid;
	}
	
	@JsonProperty("UUID")
	public final UUID getUUID()
	{
		if (mUUID == null)
		mUUID = UUID.randomUUID();
		return mUUID;
	}
	
	@JsonIgnore
	public int getInterval( int note_id )
	{
		return mNotes.get(note_id).getID();
	}
	
	public static int makeAbsoluteInterval( int num )
	{
		int interval = num % 7;
		
		if (interval < 0) //Negative modulo is weird ya'll
		{
			interval += 7;
		}
		
		return interval;
	}
	
	@JsonIgnore
	public int getAbsoluteInterval( int note_id )
	{
		return makeAbsoluteInterval( getInterval( note_id ) );
	}
	
	@JsonIgnore
	public final ArrayList<Integer> getIntervals( )
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
				
		for (int i=0;i< mNotes.size(); ++i)
		{
			result.add( getInterval(i) );
		}
		
		return result;
	}
	
	@JsonIgnore
	public final ArrayList<Integer> getAbsoluteIntervals( )
	{
		//Return intervals in positive single-octave space.
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (int i=0;i< mNotes.size(); ++i)
		{
			//Look carefully at ToneChord.getInterval() and behold the glory of polymorphism!
			result.add( getAbsoluteInterval(i) );
		}
		
		return result;
	}
	
	@JsonIgnore
	public void setName(String name) {
		mName = name;
	}

	@JsonIgnore
	public final String getName() {
		return mName;
	}

	@JsonIgnore
	public final ArrayList<String> getAllNoteNames()
	{
		//Return names of all notes in the chord.
		
		ArrayList<String> names = new ArrayList<String>();
		for (AbstractNote note : mNotes)
		{
			names.add(note.getName());
		}
		return names;
	}
	
	public void addNote(AbstractNote note) {
		// Add note even if nonunique.
		mNotes.add(note);
	}

	public void addNoteIfNew(AbstractNote note) {
		// Add note only if unique
		for (AbstractNote c : mNotes) {
			if (c.equals(note)) {
				return; // Don't add note already in chord.
			}
		}
		mNotes.add(note);
	}

	public void deleteNote(AbstractNote note) {
		// Delete any notes that are exact match.
		Iterator<AbstractNote> iter = mNotes.iterator();

		while (iter.hasNext()) {
			AbstractNote item = iter.next();
			if (note.equals(item)) {
				iter.remove();
			}
		}
	}

	@JsonIgnore
	public ArrayList<AbstractNote> getNotes() {
		return mNotes;
	}

	@JsonIgnore
	public void setNotes(AbstractNote[] notes) {
		mNotes.clear();
		for (AbstractNote note : notes) {
			mNotes.add(note);
		}
	}

	@JsonIgnore
	public final int getNumNotes() {
		return mNotes.size();
	}
	
	public void reduceSharps()
	{
		for(int i=0;i<getNumNotes();++i)
		{
			getNote(i).reduceSharps();
		}
	}
	
	public AbstractNote getNote(int id)
	{
		return (AbstractNote) mNotes.get(id);
	}
}
