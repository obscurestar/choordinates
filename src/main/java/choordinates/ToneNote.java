package choordinates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;

/*
 * ToneNote extends the abstract note class to support notes
 * in the standard western scale. EG a note named A-G
 * with one or more flat or sharp modifiers.
 */
public class ToneNote extends AbstractNote{
	@JsonIgnore public static final int[] mNoteMap = { 0, 2, 3, 5, 7, 8, 10 };  //ABCDEFG  0 is A-flat.

	@JsonCreator
	ToneNote(){ super(); }
	
	@JsonIgnore
	ToneNote( ToneNote note )
	{
		super( note );
	}
	
	@JsonIgnore
	public ToneNote( int semitones )
	{
		mOctave = semitones/12;
		
		semitones = mod( semitones, 12 );
		
		mID = findInMap( semitones );
		
		if (mID == -1)
		{
			mID = findInMap( semitones+1 );
			mSharp = -1;
		}

		setID( mod( mID, 7) );
	}
	
	@JsonIgnore
	public ToneNote(ToneNote root, int semitones)
	{	
		super ( root.getSemitone() + semitones );
	}
	
	public ToneNote(String note)
	{
		/*
		 * Tries to generate a note from a string.
		 * Throws exception on failure.
		 */
		 
		IllegalArgumentException bad_name = new IllegalArgumentException("'" + note + "' is not a valid note name. Must be A-G followed by flats or sharps");

		note = note.trim();

		if (note.length() < 1)
		{
			//String too long or short.
			throw bad_name;
		}

		String note_up = note.toUpperCase();
		int note_index = note_up.charAt(0) - 'A';
		
		try 
		{
			setID( note_index );
		}
		catch (IllegalArgumentException e)
		{
			throw bad_name;
		}
						
		if (note_up.charAt(0) != note.charAt(0))
		{
			mOctave++;
		}
		
		if ( note.length() > 1 && !setValidSharp( note.substring(1) ) )
		{
			throw bad_name;
		}
	}

	@JsonIgnore
	@Override
	public String getNoteName()
	{
		/*
		 * Generate the name of this note from the data.
		 */
		String name = "";
		name += (char)('A'+mID);
		
		if (mOctave > 0) //Make higher octaves lower case
		{
			name = name.toLowerCase();
		}
	
		return name;
	}
	
	@JsonIgnore
	protected int[] getNoteMap()
	{
		return mNoteMap;
	}
	
	@Override
	public boolean equivalent(AbstractNote note)
	{
		ToneNote a = new ToneNote( note.getSemitone() );
		ToneNote b = new ToneNote( getSemitone() );
		
		if ( b.getID() == a.getID()
				&& b.getSharp()  == a.getSharp() )
		{
			return true;
		}
		return false;
	}
}
