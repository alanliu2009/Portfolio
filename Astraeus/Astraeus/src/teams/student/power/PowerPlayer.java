package teams.student.power;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import objects.entity.unit.Unit;
import player.Boon;
import player.Player;
import teams.student.power.resources.ArmyManagement;
import teams.student.power.resources.MinerBuddies;
import teams.student.power.resources.Point;
import teams.student.power.resources.ResourceManagement;
import teams.student.power.resources.Strategy;

public class PowerPlayer extends Player
{	
	private Strategy strats;
	private ResourceManagement resManager;
	private ArmyManagement armyManager;
	
	public PowerPlayer(int team, Game g) 
	{
		super(team, g);
		
		setName("Power");
		setTeamImage("src/teams/student/power/myTeam.png");

		setColorPrimary(174, 114, 247);
		setColorSecondary(180, 120, 120);
		setColorAccent(247, 210, 247);
		
		setBoonOne(Boon.PILOT);		//MINER, SCOUT		
		setBoonTwo(Boon.EXPERT);
		
		strats = new Strategy(this);
	}

	public Strategy getStrats() { return strats; }
	public float getEnemyFleetX() { return armyManager.getEnemyFleetX(); }
	public float getEnemyFleetY() { return armyManager.getEnemyFleetY(); }
	public ArrayList<Unit> getEnemyFleet() { return armyManager.getEnemyFleet(); }
	
	//notes from testing: production of gatherer's vs miners based on how many free-floating resources in a safe area there are
	//we just produce way too many miners in general
	//
	
	public void opening()
	{
		resManager = new ResourceManagement(this);
		armyManager = new ArmyManagement(this);
	}
	
	public void strategy() 
	{	
		strats.resManagerUpdate();
		strats.armyManagerUpdate();
		strats.strategy();
	}
	
	public ResourceManagement getResourceManager() 
	{
		return resManager;
	}
	
	public ArmyManagement getArmyManager() 
	{
		return armyManager;
	}
			
	public void draw(Graphics g) 
	{
		if(resManager != null)
		{
			resManager.draw(g);
			ArrayList<MinerBuddies> minerGroups = resManager.getBuddies();
			for(MinerBuddies m : minerGroups) {

				m.draw(g);
			}
		}

		if(armyManager != null) {
			armyManager.draw(g);
		}

	}
}
