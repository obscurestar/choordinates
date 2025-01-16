package choordinates;

import javax.swing.ImageIcon;
import java.awt.Rectangle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Preferences {
	@JsonIgnore
	private ImageIcon mIcon = new ImageIcon("choordinates64.png");

	@JsonProperty("left_handed")
	private boolean mLefty = false;
	@JsonProperty("neck_frets")
	private int mNeckFrets = 24;
	@JsonProperty("panel_frets")
	private int mPanelFrets = 6;

	@JsonProperty("main_bounds")
	private int[] mMainRect;
	@JsonProperty("tuning_bounds")
	private int[] mTuningRect;
	@JsonProperty("chords_bounds")
	private int[] mChordRect;
	@JsonProperty("preferences_bounds")
	private int[] mPrefRect;
	
	@JsonIgnore
	public int[] getMainRect()
	{
		return mMainRect;
	}
	
	@JsonIgnore
	public int[] getTuningRect()
	{
		return mTuningRect;
	}
	
	@JsonIgnore
	public int[] getChordRect()
	{
		return mChordRect;
	}
	
	@JsonIgnore
	public int[] getPrefRect()
	{
		return mPrefRect;
	}
	
	@JsonIgnore
	public int getNeckLength()
	{
		return mNeckFrets;
	}
	
	@JsonIgnore
	public int getPanelLength()
	{
		return mPanelFrets;
	}
	
	@JsonIgnore
	public boolean getLefty()
	{
		return mLefty;
	}
	
	@JsonIgnore
	public void setMainRect( Rectangle rect )
	{
		mMainRect = new int[] { rect.x, rect.y, rect.width, rect.height };
	}
	
	@JsonIgnore
	public void setTuningRect( Rectangle rect )
	{
		mTuningRect = new int[] { rect.x, rect.y, rect.width, rect.height };
	}
	
	@JsonIgnore
	public void setChordRect( Rectangle rect )
	{
		mChordRect = new int[] { rect.x, rect.y, rect.width, rect.height };
	}
	
	@JsonIgnore
	public void setPrefRect( Rectangle rect )
	{
		mPrefRect = new int[] { rect.x, rect.y, rect.width, rect.height };
	}

	@JsonIgnore
	public void setNeckLength(int frets)
	{
		mNeckFrets = frets;
	}
	
	@JsonIgnore
	public void setPanelLength(int frets)
	{
		mPanelFrets = frets;
	}

	@JsonIgnore
	public void setLefty( boolean left_handed )
	{
		mLefty = left_handed;
	}
	
	@JsonIgnore
	public ImageIcon getIcon()
	{
		return mIcon;
	}
	
	Preferences()
	{
		mMainRect = new int[] {100, 100, 499, 449};
		mTuningRect = new int[] {180, 180, 320, 211};
		mChordRect = new int[] {150, 150, 490, 258};
		mPrefRect = new int[] {100, 100, 187, 261};
	}
}
