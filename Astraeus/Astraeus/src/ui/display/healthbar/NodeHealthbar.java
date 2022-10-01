package ui.display.healthbar;

import org.newdawn.slick.Graphics;

import engine.Settings;
import objects.entity.Entity;

public class NodeHealthbar extends Healthbar
{
	public NodeHealthbar(Entity e) 
	{
		super(e);
	}

	public boolean showingHealthbar()
	{
		if(!Settings.showNodeHealthbars || owner.isDead())
		{
			return false;
		}
		else if(Settings.smartNodeHealthbars && owner.isUndamaged())
		{
			return false;
		}
		else
		{
			return true;
		}
	}	
	
	public void render(Graphics g)
	{
		super.render(g);
	}
	
	public float getHeight()
	{
		return owner.getHeight() / 18 + 5;
	}
	
	public float getY()
	{
		return owner.getY() - h;
	}
}
