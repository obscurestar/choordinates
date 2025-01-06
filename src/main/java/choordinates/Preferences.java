package choordinates;

import javax.swing.ImageIcon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Preferences {
	@JsonIgnore
	private ImageIcon mIcon = new ImageIcon("choordinates64.png");
	private boolean mLefty;
	
	@JsonProperty("left_handed")
	public boolean getLefty()
	{
		return mLefty;
	}
	
	@JsonProperty("right_handed")
	public void setLefty( boolean left_handed )
	{
		mLefty = left_handed;
	}
	
	@JsonIgnore
	public ImageIcon getIcon()
	{
		return mIcon;
	}
}
