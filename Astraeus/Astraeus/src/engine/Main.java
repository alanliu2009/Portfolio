package engine;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import engine.states.End;
import engine.states.Game;
import engine.states.Menu;
import engine.states.Splash;

public class Main extends StateBasedGame 
{
    public static final int TITLE_ID = 0;
    public static final int SELECTION_BATTLE_ID  = 3;
    public static final int GAME_ID  = 4;
    public static final int END_ID  = 5;
       
    private BasicGameState title;
    private BasicGameState selectionBattle;
    private BasicGameState game;
    private BasicGameState end;

    private static AppGameContainer appgc;
    
    
	public Main(String name) 
	{
		super(name);
		
		title = new Splash(TITLE_ID);
		game = new Game(GAME_ID);
		selectionBattle = new Menu(SELECTION_BATTLE_ID, (Game) game);
		end = new End(END_ID);
	}

	public void initStatesList(GameContainer gc) throws SlickException 
	{
		addState(title);
		addState(selectionBattle);
		addState(game);
		addState(end);
	}

	public static int getScreenWidth()
	{
		return appgc.getScreenWidth();
	}
	
	public static int getScreenHeight()
	{
		return appgc.getScreenHeight();
	}
	
	public static int getFPS()
	{
		return appgc.getFPS();
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			appgc = new AppGameContainer(new Main("Astraeus"));
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			
			appgc.setDisplayMode(getScreenWidth(), getScreenHeight(), false);
			appgc.setTargetFrameRate(Values.FRAMES_PER_SECOND);
		//	System.getProperties().list(System.out);
			appgc.start();
			appgc.setVSync(true);
		

		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
}