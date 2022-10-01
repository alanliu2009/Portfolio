package teams.starter.dummy;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import player.Boon;
import player.Player;


public class DummyPlayer extends Player
{	
	
	public DummyPlayer(int team, Game g) 
	{
		super(team, g);
		
		setName("Dummy");
		setTeamImage("src/teams/starter/dummy/dummy.png");
		
		setBoonOne(Boon.ARMORER);
		setBoonTwo(Boon.GUARDIAN);
		
		setColorPrimary(165, 90, 70);
		setColorSecondary(255, 255, 255);
		setColorAccent(200, 200, 200);
	}
	

	public void strategy() 
	{		

	}
			
	public void draw(Graphics g) 
	{
		
	}
	
}
