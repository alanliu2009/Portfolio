package animations;

import org.newdawn.slick.Graphics;

abstract public class Animation
{
	protected int ticks;
	protected int duration;
	protected float x;
	protected float y;
	protected float size;

	public Animation(float x, float y, int duration) 
	{
		this.x = x;
		this.y = y;
		this.duration = duration;
		this.ticks = 0;
	}

	public boolean isDone() 
	{
		// Negative values represent an object that never goes away
		if(duration < 0)
		{
			return false;
		}
		else
		{
			return ticks > duration;
		}

	}

	public void update()
	{
		if (!isDone() && ticks >= 0)
		{
			ticks++;
		}
	}
	
	public int getFadeAlphaValue()
	{
		return (int) (255.0f * percentLeft());
	}
	
	public float percentLeft()
	{
		return 1 - ((float) ticks) / ((float) duration);
	}
	
	public float percentComplete()
	{
		return ((float) ticks) / ((float) duration);
	}	
	
	public abstract void render(Graphics g);

}
