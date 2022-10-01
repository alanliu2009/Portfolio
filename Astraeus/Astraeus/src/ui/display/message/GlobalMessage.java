package ui.display.message;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.Utility;
import ui.display.Fonts;
import ui.display.hud.Hud;

public class GlobalMessage extends Message
{
	public GlobalMessage(String message)
	{
		super(message);
	}
	
	public GlobalMessage(String message, int duration)
	{
		super(message, duration);
	}
	
	public GlobalMessage(String message, int duration , Color color)
	{
		super(message, duration, color);
	}
	
	public void render(Graphics g)
	{
		Font f = Fonts.ocr20;
		
		g.setFont(f);
		
		if(color != null)
		{
			g.setColor(color);
		}
		else
		{
			int alpha = (int) Math.pow((255 * this.percentComplete()), 1.3);
			g.setColor(new Color(255, 255, 255, alpha));
		}

		float x = Main.getScreenWidth() / 2;
		float y = Main.getScreenHeight() - (id + 2) * 20 - (Main.getScreenHeight() * Hud.BAR_HEIGHT_PERCENT);
		
		Utility.drawStringCenterTop(g, f, message, x, y);
	}
	

}
