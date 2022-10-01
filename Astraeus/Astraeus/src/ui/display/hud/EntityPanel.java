package ui.display.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import components.Component;
import components.upgrade.Plating;
import components.upgrade.Shield;
import components.upgrade.Structure;
import components.weapon.Weapon;
import conditions.debuffs.Immobilized;
import conditions.debuffs.Slow;
import conditions.debuffs.Stop;
import engine.Main;
import engine.Utility;
import engine.Values;
import objects.entity.node.Asteroid;
import objects.entity.node.Derelict;
import objects.entity.node.Node;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import territory.TerritoryManager;

public class EntityPanel extends HudElement
{
	Unit unit;
	Node node;
	int indent;
	int pos;
	int spacing;
	
	public EntityPanel(float x, float y, float w, float h)
	{
		super(x, y, w, h);
	}
	
	public boolean hasUnit()
	{
		return unit != null && unit.isAlive();
	}
	
	public void setUnit(Unit u)
	{
		this.unit = u;
		this.node = null;
	}
	
	public boolean hasNode()
	{
		return node != null && node.isAlive();
	}
	
	public void setNode(Node n)
	{
		this.unit = null;
		this.node = n;
	}
	
	public void render(Graphics g)
	{		
		super.render(g);

		if(hasUnit())
		{			
			setFontsAndSpacing();

			drawTitle(g);		
			drawAttribute(g);		
			drawWeapons(g);
			drawUpgrades(g);
			drawDefense(g);
		}
		
		if(hasNode()) 
		{
			setFontsAndSpacing();
			drawNodeTitle(g);
			drawNodeAttribute(g);
		}
	}
	
	public void setFontsAndSpacing()
	{
//		if(Main.getScreenWidth() > 1920)
//		{
//			bigFont = Fonts.ocr20;      BIG FONT here is HUD's MEDIUM FONT
//			smallFont = Fonts.ocr16;	SMALL FONT IS THE SAME
//			indent = 20;
//			spacing = 16;				USE THIS TO REVERSE ENGINEER SCALING
//		}
//		else if(Main.getScreenWidth() == 1920)
//		{
//			bigFont = Fonts.ocr16;
//			smallFont = Fonts.ocr14;
//			indent = 14;
//			spacing = 14;
//		}
//		else
//		{
//			bigFont = Fonts.ocr14;
//			smallFont = Fonts.ocr12;
//			indent = 12;
//			spacing = 12;
//		}
		
		if(Main.getScreenWidth() > 1920)
		{
			indent = 20;
			spacing = 16;				
		}
		else if(Main.getScreenWidth() == 1920)
		{
			indent = 14;
			spacing = 14;
		}
		else
		{
			indent = 12;
			spacing = 10;
		}
		
		pos = 3;

	}
	
	
	public void drawNodeTitle(Graphics g)
	{
		String message = "";
		if(node instanceof Asteroid)
		{
			g.setColor(TerritoryManager.getAsteroidColor().darker().darker());
			message = "Mine for Minerals";
		}
		else if(node instanceof Derelict)
		{
			g.setColor(TerritoryManager.getDerelictColor().darker().darker());
			message = "Mine for Scrap";
		}

		g.fillRect(x+2, y + indent - 4, w-4, (int) (spacing * 1.5 + 25));

		g.setFont(Hud.mediumFont);
		g.setColor(Color.white);
		g.drawString(node.getName(), x + indent, y + indent);
		

		g.setFont(Hud.smallFont);
		g.setColor(Color.gray);
		g.drawString(message, x + indent, y + indent + spacing + 9);
		pos++;
		
		
	}
	
	public void drawNodeAttribute(Graphics g)
	{
		if(node.getMaxShield() != 0)
		{
			g.setColor(Values.COLOR_SHIELD);
			g.drawString("Shields:   " + (int) node.getCurShield() + " / " + (int) node.getMaxShield(), x + indent, y + indent + spacing * pos++);
		} 
		if(node.getMaxPlating() != 0)
		{
			g.setColor(Values.COLOR_PLATING);
			g.drawString("Plating:   " + (int) node.getCurPlating() + " / " + (int)  node.getMaxPlating(), x + indent, y + indent + spacing * pos++);
		}
		
		g.setColor(Values.COLOR_STRUCTURE);
		g.drawString("Structure: " + (int) node.getCurStructure() + " / " + (int)  node.getMaxStructure(), x + indent, y + indent + spacing * pos++);
		
		pos++;
		
		if(node.getCurResources() != 0)
		{
			g.setColor(Values.COLOR_CAPACITY);
			if(node instanceof Asteroid)
			{
				g.drawString("Minerals:  " + (int) node.getCurResources() + " / " + (int)  node.getMaxResources(), x + indent, y + indent + spacing * pos++);
			}
			else
			{
				g.drawString("Scrap:  " + (int) node.getCurResources() + " / " + (int)  node.getMaxResources(), x + indent, y + indent + spacing * pos++);

			}
		}		
	}
	
