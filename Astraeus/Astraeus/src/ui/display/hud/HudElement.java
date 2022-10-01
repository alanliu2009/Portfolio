package ui.display.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class HudElement 
{
	protected float x;
	protected float y;
	protected float w;
	protected float h;
	
	private final Color COLOR_BORDER = new Color(50, 50, 50);
	private final Color COLOR_BACKGROUND = new Color(12, 12, 12);
	
	public HudElement(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void render(Graphics g)
	{
		int edge = 2;
		
		g.setLineWidth(edge);
		
		g.setColor(COLOR_BACKGROUND);
		g.fillRect(x,  y, w, h);
		
		g.setColor(COLOR_BORDER);
		g.drawRect(x+edge/2,  y, w-edge, h-edge);
		
		g.resetLineWidth();
	}

}
