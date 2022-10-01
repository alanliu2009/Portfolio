package managers;

import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import core.Values;

public class SoundManager {
	final public static HashMap<String, Sound> Sounds = new HashMap<String, Sound>();
	
	private static Sound background;
	private static float pitch = 1f;
	private static float volume = 0.5f;
	
	public static int getSize() { return Sounds.size(); }
	public static HashMap<String, Sound> getSoundHash(){ return Sounds; }
	
	public static void playSound(String name) {
		try {
			Sound s = Sounds.get(name);
			if(!s.playing()) s.play(pitch, volume); // This way, the same sound effect will not be played over itself
		} catch(Exception e) { System.out.println("Failure in playing a sound"); }
	}
	
	public static void playBackgroundMusic(String name) {
		if(background != null) background.stop(); // Stop the existing background music
		
		background = Sounds.get(name); // Get the background music desired
		
		System.out.println(pitch);
		System.out.println(volume);
		background.loop(1f,1f); // Loop the background music
	}
	
	public static void stopBackgroundMusic() {
		if(background != null) background.stop();
	}
	
	public static void increaseVolume() {
		System.out.println(volume);
		volume += 0.1;
		if (volume > 1) {
			volume = 1;
		}
	}
	public static void decreaseVolume() {
		volume -= 0.1;
		if (volume < 0) {
			volume = 0;
		}
	}
	
	public static float getVolume() {
		return volume;
	}
}