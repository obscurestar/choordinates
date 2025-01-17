package com.obscurestar.choordinates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.UUID;

/* A class describing the shape of a chord for favorite chord selection*/

public class ChordShape{
	@JsonProperty("span")
	private int mSpan=-1; //Number of frets the shape spans.
	@JsonProperty("strings")
	private int[] mStrings;
	@JsonProperty("lowest_string")
	private int mLowestString=Integer.MAX_VALUE; //First string where a note is set.
	@JsonProperty("first_note")
	private IntervalNote mFirstNote; //The interval note at the first string.
	@JsonProperty("uuid")
	private UUID mUUID;
	
	@JsonCreator
	public ChordShape(){}
	
	@JsonIgnore
	private void generateUUID()
	{		
		byte[] uuid_bytes = new byte[mStrings.length];
		
		//Get the strings that constitute this favorite.
		for( int i=0;i<mStrings.length;++i)
		{
			uuid_bytes[i] = (byte)mStrings[i];
		}
		
		mUUID = UUID.nameUUIDFromBytes( uuid_bytes );
	}
	
	@JsonIgnore
	public ChordShape(ToneChord tuning, ToneNote root_note, IntervalChord intervals, int first_fret, int num_strings, int[] strings)
	{
		ToneChord selected = new ToneChord(root_note, intervals);
		mStrings = new int[ num_strings ];
		mLowestString = Integer.MAX_VALUE;
		
		int lowest = Integer.MAX_VALUE;
		int highest = Integer.MIN_VALUE;
		
		//Used to look up names. 
		//ToneChord tone_chord = new ToneChord (root_note, interval_chord);
						
		for (int i=0;i<num_strings;++i)
		{
			
			mStrings[i] = strings[i];
	
			if (mStrings[i] > -1)
			{
				mStrings[i] -= first_fret;
				
				highest = Math.max(highest,  mStrings[i]);
				if (mStrings[i] < lowest)
				{
					lowest = mStrings[i];
					mLowestString = i;
					
					//Get the note name from the tuning. 
					ToneNote string_note = new ToneNote(  tuning.getNote(i), strings[i] + first_fret );
					
					for (int j=0;j<selected.getNumNotes();++j)
					{
						if ( string_note.getName().toUpperCase().compareTo(
								selected.getNote(j).getName().toUpperCase()) == 0 )
						{
							//Found it.
							mFirstNote = new IntervalNote(  selected.getNote(j).getOctaveSemitone() - root_note.getOctaveSemitone());
							break;
						}
					}
				}

			}
		}
		
		if (mFirstNote == null)
		{
			throw new IllegalArgumentException("No notes selected.");
		}
		
		mSpan = (highest - lowest) + 1;
		
		for (int i=0;i<mStrings.length;++i)
		{
			if (mStrings[i] != -1)
			{
				mStrings[i] -= lowest;
			}
		}
		generateUUID();
	}
 
	@JsonIgnore
	public int getLowestString()
	{
		return mLowestString;
	}
	
	@JsonIgnore
	public int getString(int num)
	{
		return mStrings[num];
	}
	
	@JsonIgnore
	public IntervalNote getFirstNote()
	{
		return mFirstNote;
	}
	
	@JsonIgnore
	public int getSpan()
	{
		return mSpan;
	}
	
	@JsonIgnore
	public boolean isValid()
	{
		if (mFirstNote ==null)
		{
			return false;
		}
		return true;
	}
	
	@JsonIgnore
	public UUID getUUID()
	{
		if ( mUUID == null )
		{
			throw new NullPointerException("UUID must be generated first.");
		}
		return mUUID;
	}
}
