package teams.student.drive;

import org.newdawn.slick.Graphics;

import engine.states.Game;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import player.Boon;
import player.Player;
import teams.student.drive.controllers.MacroController;
import teams.student.drive.controllers.resource.ResourceController;

public class DrivePlayer extends Player
{		
	private MacroController macroController;
	
	public DrivePlayer(int team, Game g) {
		super(team, g);
		
		// Default Player Initializations
		setName("Drive"); // Team Name
		setTeamImage("src/teams/student/drive/Drive.png"); // Team Image

		// Color
		setColorPrimary(255, 255, 0); // Team Primary Color
		setColorSecondary(255, 255, 255); // Team Secondary color
		setColorAccent(255, 255, 255); // Team Accent Color
		
		// Boons
		setBoonOne(Boon.SNIPER);
		setBoonTwo(Boon.SCOUT);
	}
	
	public MacroController getMacroController() { return macroController; }
	public ResourceController getResourceController() { return macroController.getResourceController(); }
	public void draw(Graphics g) { this.macroController.draw(g); }
	
	@Override
	public void initialize() {
		super.initialize();
		
		// Controller Initialization
		this.macroController = new MacroController(this);
	}
	
	public void opening() {	macroController.initialStrategy(); } // Opening Strategy Initialization
	public void strategy() { macroController.update();  }		 // Strategy
}
