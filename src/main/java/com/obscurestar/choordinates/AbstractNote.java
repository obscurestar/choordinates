package com.obscurestar.choordinates;

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
	@JsonIgnore private int[] mNoteMap = { 1, 2 ,3 };
	@JsonIgnore public static final String mFlatChars = "♭b-";
	@JsonIgnore public static final String mSharpChars = "♯#+";
	@JsonProperty("id")
	protected int mID = -1;   //A number from 0-6 representing A B C D E F G
	@JsonProperty("sharps")
	protected int mSharp = 0; //A positive or negative number representing how many semitones to move the named note.
	@JsonProperty("octave")
	protected int mOctave = 0; //+- octaves from center

	@JsonCreator
	public AbstractNote() {}

	@JsonIgnore
	public AbstractNote( AbstractNote note )
	{
		//Copy constructor.
		mID = note.getID();
		mSharp = note.getSharp();
		mOctave = note.getOctave();
	}

	@JsonIgnore
	public AbstractNote( int semitones )
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

	@JsonIgnore public abstract String getNoteName();
	@JsonIgnore protected abstract int[] getNoteMap();
	public abstract boolean equivalent(AbstractNote note);
	 
	@JsonIgnore
	public String getName()
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
	public int  getID()
	{
		return mID;
	}

	@JsonIgnore
	public int getSharp()
	{
		return mSharp;
	}

	@JsonIgnore
	public int getOctave()
	{
		return mOctave;
	}

	@JsonIgnore
	public int getOctaveSemitone()
	{
		return getNoteMap()[ mID ] + mSharp;
	}
	
	@JsonIgnore
	public int getSemitone()
	{
		return  getOctaveSemitone() + mOctave*12;
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
	public void setOctave( int octave)
	{
		mOctave = octave;
	}
	
	@JsonIgnore
	public void setSafeID( int id )
	{
		//Just assumes you know what you're doing!
		setID( mod (id, 7 ) );
	}

	public static int mod(int val, int mod)
	{
		//Handles negatives correctly for our context.
		val = val % mod;
		if (val < 0 )
		{
			val += mod;
		}
		return val;
	}
	
	@JsonIgnore
	protected boolean setValidSharp(String str)
	{
		/*Sets sharps and flats from string
		 * returns offset
		 */
		for (int index=0;index<str.length(); ++index)
		{
			if (mFlatChars.indexOf( str.charAt(index)) != -1)
			{
				mSharp--;
			}
			else if (mSharpChars.indexOf( str.charAt(index)) != -1)
			{
				mSharp++;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	protected int findInMap( int num )
	{		
		num = mod(num,12);
		int result = -1;
		
		int note_map[] = getNoteMap();

		for (int i=0;i<note_map.length;++i)
		{
			if (num == note_map[i])
			{
				result = i;
				break;
			}
		}
		return result;
	}
	

}
