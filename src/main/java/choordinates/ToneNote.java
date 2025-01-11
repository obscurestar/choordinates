package choordinates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;

/*
 * ToneNote extends the abstract note class to support notes
 * in the standard western scale. EG a note named A-G
 * with one or more flat or sharp modifiers.
 */
public class ToneNote extends AbstractNote{
	private final int[] mToneMap = { 0, 2, 3, 5, 7, 8, 10 };  //ABCDEFG  0 is A-flat.
	@JsonProperty("octave")
	private int mOctave = 0; //A positive or negative number representing distance from middle octave.

	@JsonCreator
	ToneNote(){ super(); }
	
	@JsonIgnore
	ToneNote( ToneNote note )
	{
		//Copy constructor
		super( note );
		mOctave = note.getOctave();
	}
	
	@JsonIgnore
	public ToneNote(ToneNote root, int in_semitones)
	{
		
		/*Given a tone note and semitones create a relative note.*/
		
		int semitones = (root.getOctaveSemitone() + in_semitones) % 12;
		
		mID = -1;
		
		for (int i=0;i<mToneMap.length;++i)
		{
			if (semitones == mToneMap[i])
			{
				mID = i;
				break;
			}
		}
		
		if (mID == -1)
		{
			semitones --;
			for (int i=0;i<mToneMap.length;++i)
			{
				if (semitones == mToneMap[i])
				{
					mID = (i+1)%7;
					mSharp = -1;
					break;
				}
			}
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
	public void setOctave(int octave)
	{
		mOctave = octave;
	}
	
	@JsonIgnore
	public int getOctave()
	{
		return mOctave;
	}

	@JsonIgnore
	public int getOctaveSemitone()
	{
		//Semitones from nearest A.
		return mToneMap[mID] + mSharp;
	}
	
	@JsonIgnore
	public int getSemitone()
	{
		//Semitones from middle A.
		return mOctave * 12 + getOctaveSemitone();
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
		
		if (note_index < 0 || note_index > 6)
		{
			//Not a valid note name.
			throw bad_name;
		}
				
		mID = note_index;
		
		if (note_up.charAt(0) != note.charAt(0))
		{
			mOctave++;
		}
		
		for (int i=1;i<note.length();++i)
		{
			if (mFlatChars.indexOf( note.charAt(i)) != -1)
			{
				mSharp--;
			}
			else if (mSharpChars.indexOf( note.charAt(i)) != -1)
			{
				mSharp++;
			}
			else
			{
				//Some other character.
				throw bad_name;
			}
		}
	}

	public boolean equals(AbstractNote abs_note)
	{
		//Notes must be of same subclass to be identical.
		ToneNote note;
		if (abs_note instanceof ToneNote) {
            note = (ToneNote) abs_note;
		}
		else
		{
			return false;
		}
            
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
