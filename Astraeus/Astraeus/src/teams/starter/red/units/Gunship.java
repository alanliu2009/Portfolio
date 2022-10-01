package teams.starter.red.units;


import components.upgrade.Plating;
import components.weapon.kinetic.MachineGun;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.starter.red.Red;
import teams.starter.red.RedUnit;

public class Gunship extends RedUnit 
{
	
	public Gunship(Red p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.ARROW);
		addWeapon(new MachineGun(this));
		addWeapon(new MachineGun(this));
		addUpgrade(new Plating(this));
		addUpgrade(new Plating(this));
	}

	public void action() 
	{
		skirmish(getWeaponOne());
		skirmish(getWeaponTwo());
	}

}
