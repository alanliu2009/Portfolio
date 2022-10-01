package teams.starter.blue.units;

import components.upgrade.Shield;
import components.weapon.energy.SmallLaser;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.starter.blue.Blue;
import teams.starter.blue.BlueUnit;

public class Guardian extends BlueUnit 
{
	
	public Guardian(Blue p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.HEAVY);
		setStyle(Style.WEDGE);
		addWeapon(new SmallLaser(this));
		addWeapon(new SmallLaser(this));
		addUpgrade(new Shield(this));			
		addUpgrade(new Shield(this));			
		addUpgrade(new Shield(this));			
		addUpgrade(new Shield(this));			
	}

	public void action() 
	{
		skirmish(getWeaponOne());
		skirmish(getWeaponTwo());
	}
	
	

}
