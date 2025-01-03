package choordinates;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;

import choordinates.AbstractNote;

/*
 * Chord contains the ChordNotes relative the root tone while remaining
 * agnostic to the root tone
 */

public abstract class AbstractChord
{
	protected ArrayList<AbstractNote> mNotes = new ArrayList<AbstractNote>(); // List of notes comprising the chord.
	protected String mName; // Preferred name for chord

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}


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

	// @JsonProperty("notes");
	public ArrayList<AbstractNote> getNotes() {
		return mNotes;
	}

	// @JsonProperty("notes");
	public void setNotes(AbstractNote[] notes) {
		mNotes.clear();
		for (AbstractNote note : notes) {
			mNotes.add(note);
		}
	}

	public int getNumNotes() {
		return mNotes.size();
	}
}
