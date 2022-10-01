package teams.starter.green.units;


import components.upgrade.NanobotHull;
import components.upgrade.Structure;
import components.weapon.explosive.ShortRangeMissile;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.starter.green.Green;
import teams.starter.green.GreenUnit;

public class Battleship extends GreenUnit 
{
	
	public Battleship(Green p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.HEAVY);
		setStyle(Style.WEDGE);
		addWeapon(new ShortRangeMissile(this));
		addWeapon(new ShortRangeMissile(this));
		addUpgrade(new Structure(this));
		addUpgrade(new Structure(this));
		addUpgrade(new NanobotHull(this));
		addUpgrade(new NanobotHull(this));

	}

	public void action() 
	{
		skirmish(getWeaponOne());
		skirmish(getWeaponTwo());
	}

}
