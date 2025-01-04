package choordinates;

/*
 * An abstract concept of a note.
 */
public abstract class AbstractNote {
	protected int mID = -1;   //A number from 0-6 representing A B C D E F G
	protected int mSharp = 0; //A positive or negative number representing how many semitones to move the named note.
	
	public abstract String getName();
	public abstract int getOctaveSemitone();
	public abstract int getSemitone();
	public abstract boolean parse(String note);
	public abstract boolean isNote(AbstractNote note);

	public AbstractNote() {};
	
	public void setID(int id)
	{
		if (id < 0 || id > 6)
		{
			throw new IllegalArgumentException("Note ID valid range 0-11");
		}
		mID = id;
	}
	
	//Setters and getters for Jackson JSON interface.
	public void setSharp(int sharpness)
	{
		mSharp = sharpness;
	}
	
	public int  getID()
	{
		return mID;
	}
	
	public int getSharp()
	{
		return mSharp;
	}
	
	
}