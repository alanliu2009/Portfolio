package ui.display.message;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

abstract public class Message 
{
	private int duration;
	private int timeLeft;
	protected String message;
	protected Color color;
	protected int id;
	
	public Message(String message)
	{
		this.message = message;
		timeLeft = 0;
		duration = 0;
	}
	
	public Message(String message, int duration)
	{
		this.message = message;
		timeLeft = duration;
		this.duration = duration;
	}
	
	public Message(String message, int duration, Color color)
	{
		this.message = message;
		timeLeft = duration;
		this.duration = duration;
		this.color = color;
	}
	
	public void update()
	{
		if(!isDone())
		{
			timeLeft--;
		}
	}

	public boolean isDone()
	{
		return timeLeft == 0;
	}
	
	public float percentComplete()
	{
		return (float) timeLeft / (float) duration;
	}
	
	public void updateID(int id)
	{
		this.id = id;
	}
	
	abstract public void render(Graphics g);
}
