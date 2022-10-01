package teams.starter.red;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import player.Boon;
import player.Player;
import teams.starter.red.units.Gunship;
import teams.starter.red.units.Sniper;
import teams.starter.red.units.Worker;

public class Red extends Player
{	
	
	public Red(int team, Game g) 
	{
		super(team, g);

		setName("Red");
		setTeamImage("src/teams/starter/red/red.png");

		setColorPrimary(200, 45, 50);
		setColorSecondary(255, 255, 0);
		setColorAccent(255, 255, 255);
		
		setBoonOne(Boon.GUNNER);
		setBoonTwo(Boon.ARMORER);
	}

	public void strategy() 
	{		
		if(getFleetValuePercentage(Worker.class) < .4f)
		{
			buildUnit(new Worker(this));
		}
		else if(getFleetValuePercentage(Gunship.class) < .3f)
		{
			buildUnit(new Gunship(this));
		}		
		else
		{
			buildUnit(new Sniper(this));

		}
	}
			
	public void draw(Graphics g) 
	{
		
	}
	
}
