package choordinates;

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
	
	public BrowserWindow(String filename, String url)
	{
		//Attempts to open remote file.  On failure, attempts to open local.
		if( !launchBrowser(url) )
		{
			Choordinates.alert("Browser disallowed", "Local helpfile at\n" + System.getProperty("user.dir") + "choordinates.html");
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
