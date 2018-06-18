package resources;

import java.awt.Image;
import java.awt.Toolkit;


public class ResourceLoader
{
	static ResourceLoader rl = new ResourceLoader();
	
	/**
	 * Constructs on ImageIcon from fileName
	 * 
	 * @param fileName : name of image file
	 */
	public static Image getImage(String fileName)
	{
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource(fileName));
	}
}
