package teams.starter.green.units;


import components.upgrade.OptimizedAlgorithms;
import components.weapon.explosive.AntimatterMissile;
import components.weapon.explosive.LongRangeMissile;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.starter.green.Green;
import teams.starter.green.GreenUnit;

public class Dreadnought extends GreenUnit 
{
	
	public Dreadnought(Green p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.HEAVY);
		setStyle(Style.BOXY);
		addWeapon(new AntimatterMissile(this));
		addWeapon(new LongRangeMissile(this));
		addUpgrade(new OptimizedAlgorithms(this));

	}

	public void action() 
	{
		skirmish(getWeaponOne());
		skirmish(getWeaponTwo());
	}

}
