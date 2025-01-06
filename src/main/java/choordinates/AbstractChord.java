package choordinates;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;

import choordinates.AbstractNote;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	@JsonProperty("notes")
	protected ArrayList<AbstractNote> mNotes = new ArrayList<AbstractNote>(); // List of notes comprising the chord.
	@JsonProperty("name")
	protected String mName; // Preferred name for chord

	@JsonIgnore
	public void setName(String name) {
		mName = name;
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
	
	public void addNote(AbstractNote note) {
		// Add note even if nonunique.
		mNotes.add(note);
	}

	public void addNoteIfNew(AbstractNote note) {
		// Add note only if unique
		for (AbstractNote c : mNotes) {
			if (c.isNote(note)) {
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
			if (note.isNote(item)) {
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
	public int getNumNotes() {
		return mNotes.size();
	}
	
	public AbstractNote getNote(int id)
	{
		return (AbstractNote) mNotes.get(id);
	}
}
