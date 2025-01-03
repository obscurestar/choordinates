package choordinates;

public class IntervalNote extends AbstractNote{

	public String getName()
	{
		return "";
	}
	
	public int getAbsoluteSemitone()
	{
		return 0;
	}
	
	public int getSemitone()
	{
		return 0;
	}
	
	public boolean parse(String note)
	{
		return false;
	}
	
	public boolean isNote(AbstractNote note)
	{
		return false;
	}
}
