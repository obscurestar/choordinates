package choordinates;

/*
 * ToneNote extends the abstract note class to support notes
 * in the standard western scale. EG a note named A-G
 * with one or more flat or sharp modifiers.
 */
public class ToneNote extends AbstractNote{
	private final int[] mToneMap = { 0, 2, 3, 5, 7, 8, 10 };  //ABCDEFG  0 is A-flat.

	public String getName()
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
		
		for (int i=0;i< Math.abs(mSharp); ++i)
		{
			if (mSharp > 0)
			{
				name += "♯";
			}
			else
			{
				name +="♭";
			}
		}

		return name;
	}
	public int getAbsoluteSemitone()
	{
		//Semitones from nearest A.
		return mToneMap[mID] + mSharp;
	}
	
	public int getSemitone()
	{
		//Semitones from middle A.
		return mOctave * 12 + getAbsoluteSemitone();
	}
	
	public boolean parse(String note)
	{
		/*
		 * Tries to generate a note from a string.
		 * On success, sets the member variables accordingly, returns true
		 * else false.
		 */
		 
		
		if (note.length() < 1)
		{
			//String too long or short.
			return false;
		}
		
		String flats = "♭b-";
		String sharps = "♯#+";

		String note_up = note.toUpperCase();
		int note_index = note_up.charAt(0) - 'A';
		
		if (note_index < 0 || note_index > 6)
		{
			//Not a valid note name.
			return false;
		}
		
		mID = note_index;
		
		if (note_up.charAt(0) != note.charAt(0))
		{
			mOctave++;
		}
		
		for (int i=1;i<note.length();++i)
		{
			if (flats.indexOf( note.charAt(1)) != -1)
			{
				mSharp--;
			}
			else if (sharps.indexOf( note.charAt(1)) != -1)
			{
	
				mSharp++;
			}
			else
			{
				//Some other character.
				return false;
			}
		}
		return true;
	}

	public boolean isNote(AbstractNote note)
	{
		//Returns true only when passed note is exact match.
		if (mID == note.getID()
				&& mSharp == note.getSharp()
				&& mOctave == note.getOctave())
		{
			return true;
		}
		return false;
	}
}
