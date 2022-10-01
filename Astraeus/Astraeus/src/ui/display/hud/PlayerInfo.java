package ui.display.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Utility;
import engine.Values;
import engine.states.Game;
import player.Player;

public class PlayerInfo
{	
	Player player;
	protected float x;
	protected float y;
	protected float w;
	protected float h;

	float pairGap = .10f;
	float basicX = .15f;
	float fleetX = basicX + .22f;
	float mineralX = fleetX + .22f;
	float combatX = mineralX + .22f;
	float lagX = combatX + .17f;
	
	public PlayerInfo(Player owner, float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.player = owner;
		

	}

	public void render(Graphics g)
	{	
//		Utility.drawStringLeftTop(g, bigFont, "" + player.getName(), x, y);
		basicInfo(g);
		fleetInfo(g);
		mineralInfo(g);
		combatInfo(g);
		lagInfo(g);
		bonusInfo(g);
		lostMineralInfo(g);
//		Utility.drawStringCenterCenter(g, bigFont, "" + (int) player.getFleetValue(), x + w * .1f, y + h * .1f);
//		Utility.drawStringCenterCenter(g, smallFont, "Fleet Value", x + w * .1f, y + h * .3f);

		
		
//		g.setColor(new Color(200, 200, 50));
//		g.drawString("Mins:     " + (int) player.getMinerals(), x + 20, y + PADDING + SPACING);
//		g.drawString("Mined:    " + (int) player.getMineralsMined(), x + 220, y + PADDING + SPACING);
//			
//		g.setColor(new Color(200, 200, 200));
//		g.drawString("Units:    " + (int) player.countMyUnits(), x + 420, y + PADDING + SPACING*3);		
	}
	
	
	public void basicInfo(Graphics g)
	{
		
	

		g.setColor(player.getColorPrimary());
	
		String name = player.getName().substring(0, Math.min(10, player.getName().length()));
		Utility.drawStringCenterCenter(g, Hud.bigFont, name, x + w * basicX, y + h * .15f);

		g.setColor(player.getColorSecondary());

		
		Utility.drawStringCenterCenter(g, Hud.smallFont, "" + player.getBoonOne().name(), x + w * basicX, y + h * .40f);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "" + player.getBoonTwo().name(), x + w * basicX, y + h * .60f);

	}
	
	
	public void fleetInfo(Graphics g)
	{

		
		g.setColor(new Color(70, 70, 70));
		
		Utility.drawStringCenterCenter(g, Hud.headingFont, "FLEET", x + w * fleetX, y + h * .12f);

		g.setColor(new Color(100, 180, 255));

		
		Utility.drawStringCenterCenter(g, Hud.bigFont, "" + (int) player.getFleetValue(), x + w * (fleetX-pairGap/2), y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Value", x + w * (fleetX-pairGap/2), y + h * .65f);

		if(player.countMyUnits() == player.getMaxFleetSize())
		{
			g.setColor(Color.red);
		}
		else if(player.countMyUnits() >= player.getMaxFleetSize() * .8f)
		{
			g.setColor(Color.yellow);
		}
		else
		{
			g.setColor(new Color(100, 180, 255));
		}
		
		Utility.drawStringCenterCenter(g, Hud.bigFont, "" + (int) player.countMyUnits(), x + w * (fleetX + pairGap/2), y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Count", x + w * (fleetX + pairGap/2), y + h * .65f);
	}
	
	public void mineralInfo(Graphics g)
	{


		
		g.setColor(new Color(70, 70, 70));
		
		Utility.drawStringCenterCenter(g, Hud.headingFont, "RESOURCE", x + w * mineralX-pairGap, y + h * .12f);

		g.setColor(new Color(255, 200, 150));		
		Utility.drawStringCenterCenter(g, Hud.bigFont, "" + (int) player.getMinerals(), x + w * (mineralX-pairGap/2), y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Store", x + w * (mineralX-pairGap/2), y + h * .65f);

		g.setColor(new Color(255, 200, 150));		
		Utility.drawStringCenterCenter(g, Hud.bigFont, "" + (int) player.getMineralsMined(), x + w * (mineralX + pairGap/2), y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Total", x + w * (mineralX + pairGap/2), y + h * .65f);
	}
	
	public void combatInfo(Graphics g)
	{


		g.setColor(new Color(70, 70, 70));
		
		Utility.drawStringCenterCenter(g, Hud.headingFont, "COMBAT", x + w * (combatX), y + h * .12f);


		float dmgPlayer = player.getDamageDealt() - player.getRepairRecieved() - player.getShieldRecieved();
		float dmgTotal = Game.getTotalDamageDealt() - Game.getTotalRepairRecieved() - Game.getTotalShieldRecieved();
		int dmg = 0;
		
		if(dmgTotal > 1)
		{
			dmg = (int) ((dmgPlayer / dmgTotal) * 100 + .5f);
		}
		
		g.setColor(new Color(255, 150, 200));
		Utility.drawStringCenterCenter(g, Hud.bigFont, "" + dmg + "%", x + w * (combatX-pairGap/2), y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Damage", x + w *(combatX-pairGap/2), y + h * .65f);
	
		int dodge = 0;
		if(player.getDodgeAttempts() > 0)
		{
			dodge =  Math.round ((float) player.getDodgeCount() / (float) player.getDodgeAttempts() * 100);
		}


		g.setColor(new Color(255, 150, 200));
		Utility.drawStringCenterCenter(g, Hud.bigFont, dodge + "%", x + w * (combatX+pairGap/2), y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Dodge", x + w * (combatX+pairGap/2), y + h * .65f);
	}
	
	public void lagInfo(Graphics g)
	{


		g.setColor(new Color(70, 70, 70));
		
		Utility.drawStringCenterCenter(g, Hud.headingFont, "ALGO", x + w * lagX, y + h * .12f);


		int latency = player.getWeightedAverageLatency(Values.LATENCY_SAMPLE_FREQUENCY);
		
		if(latency > Player.EXTREME_LATENCY)
		{
			g.setColor(new Color(255, 150, 150));
		}
		else if(latency > Player.HIGH_LATENCY)
		{
			g.setColor(new Color(255, 200, 150));
		}
		else if(latency > Player.MEDIUM_LATENCY)
		{
			g.setColor(new Color(255, 255, 150));
		}
		else
		{
			g.setColor(new Color(150, 255, 155));
		}

		Utility.drawStringCenterCenter(g, Hud.bigFont, "" + latency, x + w * lagX, y + h * .42f);
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Hud.smallFont, "Latency", x + w *lagX, y + h * .65f);
	
	
	}
	
	public void bonusInfo(Graphics g)
	{
		if(player.getDifficultyRating() > 1)
		{
			g.setColor(new Color(50, 255, 50));
		}
		else if(player.getDifficultyRating() < 1)
		{
			g.setColor(new Color(255, 50, 50));
		}
		else
		{
			return;
		}
		
		float spacingX = .06f;
		float spacingY = .42f;

		String message = player.getDifficultyRatingModifierString();
				
		if(player == Game.getPlayerOne())
		{
			Utility.drawStringCenterCenter(g, Hud.bigFont, message, x + w * (spacingX + .02f), y - h * spacingY);
		}
		else
		{
			Utility.drawStringCenterCenter(g, Hud.bigFont, message, x + w + w * (spacingX - .01f), y - h * spacingY);
		}
		
		


		
		

	}
	
	public void lostMineralInfo(Graphics g)
	{
		int mineralsLost = Math.round(player.getMineralsLost());
		
		if(mineralsLost == 0)
		{
			return;
		}
		
		g.setColor(new Color(255, 50, 50));

		
		String message = "Lost " + mineralsLost + " minerals from latency";
		float spacingY = .36f;


		Utility.drawStringCenterCenter(g, Hud.smallFont, message, x + w * .55f, y - h * spacingY);

	
	}
}
