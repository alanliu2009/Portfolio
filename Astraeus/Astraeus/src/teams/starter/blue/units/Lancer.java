package teams.starter.blue.units;


import components.upgrade.Shield;
import components.weapon.energy.Brightlance;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.starter.blue.Blue;
import teams.starter.blue.BlueUnit;

public class Lancer extends BlueUnit 
{
	
	public Lancer(Blue p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.DAGGER);
		addWeapon(new Brightlance(this));
		addUpgrade(new Shield(this));
	}

	public void action() 
	{
		skirmish(getWeaponOne());
	}

}
