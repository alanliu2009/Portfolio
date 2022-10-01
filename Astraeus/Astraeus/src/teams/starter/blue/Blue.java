package teams.starter.blue;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import player.Boon;
import player.Player;
import teams.starter.blue.units.Guardian;
import teams.starter.blue.units.Lancer;
import teams.starter.blue.units.Worker;

public class Blue extends Player
{	
	
	public Blue(int team, Game g) 
	{
		super(team, g);
		
		setName("Blue");
		setTeamImage("src/teams/starter/blue/blue.png");

		setColorPrimary(50, 100, 200);
		setColorSecondary(255, 255, 255);
		setColorAccent(255, 255, 255);
		
		setBoonOne(Boon.LANCER);
		setBoonTwo(Boon.GUARDIAN);
	}
		
	public void strategy() 
	{		
		if(getFleetValuePercentage(Worker.class) < .4f)
		{
			buildUnit(new Worker(this));
		}
		else if(getFleetValuePercentage(Guardian.class) < .3f)
		{
			buildUnit(new Guardian(this));
		}	
		else
		{
			buildUnit(new Lancer(this));
		}
	}
			
	public void draw(Graphics g) 
	{
		
	}
	
}
