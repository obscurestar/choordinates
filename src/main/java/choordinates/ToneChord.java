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
	
	public ToneChord(String tuning)
	{
		/*Because I'm lazy and don't want to make a super complex UI,
		 * the user suffers for having to use a string to describe the 
		 * tuning of the strings and I get to write a string parser.
		 * 
		 * String names are a letter A-G optionally followed by a sharp or flat 
		 * character. Sharps may be represented by + # or ♯.
		 * - or ♭ may be  used for flats. 
		 * String tone is assumed to be ascending from left to right but a 
		 * lowercase string name elevates. 
		 * String identities may be separated by whitespace or commans.
		 * 
		 * Some examples:
		 * E standard may be represented as:
		 *   E A D G B E   //Implicitly by order, High e is an octave above E
		 *   e,a d,g,b, e
		 *   E, A, D, G, B, e  //Explicitly assert High e is above E.
		 * 
		 * E♭ standard could be represented with variants of
		 * 	 E- Ab D♭ G- B- E-
		 *   and so on.
		 *   
		 * My banjolele's first string is above the pitch of the C string
		 * and would be represented as
		 * 	g C E A  //In this representation, g's pitch is between E and A. 
		 */
		
		tuning.trim();
		
		String delims =" ,\t\r\n\0";
				
		int begin = 0;
		int end;
		boolean whitespace = true;
				
		//TODO: Sloppy lazy parser.  Could probably be more efficient and less ugly.
		tuning += " ";  //This is just shameful.
		
		for (end = begin; end < tuning.length(); ++end)
		{
			ToneNote note;
			
			//Look for a delimiter character
			if ( delims.indexOf( tuning.charAt(end) ) > -1 )
			{
				if (!whitespace)
				{
					String string_name = tuning.substring(begin, end);
					begin = end;
					
					try
					{
						note = new ToneNote(string_name);
					}
			        catch (IllegalArgumentException exception)
			        {
			        	throw exception;
					}
					mNotes.add(note);
				}
				begin = end;
				whitespace = true;
			}
			else
			{
				if (whitespace)
				{
					begin = end;
				}
				whitespace = false;
			}
		}
		if (getNumNotes() ==0)
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
	
	@Override
	@JsonIgnore
	public int getAbsoluteInterval( int note_id )
	{
		return AbstractChord.makeAbsoluteInterval( getInterval(note_id) );
	}
	
	public ToneNote getNote(int id)
	{
		return (ToneNote) mNotes.get(id);
	}
}
