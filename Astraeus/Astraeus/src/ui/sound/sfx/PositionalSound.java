package ui.sound.sfx;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Point;

import engine.Settings;
import engine.Utility;
import ui.display.Camera;

public class PositionalSound extends Sound 
{

	public PositionalSound(String filename) throws SlickException 
	{
		super(filename);
	}

	public void play(Point pos) 
	{
		if (Settings.sfxOn && Settings.soundVolume > 0) 
		{
			if (isInView(pos)) {
				play(1f, soundMultiplierZoom() *  soundMultiplierSpeed());
			}
			else if (isNearView(pos)) {
				play(1f, soundMultiplierDistance(pos) * soundMultiplierZoom() * soundMultiplierSpeed());
			}
		}
	}
	

	public void play(Point pos, float pitch, float volume) 
	{
		if(canPlay(pos))
		{
			//System.out.println(soundMultiplierSpeed());
			
			if (isInView(pos)) 
			{
				play(pitch, volume * soundMultiplierZoom() *  soundMultiplierSpeed());
			}
			else if (isNearView(pos)) 
			{
				play(pitch, volume * soundMultiplierDistance(pos) * soundMultiplierZoom() * soundMultiplierSpeed());
			}
		}
		
	}

	public boolean canPlay(Point pos)
	{
		return Settings.sfxOn && Settings.soundVolume > 0 && (isInView(pos) || isNearView(pos));
	}
	
	public boolean isInView(Point pos) 
	{
		return pos.getX() >= Camera.getX() - Camera.getViewWidth() / 2
				&& pos.getX() <= Camera.getX() + Camera.getViewWidth() / 2
				&& pos.getY() >= Camera.getY() - Camera.getViewWidth() / 2
				&& pos.getY() <= Camera.getY() + Camera.getViewHeight() / 2;
	}

	public boolean isNearView(Point pos) 
	{
		return pos.getX() >= Camera.getX() - Camera.getViewWidth()
				&& pos.getX() <= Camera.getX() + Camera.getViewWidth()
				&& pos.getY() >= Camera.getY() - Camera.getViewWidth()
				&& pos.getY() <= Camera.getY() + Camera.getViewHeight();
	}

	public float soundMultiplierDistance(Point pos) 
	{
		return 1 - (Utility.distance(pos.getX(), pos.getY(), Camera.getX(), Camera.getY())
				/ ((Camera.getViewWidth() + Camera.getViewHeight() / 2)));
	}

	public float soundMultiplierZoom() 
	{
		return Camera.getZoom() * 2;
	}
	
	public float soundMultiplierSpeed()
	{
		if(Settings.gameSpeed == 1)
		{
			return 1;
		}
		else if(Settings.gameSpeed == 2)
		{
			return .85f;
		}
		else if(Settings.gameSpeed == 5)
		{
			return .70f;
		}
		else if(Settings.gameSpeed == 10)
		{
			return .55f;
		}
		else 
		{
			return .40f;
		}
	}

}
