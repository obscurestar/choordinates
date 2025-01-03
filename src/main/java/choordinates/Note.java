package choordinates;

public class Note {
	private int mID = -1;   //A number from 0-6 representing A B C D E F G
	private int mSharp = 0; //A positive or negative number representing how many semitones to move the named note.
	private int mOctave = 0; //A positive or negative number representing distance from middle octave.
	public final int[] mNoteMap = { 0, 2, 3, 5, 7, 8, 10 };  //ABCDEFG  0 is A-flat.
	
	public Note() {};

	public void setID(int id)
	{
		if (id < 0 || id > 6)
		{
			throw new IllegalArgumentException("Note ID valid range 0-11");
		}
		mID = id;
	}
	
	//Setters and getters for Jackson JSON interface.
	public void setSharp(int sharpness)
	{
		mSharp = sharpness;
	}
	
	public void setOctave(int octave)
	{
		mOctave = octave;
	}
	
	public int  getID()
	{
		return mID;
	}
	
	public int getSharp()
	{
		return mSharp;
	}
	
	public int getOctave()
	{
		return mOctave;
	}
	
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
		return mNoteMap[mID] + mSharp;
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

	public boolean isNote(Note note)
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