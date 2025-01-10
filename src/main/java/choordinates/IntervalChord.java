package choordinates;

import java.util.ArrayList;
import java.util.Iterator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * IntervalChords are abstract chords defined by their relationship
 * to the tonic.  For instance, a major chord is defined as the 
 * intervals 1 3 5.   The corresponding minor chord is represented 
 * as 1 3b 5. 
 * Intervals may also represent inverted chords by the use of 
 * negative numbers.  EG: -3 1 3 5 could represent a major chord 
 * with an inverted 7th.    
 * Valid intervals are 1-14 and (-1)-(-7) 0 is not a valid interval.
 * Interval numbers may also be suffixed with 1 or more flat or sharp
 * characters.
 */

public class IntervalChord extends AbstractChord
{
	@JsonProperty("symbol")
	private String mSymbol =  "";	//The display symbol. EG: m for minor, m7 for minor 7th
	@JsonProperty("aliases")
	private ArrayList<String> mAliases = new ArrayList<String>(); // Aliases for the chord.

	public IntervalNote getNote(int id)
	{
		return (IntervalNote) mNotes.get(id);
	}
	
	public IntervalChord()
	{
	}
	
	public IntervalChord( ToneChord tone_chord )
	{
		//Create an intervalchord from a tone chord
		ToneNote root_note = tone_chord.getNote(0);

		mName = root_note.getName();
		int root_semi = root_note.getOctaveSemitone();

		for (int i=0;i<tone_chord.getNumNotes();++i)
		{
			IntervalNote interval_note = new IntervalNote();
			ToneNote tone_note = tone_chord.getNote(i);
			
			if (i==0)
			{
				interval_note.setID(1);
			}
			else
			{
				int semitones = ( tone_note.getOctaveSemitone() - root_semi ) % 12;
				
				if (semitones < 0)
				{
					semitones += 12;
				}

				interval_note = new IntervalNote( semitones );
			}

			mNotes.add(interval_note);
		}
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

	@JsonIgnore
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

	@JsonIgnore
	public void setSymbol(String symbol)
	{
		mSymbol = symbol;
	}
	
	@JsonIgnore
	public String getSymbol()
	{
		return mSymbol;
	}
	
	@JsonIgnore
	public ArrayList<String> getAliases() {
		// Return the list of aliases.
		return mAliases;
	}

	@JsonIgnore
	public String getAliasesString()
	{
		return String.join(" ", mAliases);
	}
	
	@JsonIgnore
	public ArrayList<String> getAllChordNames() {
		// Return all names for this chord (preferred first)
		ArrayList<String> notes = getAliases();
		// It's a short list. Who cares about the cost?
		notes.add(0, mName);
		return notes;
	}

	public static IntervalChord parse(String intervals)
	{
		/*
		 * Tries to turn a delimited list into intervalchords.
		 */
		
		String delims =" ,\t\r\n\0";
				
		int begin = 0;
		int end;
		boolean whitespace = true;
		
		IntervalChord chord = new IntervalChord();
		
		//TODO: Sloppy lazy parser.  Could probably be more efficient and less ugly.
		intervals += " ";  //This is just shameful.
		
		for (end = begin; end < intervals.length(); ++end)
		{
			IntervalNote note;
			
			//Look for a delimiter character
			if ( delims.indexOf( intervals.charAt(end) ) > -1 )
			{
				if (!whitespace)
				{
					String string_name = intervals.substring(begin, end);
					begin = end;
					try
					{
						note = new IntervalNote( string_name );
					}
			        catch (IllegalArgumentException exception)
			        {
			        	throw exception;
					}
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
		if (chord.getNumNotes() ==0)
		{
			throw new IllegalArgumentException("Must contain at least one note.");
		}
		
		return chord;
	}

}
