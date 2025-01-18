package com.obscurestar.choordinates;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class BrowserWindow {
	public BrowserWindow(String url)
	{
		if ( !launchBrowser (url) )
		{
			Choordinates.alert( "Browser disallowed", "Unable to launch web browser to \n" + url );
		}
	}
	
	public BrowserWindow(String url, String filename)
	{
		//Attempts to open helpfile.  On fail just gives directory path.
		url = url.replaceAll(" ", "%20");

		if( !launchBrowser(url) )
		{
			Choordinates.alert("Helpfile at", filename);
		}
	}
	
	private boolean launchBrowser(String url)
	{
		try
		{
			if (Desktop.isDesktopSupported() 
				&& Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) 
			{
			    Desktop.getDesktop().browse(new URI(url));
			}	
		}
		catch (IOException | URISyntaxException e)
		{
			return false;
		}
		return true;
	}
}
