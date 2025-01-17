package com.obscurestar.choordinates;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class IntervalNote extends AbstractNote{
	@JsonIgnore public static final int[] mNoteMap = { 0, 2, 4, 5, 7, 9, 11 };
	
    //TODO something better than colors but the order they appear in the chord.
    //Black white yellow orange red pink
    //If you have more than 6 fingers, colors loop.  Sorry!
	@JsonIgnore 
	private static final Color[] mNoteColors = { Color.BLACK, Color.WHITE, Color.YELLOW, Color.ORANGE, Color.RED, Color.PINK };
    
	@JsonCreator
	IntervalNote(){ super(); }
	
	IntervalNote( IntervalNote note )
	{
		super( (AbstractNote) note );
	}
		
	@JsonIgnore
	public IntervalNote( int semitones )
	{	
		super( semitones );
		if (semitones < 0)
		{
			//Lousy ancient Greeks and their lousy
			//lack of understanding of the concept of 0.
			//They don't know nothing.

			mOctave--;
		}
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
	
		int begin = 0;
		boolean negative = false;
	
		if ( note.charAt(0) == '-')
		{
			begin = 1;
			mOctave--;
			negative = true;
		}
				
		int end = begin;
		
		//0 is not a valid interval but 10 could be.
		if ( note.charAt(begin) == '0' )
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
		
		if (negative) begin--;
		
		int new_id = Integer.valueOf( note.substring(begin, end) ) - 1;
		
		if (negative) new_id++;
		
		setSafeID( new_id );
		
		if ( note.length() > 1 && !setValidSharp( note.substring(end) ) )
		{
			throw bad_name;
		}
	}

	@JsonIgnore
	public int getOctaveSemitone()
	{
		return mNoteMap[ mID ] + mSharp;
	}
	
	@JsonIgnore
	public int getSemitone()
	{
		return  getOctaveSemitone() + mOctave*12;
	}
	
	@JsonIgnore
	public String getNoteName()
	{
		//Bite me Pythagoras
		return new String( String.valueOf( mID + 1) );
	}

	@JsonIgnore
	protected int[] getNoteMap()
	{
		return mNoteMap;
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
	
	public boolean equivalent(AbstractNote note)
	{
		//TODO refactor to get rid of this constructor abuse.
		IntervalNote a = new IntervalNote( (IntervalNote)note );
		a = new IntervalNote( a.getSemitone() );
		IntervalNote b = new IntervalNote( getSemitone() );
		
		//NOTE, ignores octave.   && b.getOctave() == a.getOctave()
		if ( b.getID() == a.getID()
				&& b.getSharp()  == a.getSharp() )
		{
			return true;
		}
		return false;
	}
	
	@JsonIgnore
	public static Color getColor( int cid )
	{
		return mNoteColors[ mod ( cid, mNoteColors.length ) ];
	}
}
