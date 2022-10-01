package teams.starter.green;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import player.Boon;
import player.Player;
import teams.starter.green.units.Battleship;
import teams.starter.green.units.Dreadnought;
import teams.starter.green.units.Worker;

public class Green extends Player
{	
	public Green(int team, Game g) 
	{
		super(team, g);

		setName("Green");
		setTeamImage("src/teams/starter/green/green.png");

		setColorPrimary(60, 180, 60);
		setColorSecondary(255, 255, 255);
		setColorAccent(255, 255, 255);
		
		setBoonOne(Boon.BLASTER);
		setBoonTwo(Boon.MECHANIC);
	}
		
	public void strategy() 
	{		
		if(getFleetValuePercentage(Worker.class) < .2f)
		{
			buildUnit(new Worker(this));
		}
		else if(getFleetValuePercentage(Battleship.class) < .3f)
		{
			buildUnit(new Battleship(this));
		}		
		else 
		{
			buildUnit(new Dreadnought(this));
		}
	}
			
	public void draw(Graphics g) 
	{
		
	}
	
}
