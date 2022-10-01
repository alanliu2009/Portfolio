package teams.student.theRock;

import org.newdawn.slick.Graphics;

import components.upgrade.Rangefinder;
import components.upgrade.TitanCore;
import components.weapon.Weapon;
import components.weapon.energy.Brightlance;
import components.weapon.kinetic.FlakBattery;
import conditions.Condition;
import conditions.Instant;
import engine.Values;
import objects.entity.unit.Frame;
import objects.entity.unit.Unit;
import player.Player;
import ui.display.Images;

public class TheRockUnit extends Unit
{	
	public final static float TURN_RATE_START = .012f;
	public final static float BASE_SHIP_X_POSITION = 7000;		
	public final static float BASE_SHIP_Y_POSITION = 150;

	private boolean skirmishing;
	public float turnRate;
	
	public TheRockUnit(Player p)
	{
		super(p);
		
		sheet = Images.imageBase;
		setImage();
		
		if (p.getTeam() == Values.TEAM_ONE_ID) 
		{
			this.x = -BASE_SHIP_X_POSITION - w/2;
			this.y = -BASE_SHIP_Y_POSITION - h/2;			
			theta = 0;
		} 
		else if (p.getTeam() == Values.TEAM_TWO_ID) 
		{
			this.x = BASE_SHIP_X_POSITION - w/2;
			this.y = BASE_SHIP_Y_POSITION - h/2;
			theta = 180;
		}
	
		setFrame(Frame.COLOSSAL);
		addWeapon(new FlakBattery(this));
		addWeapon(new Brightlance(this));
		addUpgrade(new TitanCore(this));
		
	//	addUpgrade
		
//		this.setHull(500000);
		
		turnRate = TURN_RATE_START;

	}	
	
	public void update() 
	{
		super.update();
	

		
		Unit u = getNearestEnemy();
		if(getDistance(this.getEnemyBase()) < 1000f) {
			u = this.getEnemyBase();
		}
		
		float trueTheta = getTheta();
		
		getWeaponOne().use(u);
		getWeaponTwo().use(u);
		
		if(team == 0 && getCenterX() > 0)
		{
			skirmishing = true;
		}
		if(team == 1 && getCenterX() < -0)
		{
			skirmishing = true;
		}
		
		if(skirmishing)
		{
			rotate(trueTheta + TURN_RATE_START);
		}

		changeSpeed(getAcceleration(), getTheta());
		this.x = u.getCenterX();
		this.y = u.getCenterY();
		
	}
	
	public void addCondition(Condition c)
	{		
		// Only adds direct damage!
		if(c instanceof Instant)
		{	
			super.addCondition(c);
		}
	}
	
	public void action() 
	{
		
	}

	public void draw(Graphics g) 
	{

	}

	public void design() 
	{
		
	}


}
