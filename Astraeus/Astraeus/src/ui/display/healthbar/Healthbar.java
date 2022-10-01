package ui.display.healthbar;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Settings;
import engine.Values;
import objects.entity.Entity;

public abstract class Healthbar 
{
	public static final int MIN_USE_DISPLAY_TIME = 45;
	
	protected Entity owner;
	protected float x;
	protected float y; 
	protected float w;
	protected float h;
	
	public Healthbar(Entity owner)
	{
		this.owner = owner;
	}
	
	public abstract boolean showingHealthbar();
	
	public void render(Graphics g)
	{
		w = owner.getSize();
		h = getHeight();
		x = owner.getX();
		y = getY();
		
		if (showingHealthbar()) 
		{
			renderHull(g);
			
			if(owner.getCurPlating() > 0)
			{
				renderPlating(g);
			}
			
			if(owner.getCurShield() > 0)
			{
				renderShield(g);
			}
		}
		
	}		
	
	abstract public float getY();
	abstract public float getHeight();
	
	public void renderHull(Graphics g)
	{
		g.setColor(new Color(70, 0, 0));
		g.fillRect(x, y, w, h);

		 // Gradient
		if(Settings.gradiantHealthbars)
		{
			for(int i = 0; i < h; i++)
			{
				float percent = 1 - (i / h) + .1f;
				int r = (int) (255 * percent);
				int gr =(int) (0 * percent);
				int b = (int) (0 * percent);
				
				g.setColor(new Color(r, gr, b));
				g.fillRect(x, y+i, owner.getCurStructure() / owner.getMaxStructure() * w, 1);	
			}
		}
		
		// Flat
		else
		{
			g.setColor(Values.COLOR_STRUCTURE);
			g.fillRect(x, y, owner.getCurStructure() / owner.getMaxStructure() * w, h);	
		}

	}

	public void renderShield(Graphics g)
	{		
		 // Gradient
		if(Settings.gradiantHealthbars)
		{
			for(int i = 0; i < h; i++)
			{
				float percent = 1 - (i / h) + .1f;
								
				int r = (int) (100 * percent);
				int gr =(int) (150 * percent);
				int b = (int) (255 * percent);
				
				
				if(!owner.isShieldRegenerating())
				{
					r = (int) (90 * percent);
					gr = (int) (120 * percent);
					b = (int) (120 * percent);
				}
				
				if(!owner.canShieldRecover())
				{
					if(i % 2 == 0)
					{
						r = (int) (90 * percent);
						gr = (int) (120 * percent);
						b = (int) (120 * percent);
					}
					else
					{
						r = (int) (200 * percent);
						gr = (int) (200 * percent);
						b = (int) (200 * percent);
					}
				}
				
				g.setColor(new Color(r, gr, b));
				g.fillRect(x, y+i, owner.getCurShield() / owner.getMaxShield() * w, 1);	
			}
		}
		
		// Flat
		else
		{
			g.setColor(Values.COLOR_SHIELD);
			g.fillRect(x, y, owner.getCurShield() / owner.getMaxShield() * w, h);
		}


	}
	
	public void renderPlating(Graphics g)
	{	
		 // Gradient
		if(Settings.gradiantHealthbars)
		{
			for(int i = 0; i < h; i++)
			{
				float percent = 1 - (i / h) + .1f;
				int r = (int) (220 * percent);
				int gr =(int) (200 * percent);
				int b = (int) (30 * percent);
				g.setColor(new Color(r, gr, b));
				g.fillRect(x, y+i, owner.getCurPlating() / owner.getMaxPlating() * w, 1);	
			}
		}
		
		// Flat
		else
		{
			g.setColor(Values.COLOR_PLATING);
			g.fillRect(x, y, owner.getCurPlating() / owner.getMaxPlating() * w, h);
		}
	}

}
