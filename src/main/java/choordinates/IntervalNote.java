package choordinates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class IntervalNote extends AbstractNote{
	private final int[] mIntervalMap = { 0, 2, 4, 5, 7, 9, 11 };
	
	@JsonIgnore
	public String getName()
	{
		String name = String.valueOf(mID);
		
		for( int i=0; i<Math.abs(mSharp); ++i)
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
	
	@JsonIgnore
	public int getOctaveSemitone()
	{
		//Lousy ancient Greeks and their lousy
		//lack of understanding of the concept of 0.
		//They don't know nothing.
		if (mID > 0)
		{
			return mIntervalMap[ (mID-1) % 7 ];
		}
		
		//Handle negative intervals.
		return mIntervalMap[ (7 - (mID+1)) % 7];
	}
	
	@JsonIgnore
	public int getSemitone()
	{
		//TODO verify negative interval logic.
		int semitone = getOctaveSemitone();
		
		int id = Math.abs(mID + 1) / 7;
		
		if (mID > 0)
		{
			return semitone + ( (id - 1) * 12);
		}
		
		return (id * -12) + semitone;
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
		String numchars = "0123456789";
		
		int begin=0;
		
		if ( note.charAt(0) == '-')
		{
			begin=1;
		}
				
		int end = begin;
		
		//0 is not a valid interval but 10 could be.
		if ( note.charAt(end) == '0' )
		{
			return false;
		}
		
		while( end < note.length() 
				&& numchars.indexOf( note.charAt(end) ) != -1) 
		{
				++end;
		}
		
		if (end == begin)
		{
			return false;   //Character wasn't a number.
		}
		
		mID = Integer.valueOf( note.substring(0, end) );
		
		begin = end;
		for (end = begin; end < note.length(); ++end)
		{
			if (flats.indexOf( note.charAt(end)) != -1)
			{
				mSharp--;
			}
			else if (sharps.indexOf( note.charAt(end)) != -1)
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
		return false;
	}
}