	public void drawTitle(Graphics g)
	{
		g.setColor(unit.getColorSecondary().darker().darker());
		g.fillRect(x+2, y + indent - 4, w-4, (int) (spacing * 1.5 + 25));
		
		g.setFont(Hud.mediumFont);
		g.setColor(unit.getColorPrimary().brighter());
		g.drawString(unit.getPlayer().getName() + " " + unit.getName(), x + indent, y + indent);
		

		g.setFont(Hud.smallFont);

		g.setColor(Color.gray);
		g.drawString(unit.getFrame().toString() + " <" + unit.getValue() + ">", x + indent, y + indent + spacing + 9);
		pos++;
		
	//	//g.setColor(Color.white);
	//	Utility.drawStringCenterCenter(g, Hud.bigFont, ""+unit.getValue(), x + w *.9f, y + h * .165f);

		g.setFont(Hud.smallFont);
	}
	


	
	public void drawAttribute(Graphics g)
	{
		if(unit.getMaxShield() != 0)
		{
			g.setColor(Values.COLOR_SHIELD);
			g.drawString("Shields:   " + (int) unit.getCurShield() + " / " + (int) unit.getMaxShield(), x + indent, y + indent + spacing * pos++);
		} 
		if(unit.getMaxPlating() != 0)
		{
			g.setColor(Values.COLOR_PLATING);
			g.drawString("Plating:   " + (int) unit.getCurPlating() + " / " + (int)  unit.getMaxPlating(), x + indent, y + indent + spacing * pos++);
		}
		
			g.setColor(Values.COLOR_STRUCTURE);
			g.drawString("Structure: " + (int) unit.getCurStructure() + " / " + (int)  unit.getMaxStructure(), x + indent, y + indent + spacing * pos++);
		
		if(unit.getCapacity() != 0)
		{
			g.setColor(Values.COLOR_CAPACITY);
			g.drawString("Capacity:  " + (int) unit.getCargo() + " / " + (int)  unit.getCapacity(), x + indent, y + indent + spacing * pos++);
		}
		
		if(unit.getMaxSpeed() > 1 * Values.SPEED && !(unit instanceof BaseShip))
		{
			g.setColor(Values.COLOR_SPEED);
			g.drawString("Speed:     " + Math.round (unit.getCurSpeed() / Values.SPEED ) + " / " + Math.round (unit.getMaxSpeedBase() / Values.SPEED), x + indent, y + indent + spacing * pos++);
		}
		
	}
	
	public void drawWeapons(Graphics g)
	{
		pos++;
		if(unit.hasWeaponOne())
		{
			String weapon = unit.getWeaponOne().getName() + " ";
			for(int i = 0; i < unit.getWeaponOne().getSize(); i++)
			{
				weapon += "*";
			}
			
			Color c = unit.getWeaponOne().getDamageType().getColor();
			
			
//			if(unit.getWeaponOne().inUse())
//			{
//				c = Utility.blend(Color.white,  c);
//				c = Utility.blend(c, unit.getWeaponOne().getDamageType().getColor());
//
//			}
//			else 
				if(unit.getWeaponOne().onCooldown())
			{
				c = c.darker();
			}
			 
			
			 
			
			
			g.setColor(c);
			
			
			
			g.drawString(weapon, x + indent, y + indent + spacing * pos++);
		}
		
		if(unit.hasWeaponTwo())
		{
			String weapon = unit.getWeaponTwo().getName() + " ";
			for(int i = 0; i < unit.getWeaponTwo().getSize(); i++)
			{
				weapon += "*";
			}
			
			Color c = unit.getWeaponTwo().getDamageType().getColor();
			
//		
//			if(unit.getWeaponOne().inUse())
//			{
//				c = Utility.blend(Color.white,  c);
//				c = Utility.blend(c, unit.getWeaponOne().getDamageType().getColor());
//			}
//			else 
				if(unit.getWeaponTwo().onCooldown())
			{
				c = c.darker();
			}
			 
			
			g.setColor(c);
			
			g.drawString(weapon, x + indent, y + indent + spacing * pos++);
		}
		
	}
	
	public void drawDefense(Graphics g)
	{
		int defense = Math.round(100 * (1-unit.getDamageTakenMultiplier()));
		int dodge = Math.round(100 * (unit.getDodgeChance()));

		if(unit.getConditions().getTotalDamageTakenModifier() <= 1)
		{
			g.setColor(Color.white);
		}
		else
		{
			g.setColor(Color.gray);
		}
		
		Utility.drawStringCenterCenter(g, Hud.mediumFont, defense + "%", x + w *.8f, y + h * .46f);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Block", x + w *.8f, y + h * .56f);
		
		if(unit.getConditions().containsType(Slow.class) || unit.getConditions().containsType(Immobilized.class) || unit.getConditions().containsType(Stop.class))
		{
			g.setColor(Color.gray);
		}
		else
		{
			g.setColor(Color.white);
		}
		
		Utility.drawStringCenterCenter(g, Hud.mediumFont, dodge + "%", x + w *.8f, y + h * .70f);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Dodge", x + w *.8f, y + h * .80f);
	}
	
	public void drawUpgrades(Graphics g)
	{
		for(Component c : unit.getComponents())
		{
			if(c instanceof Weapon || c instanceof Shield || c instanceof Plating || c instanceof Structure)
			{
				continue;
			}
			else
			{
				g.setColor(new Color(200, 200, 200));
				g.drawString(c.getName() + " ", x + indent, y + indent + spacing * pos++);
			}
				
		}
	}
	
	
}
