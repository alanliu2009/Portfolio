package ui.sound.sfx;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class SmartSound 
{	
	private PositionalSound[] sounds;
	private String file;

	public SmartSound(String file)
	{
		sounds = new PositionalSound[1];
		this.file = file;
		load();
	}
	
	public SmartSound(String file, int size)
	{
		sounds = new PositionalSound[size];
		this.file = file;
		load();
	}

	//public void playOnce
	
	public void play(Point pos, float pitch, float volume) 
	{
		if(sounds.length == 1)
		{
		//	if(sounds[0].canPlay(pos))

//			if(!sounds[0].playing() && sounds[0].canPlay(pos))
			{
				sounds[0].play(pos, pitch, volume);
			}
		}
		else
		{
			int r = (int) (Math.random() * sounds.length);
			
			
	//		System.out.println(r + " " + sounds[r].playing() + " " + sounds[r].canPlay(pos));

			//if(sounds[r].canPlay(pos))

			//			if(!sounds[r].playing() && sounds[r].canPlay(pos))
			{
				sounds[r].play(pos, pitch, volume);
			}
		
		}
	}
	
	public void play(Point pos, float pitch)
	{
		play(pos, pitch, 1);
	}
	
	public void play(Point pos)
	{
		play(pos, 1, 1);
	}

	public void load()
	{
		try
		{
			// Load individual sounds
			if(sounds.length == 1)
			{
				sounds[0] = new PositionalSound("res/sfx/" + file + ".ogg");
				return;
			}
			
			// Load sets
			for(int i = 0; i < sounds.length; i++)
			{
				sounds[i] = new PositionalSound("res/sfx/" + file + i + ".ogg");
			}
		}	
		catch (SlickException e) 
		{
			e.printStackTrace();
		}


	}

}
