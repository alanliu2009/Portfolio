package ui.display.message;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import engine.Settings;
import engine.Utility;
import engine.states.Game;
import objects.GameObject;
import objects.entity.unit.Unit;
import ui.display.Fonts;

public class ObjectMessage extends Message
{
	GameObject owner;

	public ObjectMessage(GameObject u, String message)
	{
		super(message);
		owner = u;
	}
	
	public ObjectMessage(GameObject u, String message, int duration)
	{
		super(message, duration);
		owner = u;
	}
	
	public ObjectMessage(GameObject u, String message, int duration , Color color)
	{
		super(message, duration, color);
		owner = u;
	}
	
	public void render(Graphics g)
	{
		// Do not show messages if they are hidden
		if(owner instanceof Unit)
		{
			if(!Settings.showPlayerOneInfo && ((Unit)owner).getPlayer() == Game.getPlayerOne())
			{
				return;
			}
			if(!Settings.showPlayerTwoInfo && ((Unit)owner).getPlayer() == Game.getPlayerTwo())
			{
				return;
			}
		}
		
		
		Font f = Fonts.ocr14;
		
		g.setFont(f);
		
		if(color != null)
		{
			g.setColor(color);
		}
		else
		{
			g.setColor(Color.gray);
		}

		float x = owner.getCenterX();
		float y = owner.getY() - id * 18 - 30;
		
		Utility.drawStringCenterTop(g, f, message, x, y);
	}
	

}
