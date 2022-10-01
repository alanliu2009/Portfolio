package ui.display.healthbar;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Settings;
import engine.Values;
import objects.entity.unit.Unit;

public class UnitHealthbar extends Healthbar
{
	private Unit owner;
	
	public UnitHealthbar(Unit u) 
	{
		super(u);
		owner = u;
	}
	


	public void render(Graphics g)
	{
		super.render(g);
		
		if(showingUsebar())
		{
			if(owner.hasWeaponOne() && owner.getWeaponOne().inUse())
			{
				renderUseBarOne(g);
			}
			else if(owner.hasWeaponOne() && owner.getWeaponOne().onCooldown())
			{
				renderCooldownBarOne(g);
			}
			
			
			if(owner.hasWeaponTwo() && owner.getWeaponTwo().inUse())
			{
				renderUseBarTwo(g);
			}
			else if(owner.hasWeaponTwo() && owner.getWeaponTwo().onCooldown())
			{
				renderCooldownBarTwo(g);
			}
		}
		
		if(showingCapacitybar())
		{
			renderCapacitybar(g);
		}
	}
	
	public boolean showingCapacitybar()
	{
		if(!Settings.showCapacitybars || owner.isDead() || owner.getCapacity() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean showingHealthbar()
	{
		if(!Settings.showUnitHealthbars || owner.isDead())
		{
			return false;
		}
		else if(Settings.smartUnitHealthbars && owner.isUndamaged())
		{
			return false;
		}
		else
		{
			return true;
		}
	}	
	
	public boolean showingUsebar()
	{
		if(!Settings.showUsebars || owner.isDead())
		{
			return false;
		}
		else if(Settings.smartUsebars && owner.hasWeaponOne() && owner.getWeaponOne().getUseTime() + owner.getWeaponOne().getCooldown() >= MIN_USE_DISPLAY_TIME)
		{
			return true;
		}
		else if(Settings.smartUsebars && owner.hasWeaponTwo() && owner.getWeaponTwo().getUseTime() + owner.getWeaponOne().getCooldown() >= MIN_USE_DISPLAY_TIME)
		{
			return true;
		}
		else if(Settings.smartUsebars)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void renderCapacitybar(Graphics g)
	{
		g.setColor(new Color(105, 70, 40));
		g.fillRect(x, y - 5, w, 3);
		
		g.setColor(Values.COLOR_CAPACITY);
		g.fillRect(x, y - 5, owner.getCargoPercent() * w, 3);	
	}
	
	
	public void renderUseBarOne(Graphics g)
	{
		g.setColor(new Color(70, 70, 70));
		g.fillRect(x, y + h + 1, w, 2);
		
		g.setColor(new Color(180, 180, 180));
		g.fillRect(x, y + h + 1, (float) owner.getWeaponOne().getUseProgress() / (float) owner.getWeaponOne().getUseTime() * w, 2);	
	}
	
	public void renderUseBarTwo(Graphics g)
	{
		g.setColor(new Color(70, 70, 70));
		g.fillRect(x, y + h + 4, w, 2);
		
		g.setColor(new Color(180, 180, 180));
		g.fillRect(x, y + h + 4, (float) owner.getWeaponTwo().getUseProgress() / (float) owner.getWeaponTwo().getUseTime() * w, 2);	
	}
	
	public void renderCooldownBarOne(Graphics g)
	{
		g.setColor(new Color(20, 70, 20));
		g.fillRect(x, y + h + 1, w, 2);
		
		g.setColor(new Color(50, 180, 50));
		g.fillRect(x, y + h + 1, (float) owner.getWeaponOne().getCooldownProgress() / (float) owner.getWeaponOne().getCooldown() * w, 2);	
	}
	
	public void renderCooldownBarTwo(Graphics g)
	{
		g.setColor(new Color(20, 70, 20));
		g.fillRect(x, y + h + 4, w, 2);
		
		g.setColor(new Color(50, 180, 50));
		g.fillRect(x, y + h + 4, (float) owner.getWeaponTwo().getCooldownProgress() / (float) owner.getWeaponTwo().getCooldown() * w, 2);	
	}

	
	public float getHeight()
	{
		return owner.getFrame().getImageSize() / 12 + 3;
	}
	
	public float getY()
	{
		return owner.getY() - h - 6;
	}
}
