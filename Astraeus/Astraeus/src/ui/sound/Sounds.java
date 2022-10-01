package ui.sound;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import engine.Utility;
import ui.sound.music.Song;
import ui.sound.sfx.SmartSound;

public class Sounds 
{
	public static SmartSound laser;
	public static SmartSound laserSmall;

	public static SmartSound mg;
	public static SmartSound blast;
	public static SmartSound heal;
	public static SmartSound boom;
	public static SmartSound mining;
	public static SmartSound boost;
	public static SmartSound missileFire;

	// Abilities
	public static SmartSound aegis;
	public static SmartSound emp;
	
	static ArrayList<Song> songs;

	public static void loadGameMusicFile(Song m) throws SlickException
	{
		m.loadMusic();
	}
	
	public static Song getRandomSong()
	{
		return songs.get(Utility.random(0, songs.size()-1));	
	}
	
	static void loadSFX() throws SlickException 
	{
		Sounds.boom = new SmartSound("general/boom", 3);

		Sounds.laserSmall = new SmartSound("energy/laser_small", 4);
		Sounds.laser = new SmartSound("energy/laser", 3);
		
		Sounds.mining = new SmartSound("kinetic/mining", 5);
		Sounds.mg = new SmartSound("kinetic/mg");
		
		Sounds.aegis = new SmartSound("utility/aegis");
		Sounds.boost = new SmartSound("utility/boost");

		Sounds.missileFire = new SmartSound("explosive/missile_fire");
		
	}
	
	static void loadSongList() throws SlickException 
	{		
		String path = "res/music/";
		
		songs = new ArrayList<Song>();
		songs.add(new Song("Lindsey Stirling", "Between Twilight", path + "between_twilight.ogg"));		
		songs.add(new Song("Lindsey Stirling", "The Arena", path + "the_arena.ogg"));		
		songs.add(new Song("Lindsey Stirling", "Heist", path + "heist.ogg"));
		songs.add(new Song("Lindsey Stirling", "The Pheonix", path + "pheonix.ogg"));
		songs.add(new Song("Lindsey Stirling", "First Light", path + "first_light.ogg"));
		songs.add(new Song("Lindsey Stirling", "Artemis", path + "artemis.ogg"));

	}


	
}
