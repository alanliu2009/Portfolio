package animations.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.Animation;
import animations.AnimationManager;
import engine.Utility;
import ui.display.Images;

public class Boom extends Animation 
{
	public static final int MIN_DURATION = 6;
	public static final int MAX_DURATION = 12;

	public static final int MIN_CHILDREN = 2;
	public static final int MAX_CHILDREN = 3;
	public static final int MIN_SIZE = 8;
	
	Color color;
	Image image;

	public Boom(float x, float y, float size)
	{
		super(x, y, Utility.random(MIN_DURATION, MAX_DURATION));
		this.x = x - size / 2;
		this.y = y - size / 2;
		this.size = size;
		setImage();
		image = image.getScaledCopy((int) size, (int) size);
		image.setCenterOfRotation(image.getWidth() / 2, image.getHeight() / 2);
		image.setRotation(Utility.random(360));
		color = new Color(Utility.random(200, 255), Utility.random(200, 255), 200, Utility.random(175, 255));

	}
	
	protected void setImage()
	{
		image = Images.booms.getSprite(Utility.random(0, 3), 0);
	}
	
	
	public Boom makeChild()
	{
		// Determine the center and the child's size
		float centerX = x + size/2 + Utility.random(-size/2, size/2);
		float centerY = y + size/2 + Utility.random(-size/2, size/2);
		float newSize = size * Utility.random(.5, .8);
		
		return constructChild(centerX, centerY, newSize);
	}
	
	public Boom constructChild(float centerX, float centerY, float newSize)
	{
		return new Boom(centerX, centerY, newSize);
	}


	public void update() 
	{
		super.update();

		if (!isDone() && ticks == duration - 1 && size >= MIN_SIZE) 
		{
			int children = Utility.random(MIN_CHILDREN, MAX_CHILDREN);
			for(int i = 0; i < children; i++)
			{
				AnimationManager.add(makeChild());
			}
		}

	}

	public void render(Graphics g) 
	{
		if (image != null) 
		{
			//image.setCenterOfRotation(image.getWidth() / 2 * size, image.getHeight() / 2 * size);
			// image.setRotation(theta);
			
			image.draw(x,  y,  size,  size);
			//image.draw(x, y);
		}
	}
}