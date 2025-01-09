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
	@JsonProperty("id")
	protected int mID = -1;   //A number from 0-6 representing A B C D E F G
	@JsonProperty("sharps")
	protected int mSharp = 0; //A positive or negative number representing how many semitones to move the named note.
	
	@JsonIgnore
	public abstract String getName();
	@JsonIgnore
	public abstract int getOctaveSemitone();
	@JsonIgnore
	public abstract int getSemitone();
	public abstract boolean isNote(AbstractNote note);

	@JsonCreator
	public AbstractNote() {};

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
	public int  getID()
	{
		return mID;
	}
	
	@JsonIgnore
	public int getSharp()
	{
		return mSharp;
	}
	
	
}