package choordinates;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ChoordData {
	//TODO:  Probably a better filepath.
	private static final String filename ="choordinates.json";
	private ArrayList<ToneChord> mTunings = new ArrayList<>();
	private int mCurrentTuning;
	private ArrayList<IntervalChord> mChords = new ArrayList<>();
	private int mCurrentChord;

	public ChoordData()
	{
		mCurrentTuning = -1;
		mCurrentChord = -1;
	}
	
	/*
	 * TODO There's a LOT of commonality between Chord and Tone
	 * If we add complexity here, refactor these to cast to the 
	 * AbstractChord and convert to wrappers.
	 */
	  
	//Functions related to chords.
	public int getNumChords()
	{
		return mChords.size();
	}
	
	public boolean validChord(int id)
	{
		if (id >= 0 && id < mChords.size())
		{
			return true;
		}

		return false;
	}
	
	public void setCurrentChord(int id)
	{
		//Sets current chord to the passed ID.
		//TODO maybe make a better validator some day.
		if (validChord(id))
		{
			mCurrentChord = id;
		}
		else
		{
			if (mChords.size() > 0)
			{
				mCurrentChord = 0;
			}
			else
			{
				mCurrentChord = -1;
			}
		}
	}
	
	public int getCurrentChord()
	{
		return mCurrentChord;
	}
		
	public IntervalChord getChord(int id)
	{
		if (!validChord(id))
		{
			throw new ArrayIndexOutOfBoundsException(id + " out of bounds. Only " + getNumChords() + " avaliable.");
		}
		return mChords.get(id);
	}

	public int addChord(IntervalChord chord)
	{
		mChords.add(chord);
		return mChords.size() -1;
	}
	
	public void deleteChord(int id)
	{
		//Removes a tuning from the list.
		if (validChord(id))
		{
			mChords.remove(id);
		}
	}
	
	public void updateChord(int id, IntervalChord chord)
	{
		if (validChord(id))
		{
			mChords.set(id,  chord);
		}
	}

	//Functions pertaining to tunings.
	public int getNumTunings()
	{
		return mTunings.size();
	}
	
	public boolean validTuning(int id)
	{
		if (id >= 0 && id < mTunings.size())
		{
			return true;
		}
		return false;
	}
	
	public void setCurrentTuning(int id)
	{
		//Sets current tuning to the passed ID.
		//TODO maybe make a better validator some day.
		if (validTuning(id))
		{
			mCurrentTuning = id;
		}
		else
		{
			if (mTunings.size() > 0)
			{
				mCurrentTuning = 0;
			}
			else
			{
				mCurrentTuning = -1;
			}
		}
	}
	
	public int getCurrentTuning()
	{
		return mCurrentTuning;
	}
	
	public ToneChord getTuning(int id)
	{
		if (!validTuning(id))
		{
			throw new ArrayIndexOutOfBoundsException(id + " out of bounds. Only " + getNumTunings() + " avaliable.");
		}
		return mTunings.get(id);
	}
	
	public int addTuning(ToneChord tuning)
	{
		mTunings.add(tuning);
		return mTunings.size() -1;
	}
	
	public void deleteTuning(int id)
	{
		//Removes a tuning from the list.
		if (validTuning(id))
		{
			mTunings.remove(id);
		}
	}
	
	public void updateTuning(int id, ToneChord tuning)
	{
		if (validTuning(id))
		{
			mTunings.set(id,  tuning);
		}
	}
	
	//Data serialization and deserialization routines.
	public void serialize()
	{
		if (mTunings.size() == 0 )
		{
			System.out.println("No tunings to write.");
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		
		try
		{
            String jsonString = objectMapper.writeValueAsString(mTunings);
            System.out.println(jsonString); 			
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
	}
	
}
