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
	
	@JsonIgnore
	public int getInterval( int note_id )
	{
		return mNotes.get(note_id).getID();
	}

	@JsonIgnore
	public ArrayList<Integer> getIntervals( )
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
				
		for (int i=0;i< mNotes.size(); ++i)
		{
			result.add( getInterval(i) );
		}
		
		return result;
	}

	@JsonIgnore
	public String getName() {
		return mName;
	}

	@JsonIgnore
	public ArrayList<String> getAllNoteNames()
	{
		//Return names of all notes in the chord.
		
		ArrayList<String> names = new ArrayList<String>();
		for (AbstractNote note : mNotes)
		{
			names.add(note.getName());
		}
		return names;
	}

	public AbstractNote getNote(int id)
	{
		return (AbstractNote) mNotes.get(id);
	}

	@JsonIgnore
	public ArrayList<AbstractNote> getNotes() {
		return mNotes;
	}

	@JsonIgnore
	public int getNumNotes() {
		return mNotes.size();
	}

	@JsonProperty("UUID")
	public void setUUID(UUID uuid)
	{
		mUUID = uuid;
	}
	
	@JsonProperty("UUID")
	public UUID getUUID()
	{
		if (mUUID == null)
		mUUID = UUID.randomUUID();
		return mUUID;
	}
	
	@JsonIgnore
	public void setName(String name) {
		mName = name;
	}

	@JsonIgnore
	public void setNotes(AbstractNote[] notes) {
		mNotes.clear();
		for (AbstractNote note : notes) {
			mNotes.add(note);
		}
	}
	
	public void addNote(AbstractNote note) {
		// Add note even if nonunique.
		mNotes.add(note);
	}

	public void addNoteIfNew(AbstractNote note) {
		// Add note only if unique
		for (AbstractNote c : mNotes) {
			if (c.equivalent(note)) {
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
			if (note.equivalent(item)) {
				iter.remove();
			}
		}
	}

	public boolean similar(AbstractChord chord)
	{
		if ( chord.getNumNotes() != getNumNotes()  || getNumNotes() < 0 )
		{
			return false;
		}
		
		ArrayList<Integer> semitones = new ArrayList<Integer>();
		
		for (int i=0; i<getNumNotes(); ++i )
		{
			semitones.add( getNote(i).getOctaveSemitone() );
		}
		
		//Accept notes in any order with any number of repeats.
		for (int i=0; i<getNumNotes(); ++i )
		{
			if ( semitones.indexOf( chord.getNote(i).getOctaveSemitone() ) == -1 )
			{
				return false;
			}
		}
		
		return true;
	}
}
