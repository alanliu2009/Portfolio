package components;

import org.newdawn.slick.Color;

import engine.Values;

public enum DamageType 
{
	ENERGY, 		// +20% to shields
	KINETIC, 		// +20% to plating
	EXPLOSIVE,		// +20% to hull
	PIERCE,			// +20% to plating and ignores block
	TRUE,			// No bonuses 
	SHIELDS_ONLY,	// Ignores other defenses and only damages shields
	PLATING_ONLY,	// Ignores other defenses and only damages plating
	STRUCTURE_ONLY,	// Ignores other defenses and only damages hull
	NONE;

	public static float MOD_BASE = 1.2f;
	public static float MOD_ENHANCED = MOD_BASE * 2;
	
	public float getDamageMultiplier()
	{
		if(this == ENERGY || this == KINETIC || this == PIERCE)
		{
			return 1.2f;
		}
		else if(this == EXPLOSIVE)
		{
			return 1.1f;
		}
		else if(this == TRUE || this == STRUCTURE_ONLY)
		{
			return 1f;
		}
		else
		{
			return 0f;
		}
	}
	
	public boolean isType(DamageType type)
	{
		if(type == PIERCE && isPierce())
		{
			return true;
		}
		else if(type == EXPLOSIVE && isExplosive())
		{
			return true;
		}
		else if(type == ENERGY && isEnergy())
		{
			return true;
		}
		else if(type == KINETIC && isKinetic())
		{
			return true;
		}
		else if(type == TRUE && isTrue())
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public boolean isExplosive()
	{
		return this == EXPLOSIVE;
	}
	
	public boolean isEnergy()
	{
		return this == ENERGY;
	}
	
	public boolean isKinetic()
	{
		return this == KINETIC || this == PIERCE;
	}
	
	public boolean isPierce()
	{
		return this == PIERCE;
	}

	public boolean isTrue()
	{
		return this == TRUE;
	}
	
	public boolean cannotDamageShields()
	{
		return this == STRUCTURE_ONLY || this == PLATING_ONLY;
	}
	
	public boolean cannotDamagePlating()
	{
		return this == STRUCTURE_ONLY || this == SHIELDS_ONLY;
	}
	
	public boolean cannotDamageStructure()
	{
		return this == PLATING_ONLY || this == SHIELDS_ONLY;
	}
	
	public Color getColor()
	{
		if(isEnergy())
		{
			return Values.COLOR_SHIELD;
		}
		else if(isKinetic())
		{
			return Values.COLOR_PLATING;
		}
		else if(isExplosive())
		{
			return Values.COLOR_STRUCTURE;
		}
		else
		{
			return Values.COLOR_UTILITY;
		}
	}
}
