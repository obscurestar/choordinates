package choordinates;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Note chords extend the abstract Chord class to represent notes on an A-Gb scale

public class ToneChord extends AbstractChord {

	public ToneChord() {};
	
	@JsonIgnore
	public ToneChord( ToneNote root, IntervalChord intervals)
	{
		int root_id = root.getID();
		
		mName = root.getName().toUpperCase() + intervals.getName() //C minor Seventh
		+ " (" + root.getName() + intervals.getSymbol() + ")";  //(Cm7)
		
		for (int i=0; i < intervals.getNumNotes(); ++i)
		{
			int offset = intervals.getNote(i).getID() - 1;
			
			ToneNote note = new ToneNote();
			
			note.setID( (root_id + offset) % 7);
			note.setSharp( root.getSharp() + intervals.getNote(i).getSharp());
			
			mName += " " + note.getName();
			
			mNotes.add(note);
		}
	}
	
	
	public ToneNote getNote(int id)
	{
		return (ToneNote) mNotes.get(id);
	}
	
	public static ToneChord parse(String tuning)
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
		
		String delims =" ,\t\r\n\0";
				
		int begin = 0;
		int end;
		boolean whitespace = true;
		
		ToneChord chord = new ToneChord();
		
		//TODO: Sloppy lazy parser.  Could probably be more efficient and less ugly.
		tuning += " ";  //This is just shameful.
		
		for (end = begin; end < tuning.length(); ++end)
		{
			ToneNote note = new ToneNote();
			
			//Look for a delimiter character
			if ( delims.indexOf( tuning.charAt(end) ) > -1 )
			{
				if (!whitespace)
				{
					String string_name = tuning.substring(begin, end);
					begin = end;
					if (note.parse(string_name))
					{
						chord.addNote(note);
					}
					else
					{
						throw new IllegalArgumentException("'" + string_name + "' is not a valid note name. Must be A-G followed by flats or sharps");

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
