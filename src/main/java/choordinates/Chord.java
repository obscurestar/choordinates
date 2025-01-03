package choordinates;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;

import choordinates.Note;

/*
 * Chord contains the ChordNotes relative the root tone while remaining
 * agnostic to the root tone
 */

public class Chord {
	private ArrayList<Note> mNotes = new ArrayList<Note>(); // List of notes comprising the chord.
	private String mName; // Preferred name for chord
	private ArrayList<String> mAliases = new ArrayList<String>(); // Aliases for the chord.

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void addAlias(String name) {
		// I don't care about duplicates. It's your computation time, user.
		mAliases.add(name);
	}

	public void deleteAlias(String alias) {
		// Deletes all alias matching case-insensitive
		// Doesn't care if the alias isn't found.

		Iterator<String> iter = mAliases.iterator();

		while (iter.hasNext()) {
			String item = iter.next();
			if (alias.toUpperCase() == item.toUpperCase()) {
				iter.remove();
			}
		}
	}

	public void setAliasesFromString(String aliasString) {
		// Flush whatever we had. Byeeee!
		mAliases.clear();

		if (aliasString.length() == 0) {
			// Do not allow both name and alias to be blank.
			if (mName == "") {
				return;
			}
		}

		String[] aliases = aliasString.split(",");

		for (String alias : aliases) {
			mAliases.add(alias.trim());
		}
	}

	public ArrayList<String> getAliases() {
		// Return the list of aliases.
		return mAliases;
	}

	public ArrayList<String> getAllChordNames() {
		// Return all names for this chord (preferred first)
		ArrayList<String> notes = getAliases();
		// It's a short list. Who cares about the cost?
		notes.add(0, mName);
		return notes;
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
