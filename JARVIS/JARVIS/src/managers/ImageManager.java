package managers;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ImageManager{
	final public static HashMap<String, Image> Images = new HashMap<String, Image>();
	
	public static int getSize() { return Images.size(); }
	public static HashMap<String, Image> getImageHash(){ return Images; }
	
	public static Image getPlaceholder() { return Images.get("placeholder"); }
	
	public static Image getImage(String name) {
		Image i = Images.get(name);
		
		if(i == null) return Images.get("placeholder"); 
		else return i;
	}
	public static SpriteSheet getSpriteSheet(String name, int width, int height) {
		return new SpriteSheet(getImage(name), width, height);
	}
}