package choordinates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonCreator;

/*
 * An abstract concept of a note.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ToneNote.class, name = "tone_note"),
        @JsonSubTypes.Type(value = IntervalNote.class, name = "interval_note")
})
public abstract class AbstractNote {
	@JsonIgnore
	private static int[] mOffsetMap = { 0, 2, 4, 5, 7, 9, 11 };
	@JsonProperty("id")
	protected int mID = -1;   //A number from 0-6 representing A B C D E F G
	@JsonProperty("sharps")
	protected int mSharp = 0; //A positive or negative number representing how many semitones to move the named note.
	
	@JsonIgnore
	public static final String mFlatChars = "♭b-";
	@JsonIgnore
	public static final String mSharpChars = "♯#+";
	
	@JsonIgnore
	public abstract String getNoteName();
	@JsonIgnore
	public abstract int getOctaveSemitone();
	@JsonIgnore
	public abstract int getSemitone();
	public abstract boolean equals(AbstractNote note);
	public abstract void reduceSharps();
 
	@JsonCreator
	public AbstractNote() {};
	
	@JsonIgnore
	public AbstractNote( AbstractNote note )
	{
		//Copy constructor.
		mID = note.getID();
		mSharp = note.getSharp();
	}
	
	@JsonIgnore
	public AbstractNote( int semitones )
	{
		semitones = semitones % 12;
		
		if (semitones < 0)
		{
			semitones += 12;
		}
		
		for (int i=0;i<mOffsetMap.length;++i)
		{
			if (semitones == mOffsetMap[i])
			{
				mID = i+1;
				break;
			}
		}
		
		if (mID == -1)
		{
			semitones --;
			for (int i=0;i<mOffsetMap.length;++i)
			{
				if (semitones == mOffsetMap[i])
				{
					mID = i+2;
					mSharp = -1;
					break;
				}
			}
		}
		mID = mID % 7;
	}

	@JsonIgnore
	public final String getName()
	{
		String result = getNoteName();
		for (int i=0;i<Math.abs(mSharp);++i )
		{
			if (mSharp < 0)
			{
				result += mFlatChars.charAt(0);
			}
			else
			{
				result += mSharpChars.charAt(0);
			}
		}
		return result;
	}
	
	@JsonIgnore
	public void setID(int id)
	{
		if (id < 0 || id > 6)
		{
			throw new IllegalArgumentException("Note ID valid range 0-6");
		}
		mID = id;
	}
	
	@JsonIgnore
	public void setSharp(int sharpness)
	{
		mSharp = sharpness;
	}
	
	@JsonIgnore
	public final int  getID()
	{
		return mID;
	}
	
	@JsonIgnore
	public final int getSharp()
	{
		return mSharp;
	}
}
