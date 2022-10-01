package animations.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import animations.Animation;
import engine.Utility;
import objects.entity.Entity;
import ui.display.Fonts;

public class ShatteredIcon extends Animation
{
	//Image image;

	public ShatteredIcon(Entity e) 
	{
		super(e.getX() + e.getSize() / 2, e.getY() - e.getSize(), 1);
	}
	
	public void update()
	{
		super.update();
	}

	public void render(Graphics g) 
	{	
		g.setColor(new Color(50, 50, 50));
		g.fillOval(x - 11, y - 10, 24, 24);

		g.setColor(new Color(150, 150, 150));
		Utility.drawStringCenterCenter(g, Fonts.ocr28, "X", x, y);

//		g.setColor(outer);
//		g.fillOval(displayX, displayY, displaySize, displaySize);
	}
}