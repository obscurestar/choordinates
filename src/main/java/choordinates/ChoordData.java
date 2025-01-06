package choordinates;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.IOException;

public class ChoordData {
	//TODO:  Probably a better filepath.
	@JsonIgnore
	private static final String mJsonFile ="choordinates.json";
	@JsonProperty("tunings")
	private ArrayList<ToneChord> mTunings = new ArrayList<>();
	@JsonProperty("current_tuning")
	private int mCurrentTuning;
	@JsonProperty("chords")
	private ArrayList<IntervalChord> mChords = new ArrayList<>();
	@JsonProperty("current_chord")
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
	@JsonIgnore
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
	
	@JsonProperty("current_chord")
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
	
	@JsonIgnore
	public int getCurrentChord()
	{
		return mCurrentChord;
	}
		
	@JsonIgnore
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
	@JsonIgnore
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
	
	@JsonProperty("current_tuning")
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
	
	@JsonIgnore
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
	public void write()
	{
		ObjectMapper objectMapper = new ObjectMapper();
				
		try
		{
            
            objectMapper.writeValue(new File(mJsonFile), this);
        }
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
			return;
		}
        catch (IOException e)
        {
            e.printStackTrace();
		}
	}
	
	//Tasty static initializer
	public static ChoordData read()
	{     
		//Read data into a ChoordData object
		//If the JSON fails to load for some reason,
		//will return the default empty container.
		ChoordData result = new ChoordData();
		
		try
	    {
	        // Create an ObjectMapper instance
	        ObjectMapper objectMapper = new ObjectMapper();
	                    
	        File jsonFile = new File(mJsonFile);
	        
	        //Don't care if doesn't exist.
	        if (jsonFile.exists()) 
            {	        
	        	// Read the JSON file and map it to a Java object
	        	result =  objectMapper.readValue(jsonFile, ChoordData.class);
            }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }   
		
		//Make sure currents are within bounds when file loaded.
		result.setCurrentTuning( result.getCurrentTuning() );
		result.setCurrentChord( result.getCurrentChord() );
		
		return result;
	}
	
}
