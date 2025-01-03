package choordinates;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;

import choordinates.Note;

/*
 * Chord contains the ChordNotes relative the root tone while remaining
 * agnostic to the root tone
 */

public class Chord
{
	protected ArrayList<Note> mNotes = new ArrayList<Note>(); // List of notes comprising the chord.
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
		for (Note note : mNotes)
		{
			names.add(note.getName());
		}
		return names;
	}
	
	public void addNote(Note note) {
		// Add note even if nonunique.
		mNotes.add(note);
	}

	public void addNoteIfNew(Note note) {
		// Add note only if unique
		for (Note c : mNotes) {
			if (c.isNote(note)) {
				return; // Don't add note already in chord.
			}
		}
		mNotes.add(note);
	}

	public void deleteNote(Note note) {
		// Delete any notes that are exact match.
		Iterator<Note> iter = mNotes.iterator();

		while (iter.hasNext()) {
			Note item = iter.next();
			if (note.isNote(item)) {
				iter.remove();
			}
		}
	}

	// @JsonProperty("notes");
	public ArrayList<Note> getNotes() {
		return mNotes;
	}

	// @JsonProperty("notes");
	public void setNotes(Note[] notes) {
		mNotes.clear();
		for (Note note : notes) {
			mNotes.add(note);
		}
	}

	public int getNumNotes() {
		return mNotes.size();
	}
}
