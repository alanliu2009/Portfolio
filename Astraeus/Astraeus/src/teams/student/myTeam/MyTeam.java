package teams.student.myTeam;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import player.Boon;
import player.Player;
import teams.student.myTeam.units.Fighter;
import teams.student.myTeam.units.Gatherer;
import teams.student.myTeam.units.Miner;

public class MyTeam extends Player
{	
	public MyTeam(int team, Game g) 
	{
		super(team, g);
		
		setName("My Team");
		setTeamImage("src/teams/student/myTeam/myTeam.png");

		setColorPrimary(150, 150, 150);
		setColorSecondary(255, 255, 255);
		setColorAccent(255, 255, 255);
		
		setBoonOne(Boon.MINER);				
		setBoonTwo(Boon.SNIPER);
	}
	
	public void opening()
	{
		buildUnit(new Miner(this));
		buildUnit(new Gatherer(this));
		buildUnit(new Gatherer(this));
	}
	
	public void strategy() 
	{		
		if(getFleetValuePercentage(Gatherer.class) < .2f)
		{
			buildUnit(new Gatherer(this));
		}
		else if(getFleetValuePercentage(Miner.class) < .2f)
		{
			buildUnit(new Miner(this));
		}
		else
		{
			buildUnit(new Fighter(this));
		}		
	}
			
	public void draw(Graphics g) 
	{
		
	}
	
}
