package choordinates;

import java.util.ArrayList;
import java.util.Iterator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;

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

	@JsonCreator
	public IntervalChord(){}
	
	@JsonIgnore
	public IntervalChord( IntervalChord chord )
	{
		//Copy constructor
		super( chord );
	}
	
	@JsonIgnore
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
			
			int semitones = tone_note.getOctaveSemitone() - root_semi;

			interval_note = new IntervalNote( semitones );

			mNotes.add(interval_note);
		}
	}
	
	@JsonIgnore
	public IntervalChord(String interval_string)
	{
		/*
		 * Tries to turn a delimited list into intervalchords.
		 */
		
		interval_string = interval_string.trim();
		String[] intervals = interval_string.split(" ");
		
		for (String interval:intervals)
		{
			IntervalNote note;
			try
			{
				note = new IntervalNote( interval );
			}
			catch( IllegalArgumentException exception )
	        {
	        	throw exception;
			}
			mNotes.add( note );
		}

		if (getNumNotes() ==0)
		{
			throw new IllegalArgumentException("Must contain at least one note.");
		}
	}
	
	public IntervalNote getNote(int id)
	{
		return (IntervalNote) mNotes.get(id);
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
}
