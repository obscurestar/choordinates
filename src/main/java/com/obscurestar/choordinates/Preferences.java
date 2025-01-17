package com.obscurestar.choordinates;

import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Preferences {
	@JsonIgnore
	private static ImageIcon mIcon;
	@JsonIgnore
	private String mFilename;

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
	
	@JsonIgnore
	public void loadIcon( )
	{
		String filename = "choordinates64.png";
		mIcon = null;
		boolean got_stream = true;
		
        ClassLoader classloader = ClassLoader.getSystemClassLoader();

        // Load the image from the JAR file
        InputStream input_stream = null;
        try
        {
        	input_stream = classloader.getResourceAsStream( filename );
        }
        catch ( NullPointerException e )
        {
        }
        
        if (input_stream == null) {
        	got_stream = false;
        }
        
        if (got_stream) //Found it in the jar.
        {
	        try
	        {
	        	BufferedImage image = ImageIO.read(input_stream);
	        	mIcon = new ImageIcon(image);
	        }
	        catch ( IOException e )
	        {
	        	mIcon = null;
	        }
        }
        
        if (mIcon == null || mIcon.getImage() == null)
        {
        	//Try getting from directory instead. (development mode)
            mIcon = new ImageIcon( "resources/" + filename );
        }
        
        if (mIcon == null || mIcon.getImage() == null)
        {
        	System.out.println("You failed! You get NOTHING!");
            mIcon = new ImageIcon( );
        }
      
        try
        {
        	//Suppress compiler whinging.
        	input_stream.close();
        }
        catch (IOException e){}
	}
	
	Preferences()
	{
		mMainRect = new int[] {100, 100, 499, 449};
		mTuningRect = new int[] {180, 180, 320, 211};
		mChordRect = new int[] {150, 150, 490, 258};
		mPrefRect = new int[] {100, 100, 187, 261};
	}
}
