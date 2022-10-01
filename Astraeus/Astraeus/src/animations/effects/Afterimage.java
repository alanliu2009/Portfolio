package animations.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.Animation;
import engine.Settings;
import objects.entity.unit.Unit;

public class Afterimage extends Animation 
{
	public static final int FREQUENCY = 5;
	
	Unit u;

	private float rotation;
	int r;
	int gr;
	int br;
	float scale;
	Image image;

	public Afterimage( Unit u, float x, float y) 
	{
		super(x, y, 50);

		this.u = u;
		rotation = u.getTheta();
		r = u.getColorPrimary().getRed();
		gr = u.getColorPrimary().getGreen();
		br = u.getColorPrimary().getBlue();

		image = u.getImage().getScaledCopy(u.getScale());
		image.setCenterOfRotation(image.getWidth() / 2 * u.getScale(), image.getHeight() / 2 * u.getScale());
		image.setRotation(rotation);
	}

	public void render(Graphics g) 
	{
		if(Settings.showAnimations && ticks > 5)
		{
			int alpha = (int) (175 * (1 - ((float) ticks) / ((float) duration)));
			Color c = new Color(r, gr, br, alpha);
			g.drawImage(image,  x,  y, c);
		}
	}
}