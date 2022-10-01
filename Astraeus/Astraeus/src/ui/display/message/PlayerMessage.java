package ui.display.message;

import org.newdawn.slick.Color;

public class PlayerMessage
{
	String message;
	Color color;
	
	public PlayerMessage()
	{
		this.message = "";
		this.color = Color.white;
	}
	
	
	public PlayerMessage(String message)
	{
		this.message = message;
		this.color = Color.white;
	}
	
	public PlayerMessage(String message, Color color)
	{
		this.message = message;
		this.color = color;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void set(String message, Color color)
	{
		setMessage(message);
		setColor(color);
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	

}
