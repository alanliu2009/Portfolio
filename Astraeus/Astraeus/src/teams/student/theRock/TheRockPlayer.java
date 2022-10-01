package teams.student.theRock;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import objects.entity.unit.BaseShip;
import player.Boon;
import player.Player;

public class TheRockPlayer extends Player
{	
	public TheRockPlayer(int team, Game g) 
	{
		super(team, g);
		
		setName("The Rock");
		setTeamImage("src/teams/student/theRock/TheRock.jpg");

		setColorPrimary(150, 150, 150);
		setColorSecondary(255, 255, 255);
		setColorAccent(255, 255, 255);
		
		setBoonOne(Boon.SCOUT);				
		setBoonTwo(Boon.GUNNER);
	}
	
	public void opening()
	{
		for(int i = 0; i < 8; i++) {
			buildUnit(new TheRockUnit(this));
		}
	}
	
	public void strategy() 
	{		
		
	}
			
	public void draw(Graphics g) 
	{
		
	}
	
}
