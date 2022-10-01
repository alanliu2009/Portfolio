package ui.sound.sfx;

import org.newdawn.slick.geom.Point;

public class SoundTime
{
	private SmartSound sound;
	private int timeLeft;
	private Point pos;
	private float pitch;
	private float volume;
	
	public SoundTime(SmartSound sound, int timeLeft, Point pos, float pitch, float volume)
	{
		this.sound = sound;
		this.timeLeft = timeLeft;
		this.pos = pos;
		this.pitch = pitch;
		this.volume = volume;
	}
	
	public SoundTime(SmartSound sound, int timeLeft, Point pos)
	{
		this.sound = sound;
		this.timeLeft = timeLeft;
		this.pos = pos;
		pitch = 1;
		volume = 1;
	}
	
	public void update()
	{
		if(timeLeft == 0)
		{
			sound.play(pos, pitch, volume);
		}
		
		timeLeft--;
	}
	
	public boolean isDone()
	{
		return timeLeft < 0;
	}

}
