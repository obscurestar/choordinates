package choordinates;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class IntervalNote extends AbstractNote{
	private final int[] mIntervalMap = { 0, 2, 4, 5, 7, 9, 11 };
	
    //TODO something better than colors but the order they appear in the chord.
    //Black white yellow orange red pink
    //If you have more than 6 fingers, colors loop.  Sorry!
	@JsonIgnore 
	private static final Color[] mNoteColors = { Color.BLACK, Color.WHITE, Color.YELLOW, Color.ORANGE, Color.RED, Color.PINK };
    
	@JsonCreator
	IntervalNote(){ super(); }
	
	@JsonIgnore
	public IntervalNote( int semitones )
	{
		semitones = semitones % 12;
		
		if (semitones < 0)
		{
			semitones += 12;
		}
		
		for (int i=0;i<mIntervalMap.length;++i)
		{
			if (semitones == mIntervalMap[i])
			{
				mID = i+1;
				break;
			}
		}
		
		if (mID == -1)
		{
			semitones --;
			for (int i=0;i<mIntervalMap.length;++i)
			{
				if (semitones == mIntervalMap[i])
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
	public int getOctaveSemitone()
	{
		//Lousy ancient Greeks and their lousy
		//lack of understanding of the concept of 0.
		//They don't know nothing.
		if (mID > 0)
		{
			return mIntervalMap[ (mID-1) % 7 ] + mSharp;
		}
		
		//Handle negative intervals.
		return mIntervalMap[ (7 - (mID+1)) % 7] + mSharp;
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
	
	@JsonIgnore
	public IntervalNote(String note)
	{
		/*
		 * Tries to generate a note from a string.
		 * On success, sets the member variables accordingly, returns true
		 * else false.
		 */
		
		IllegalArgumentException bad_name = new IllegalArgumentException("'" + note + "' is not a valid note name. Must be 1-7followed by flats or sharps");	 
		
		note = note.trim();
		
		if (note.length() < 1)
		{
			//String too long or short.
			throw bad_name;
		}
		
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
			throw bad_name;
		}
		
		while( end < note.length() 
				&& numchars.indexOf( note.charAt(end) ) != -1) 
		{
				++end;
		}
		
		if (end == begin)
		{
			throw bad_name;   //Character wasn't a number.
		}
		
		mID = Integer.valueOf( note.substring(0, end) );
		
		begin = end;
		for (end = begin; end < note.length(); ++end)
		{
			if (mFlatChars.indexOf( note.charAt(end)) != -1)
			{
				mSharp--;
			}
			else if (mSharpChars.indexOf( note.charAt(end)) != -1)
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
	
	public String getNoteName()
	{
		return new String( String.valueOf( mID ) );
	}
	
	public void reduceSharps()
	{
		/*While Ebb and E# are valid notes in some contexts, it can be annoying in others.*/
		
		int semitones = getOctaveSemitone();
		
		if (mSharp == 0)
		{
			//Already good.
			return;
		}
		
		//How much does a ctor cost, really?
		IntervalNote note = new IntervalNote( semitones );
		mID = note.getID();
		mSharp = note.getSharp();
	}
	
	public boolean isNote(AbstractNote note)
	{
		return false;
	}
	
	@JsonIgnore
	public static Color getColor( int cid )
	{
		return mNoteColors[ cid % mNoteColors.length ];
	}
}
