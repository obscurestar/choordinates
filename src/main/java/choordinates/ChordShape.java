package choordinates;

import java.util.ArrayList; 

/* A class describing the shape of a chord for favorite chord selection*/

public class ChordShape {
	private int mSpan=-1; //Number of frets the shape spans.
	private int[] mStrings;
	
	public ChordShape(int first_fret, int num_strings, int[] strings)
	{
		mStrings = new int[ num_strings ];
		
		int lowest = Integer.MAX_VALUE;
		
		for (int i=0;i<mStrings.length;++i)
		{
			
			mStrings[i] = strings[i];
			
			if (mStrings[i] != -1)
			{
				mStrings[i] -= first_fret;
				lowest = Math.min(mStrings[i], lowest);
			}
		}
		for (int i=0;i<mStrings.length;++i)
		{
			if (mStrings[i] != -1)
			{
				mStrings[i] -= lowest;
				mSpan = Math.max(mStrings[i], mSpan);
			}
		}
	}
	
	public boolean isValid()
	{
		if (mSpan > -1)
		{
			return true;
		}
		return false;
	}
}
