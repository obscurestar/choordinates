package com.obscurestar.choordinates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

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
	@JsonProperty("notes")
	protected ArrayList<AbstractNote> mNotes = new ArrayList<AbstractNote>(); // List of notes comprising the chord.
	@JsonProperty("name")
	protected String mName; // Preferred name for chord
	
	@JsonCreator
	public AbstractChord() {}
	
	@JsonIgnore
	public AbstractChord( AbstractChord chord )
	{
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

	public void reduceNotes()
	{
		//Remove duplicate notes.
		
		Set<String> unique = new HashSet<String>();
		
		for( AbstractNote note:mNotes )
		{
			unique.add(note.getName());
		}
		
		//Doing it this way to preserve note order.
		boolean[] already_found = new boolean[unique.size()]; //Defaults to false on create
		
		//No way to raw index into sets?  Good grief.
		ArrayList<String> unique_list = new ArrayList<>(unique);
		
		for (int i=0;i<unique_list.size();++i)
		{
			String unique_name = unique_list.get(i);
			Iterator<AbstractNote> iter = mNotes.iterator();
			while (iter.hasNext())
			{
				AbstractNote note = iter.next();
				if (unique_name.compareTo(note.getName()) == 0)
				{
					if ( already_found[i] )
					{
						iter.remove();
					}
					else
					{
						already_found[i] = true;
					}
				}
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
	
	@JsonIgnore
	public boolean hasNotes()
	{
		if (getNumNotes() > 0)
		{
			return true;
		}
		return false;
	}
}
