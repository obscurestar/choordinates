package choordinates;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;

//Note chords extend the abstract Chord class to represent notes on an A-Gb scale

public class ToneChord extends AbstractChord {

	@JsonCreator
	public ToneChord() {};
	
	@JsonIgnore
	public ToneChord( ToneChord chord )
	{
		//Copy constructor.
		super( chord );
	}
	
	@JsonIgnore
	public ToneChord( ToneNote root, IntervalChord intervals)
	{		
		mName = root.getName().toUpperCase() + " " + intervals.getName() //C minor Seventh
		+ " (" + root.getName().toUpperCase() + intervals.getSymbol() + ") -";  //(Cm7)
		
		int root_semitones = intervals.getNote(0).getOctaveSemitone();
		
		for (int i=0; i < intervals.getNumNotes(); ++i)
		{
			int semitones = intervals.getNote(i).getOctaveSemitone() - root_semitones;
			
			if (semitones < 0) semitones += 12;
			
			ToneNote note = new ToneNote( root, semitones);
			
			mName += " " + note.getName();
			
			mNotes.add(note);
		}
	}
	
	public ToneChord(String chord_string)
	{	
		chord_string = chord_string.trim();
		String[] note_strings = chord_string.split(" ");
		
		for(String note_string:note_strings)
		{
			ToneNote note;
			try
			{
				note = new ToneNote( note_string );
			}
			catch( IllegalArgumentException exception )
			{
				throw exception;
			}
			mNotes.add( note );
		}
		
		if ( getNumNotes() == 0 )
		{
			throw new IllegalArgumentException("Must contain at least one note.");
		}
	}
	
	@Override
	@JsonIgnore
	public int getInterval( int note_id )
	{
		//For tone chords, we want to get the interval relative to the root note.
		return super.getInterval(note_id) - mNotes.get(0).getID();
	}
		
	public ToneNote getNote(int id)
	{
		return (ToneNote) mNotes.get(id);
	}
}
