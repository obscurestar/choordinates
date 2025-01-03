package choordinates;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ChoordData {
	//TODO:  Probably a better filepath.
	private static final String filename ="choordinates.json";
	private ArrayList<Chord> mTunings = new ArrayList<>();
	private int mCurrentTuning;

	public ChoordData()
	{
		mCurrentTuning = -1;
	}
	
	public boolean validID(int id)
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
		if (validID(id))
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
	
	public Chord getTuning(int id)
	{
		if (!validID(id))
		{
			throw new ArrayIndexOutOfBoundsException(id + " out of bounds. Only " + getNumTunings() + " avaliable.");
		}
		return mTunings.get(id);
	}
	
	public int addTuning(Chord tuning)
	{
		mTunings.add(tuning);
		return mTunings.size() -1;
	}
	
	public void deleteTuning(int id)
	{
		//Removes a tuning from the list.
		if (validID(id))
		{
			mTunings.remove(id);
		}
	}
	
	public void updateTuning(int id, Chord tuning)
	{
		if (validID(id))
		{
			mTunings.set(id,  tuning);
		}
	}
	
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
	
	public int getNumTunings()
	{
		return mTunings.size();
	}
}
