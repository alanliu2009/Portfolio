package teams.student.myTeam.units;

import components.upgrade.Plating;
import components.upgrade.Shield;
import components.weapon.energy.SmallLaser;
import components.weapon.kinetic.MachineGun;
import objects.entity.unit.Frame;
import objects.entity.unit.Style;
import teams.student.myTeam.MyTeam;
import teams.student.myTeam.MyTeamUnit;

public class Fighter extends MyTeamUnit 
{
	
	public Fighter(MyTeam p)  
	{
		super(p);
	}
	
	public void design()
	{
		setFrame(Frame.MEDIUM);
		setStyle(Style.DAGGER);
		addWeapon(new MachineGun(this));
		addWeapon(new SmallLaser(this));
		addUpgrade(new Plating(this));
		addUpgrade(new Shield(this));
	}

	public void action() 
	{
		skirmish(getWeaponOne());
		skirmish(getWeaponTwo());
	}	

}
