package teams.starter.red.units;


import components.upgrade.Plating;
import components.weapon.kinetic.Railgun;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.starter.red.Red;
import teams.starter.red.RedUnit;

public class Sniper extends RedUnit 
{
	
	public Sniper(Red p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.DAGGER);
		addWeapon(new Railgun(this));
		addUpgrade(new Plating(this));

	}

	public void action() 
	{
		skirmish(getWeaponOne());
	}
}
