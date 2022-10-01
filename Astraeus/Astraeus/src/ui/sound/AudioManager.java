package ui.sound;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import engine.Settings;
import ui.sound.music.Song;
import ui.sound.sfx.SoundTime;

public class AudioManager 
{
	public final static float MUSIC_SPECIAL_CHANCE = .01f;
	public final static float MUSIC_HIDDEN_CHANCE = .002f;
	
	static Song gameMusic;
	private static ArrayList<SoundTime> delayedSounds;
	
	
	/************** Setup **************/
	
	public static void loadSFX() throws SlickException 
	{
		Sounds.loadSFX();
		delayedSounds = new ArrayList<SoundTime>();
	}
	
	public static void loadMusic() throws SlickException 
	{
		Sounds.loadSongList();
		setRandomGameMusic();
		playGameMusic();
	}
	
	public static void leave()
	{
		delayedSounds.clear();
		gameMusic = null;
	}
	
	/************** Music **************/

	public static Song getGameMusic()
	{
		return gameMusic;
	}
	
	public static void setRandomGameMusic() throws SlickException
	{		
		gameMusic = Sounds.getRandomSong();
	}
	
	public static void playGameMusic() throws SlickException
	{
		if(Settings.musicOn) 
		{
			if(gameMusic == null)
			{
				setRandomGameMusic();
			}
			
			if(!gameMusic.isLoaded())
			{
				gameMusic.loadMusic();
			}			
		}
	}
	
	public static void setMusicVolume(float volume)
	{
		try 
		{
			if(getGameMusic() != null && getGameMusic().getMusic() != null)
			{
				getGameMusic().getMusic().setVolume(volume);
			}
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public static void update()
	{
		for(int i = 0; i < delayedSounds.size(); i++)
		{
			delayedSounds.get(i).update();
			
			if(delayedSounds.get(i).isDone())
			{
				delayedSounds.remove(i);
				i--;
			}
		}
		
		
	}
	/************** SFX **************/
	
	public static void addDelayedSound(SoundTime st)
	{
		delayedSounds.add(st);
	}

}
