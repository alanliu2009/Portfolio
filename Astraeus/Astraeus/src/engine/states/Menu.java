package engine.states;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import engine.Main;
import engine.Utility;
import engine.Values;
import player.Player;
import teams.starter.blue.Blue;
import teams.starter.red.Red;
import ui.display.Fonts;
import ui.display.Images;
import ui.display.selector.Button;
import ui.display.selector.DropDown;
import ui.display.selector.UIManager;
import ui.display.selector.WriteButton;
import ui.display.selector.WriteButton.WritingType;


public class Menu extends BasicGameState
{
	private int id;
	private static Game g;

	private Image background;

	private static String[] blklst = {"neutral"};

	private static List<Player> players;
	private static List<Group> groups;
	private static Map<Integer, Class<? extends Player>> tempPlayers;

	private static final String playersDir = "bin/teams";

	private static List<DropDown<Class <? extends Player>>> playerDropDowns;
	private static List<WriteButton> difficultyButtons;


	Button<Integer> run;
	DropDown<Integer> test;

	public Menu(int id, Game g) {
		this.id = id;
		this.g = g;
		players = new ArrayList<Player>();
		tempPlayers = new HashMap<Integer, Class<? extends Player>>();
	}


	public static void setPlayers()
	{
		addPlayer(Blue.class);		
		players.get(0).setDifficultyRating(100);
		addPlayer(Red.class);
		players.get(1).setDifficultyRating(100);

	}


	public int getID() {
		return id;
	}

	public static List<Player> getPlayers() {
		return players;
	}



	public static String spaceString(String s)
	{
		for(int i = 1; i < s.length(); i++)
		{
			if(s.substring(i, i+1).equals(s.substring(i, i+1).toUpperCase()))
			{
				s = s.substring(0, i) + " " + s.substring(i);
				i++;
			}
		}

		return s;
	}

	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		UIManager.gc = gc;

		

		run = new Button<Integer>(Main.getScreenWidth() * .025f, Main.getScreenHeight() * .85f,  Main.getScreenWidth() * .275f, Main.getScreenHeight() * .060f, 1);
		run.addText(0, "Run Game");

		
		groups = new ArrayList<Group>();
		InitGroups();

		initButtons();

		File f = new File("res/select/Select.txt");

		if(!f.exists())
			return;

		int i = 0;
		try {
			Scanner s = new Scanner(f);

			String t;

			i = 0;

			while(s.hasNextLine()) {
				int prevI = i;
				t = s.nextLine();
				for(Group g : groups) {
					for(Class<?extends Player> c : g.getPlayers()) {
						if(c.getName().equals(t)) {
							playerDropDowns.get(i).setReturnOBJ(c);
							playerDropDowns.get(i).addText(1, spaceString(c.getSimpleName()));
							tempPlayers.put(i, c);
							try {

								playerDropDowns.get(i).addColor(0, getColor(i).darker(.4f));
								playerDropDowns.get(i).addColor(2, new Color(200, 200, 200));
								playerDropDowns.get(i).addColor(3, new Color(10,10,10, 150));

							} catch (IllegalArgumentException | SecurityException e) {
								e.printStackTrace();
							}
							i++;
						}
					}
				}
				t = s.nextLine();
				if(i == prevI) {
					System.out.println("\nSelect.txt issue: Team " + (i+1) + " is not valid");
					i++;
				}
				if(isInteger(t)) {
					difficultyButtons.get(i-1).setReturnOBJ(t);
					difficultyButtons.get(i-1).addText(1, difficultyButtons.get(i-1).getText(0) + t);
				} else
					System.out.println("\nSelect.txt issue: Difficulty " + (i) + " is not a number");
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(NoSuchElementException e) {
			System.out.println("\nSelect.txt issue: Not formatted correctly");
			difficultyButtons.get(i-1).setReturnOBJ(null);
		}

		f = new File("res/select/SelectScenario.txt");

		if(!f.exists())
			return;


	}

	private void initButtons() {
		playerDropDowns = new ArrayList<DropDown<Class <? extends Player>>>();
		difficultyButtons = new ArrayList<WriteButton>();
	
		float x1 = Main.getScreenWidth() * .025f;
		float x2 = Main.getScreenWidth() * .175f;
		float y1 = Main.getScreenHeight() * .400f;
		float y2 = Main.getScreenHeight() * .450f;
		float w = Main.getScreenWidth() * .125f;
		float h = Main.getScreenHeight() * .040f;

		difficultyButtons.add(new WriteButton(x1, y1, w, h * 3/4, WritingType.INTEGER));
		playerDropDowns.add(new DropDown<Class <? extends Player>>(x1,  y2,w, h));

		difficultyButtons.add(new WriteButton(x2, y1, w, h * 3/4, WritingType.INTEGER));
		playerDropDowns.add(new DropDown<Class <? extends Player>>(x2,  y2, w, h));

		for(int i = 0; i < 2; i++) {
			playerDropDowns.get(i).addText(0, "Player " + (i+1));
			for(Group g : groups) {
				for(Class<? extends Player> e : g.getPlayers()) {
					playerDropDowns.get(i).addElement(e, spaceString(e.getSimpleName()));
				}
			}
			difficultyButtons.get(i).setDefault("Power: ", "100");
		}


	}

	private class Group {
		String name;
		String path;
		List<Class<? extends Player>> players;

		Group(File file) {
			name = file.getName();
			path = file.getAbsolutePath();
			players = new ArrayList<Class<?extends Player>>();

			for(File f : file.listFiles()) {
				if(!blklstd(f.getName())) {
//					System.out.println("Printing file in class SelectionBattle: " + f);
					List<Class<?extends Player>> c = getPlayer(f);

					if(c != null)
						players.addAll(c);
				}
			}
		}

		List<Class<?extends Player>> getPlayer(File f) 
		{
			ArrayList<Class<? extends Player>> temp = new ArrayList<Class<? extends Player>>();
			for(File a : f.listFiles()) {

				try {
					URL url = Paths.get(new File("bin").getAbsolutePath()).toUri().toURL();
					URL[] urls = {url};
					ClassLoader cl = new URLClassLoader(urls);
					Class<?> cs = cl.getSystemClassLoader().loadClass(a.getPath().substring(4, a.getPath().length() - 6).replace("\\", "."));
					if(Player.class.isAssignableFrom(cs) && !Modifier.isAbstract(((Class<? extends Player>) cs).getModifiers())) {
						temp.add((Class<? extends Player>) cs);
					}
				}	catch(Exception e) {
				}
			}
			return temp;
		}

		List<Class<?extends Player>> getPlayers() {
			return players;
		}

		String getName() {
			return this.name;
		}

		String getPath() {
			return this.path;
		}
	}

	private void InitGroups() {
		File dir = new File(playersDir);

		for (File f : dir.listFiles()) {
			if(f.isDirectory() && !blklstd(f.getName()))
				groups.add(new Group(f));
		}
	}


	public static void addPlayer(Class <? extends Player> clazz) {	
		try {
			Player t = (Player)(clazz.getConstructor(new Class<?>[] {Integer.TYPE, Game.class})).newInstance(players.size(), g);
			players.add(t);			

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	private boolean blklstd(String s) {
		for(String a : blklst)
			if(a.equalsIgnoreCase(s))
				return true;

		return false;
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{		



		int c = 0;

		for(DropDown d : playerDropDowns) {
			Class<? extends Player> t = (Class<? extends Player>) d.getReturnOBJ();

			if(t != null) {
				if(tempPlayers.get(c) != t) {
					tempPlayers.put(c, t);
					try {
						d.addColor(0, getColor(c).darker(.35f));

						// Text Color
						d.addColor(2, new Color(240,240,240));

						// Shadow
						d.addColor(3, new Color(10,10,10, 150));


					} catch (IllegalArgumentException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
			else {
				d.addColor(0, new Color(64, 64, 64));
				//d.addColor(2, new Color(200, 200, 200));
				d.addColor(3, new Color(10,10,10, 150));

				tempPlayers.put(c, null); 
			}
			c++;
		}

		for(WriteButton wb : difficultyButtons) {
			int difficulty;
			try {
				difficulty = Integer.parseInt(wb.getReturnOBJ());
			}
			catch(NumberFormatException e) {
				wb.setReturnOBJ("100");
				wb.addText(1, wb.getText(0)+wb.getReturnOBJ());
				difficulty = 100;
			}
			if(difficulty < 100) {
				int red = (int) ((float) (100-Math.max(difficulty, Values.SELECT_MIN_DIFFICULTY_RED))/
						(100-Values.SELECT_MIN_DIFFICULTY_RED)*(Values.SELECT_DIFFICULTY_RED_MAX-80));
				wb.addColor(0, new Color(80+red, 64, 64));
				wb.addColor(2, new Color(255, 255, 255));
				wb.addColor(3, new Color(10,10,10, 150));
			}
			else if(difficulty > 100) {
				int green = (int) ((float) (Math.min(difficulty, Values.SELECT_MAX_DIFFICULTY_GREEN)-100)/
						(Values.SELECT_MAX_DIFFICULTY_GREEN-100)*(Values.SELECT_DIFFICULTY_GREEN_MAX-80));
				wb.addColor(0, new Color(64, 80+green, 64));
				wb.addColor(2, new Color(255, 255, 255));
				wb.addColor(3, new Color(10,10,10, 150));
			}
			else {
				wb.addColor(0, new Color(40, 25, 80));
				wb.addColor(2, new Color(255, 255, 255));
				wb.addColor(3, new Color(10,10,10, 150));
			}
		}


		//			if(run.isPressed() || gc.getInput().isKeyDown(Input.KEY_SPACE) || gc.getInput().isKeyDown(Input.KEY_ENTER)) {
		if(hasPlayers() && (run.isPressed() || gc.getInput().isKeyDown(Input.KEY_SPACE)) && run.isUsingInput()) {

			for(int i = 0; i < 2; i++) {
				if(playerDropDowns.get(i).getReturnOBJ() != null) {
					Menu.addPlayer((playerDropDowns.get(i).getReturnOBJ()));
					players.get(i).setDifficultyRating(Integer.parseInt(difficultyButtons.get(i).getReturnOBJ()));
				}
			}

			File f = new File("res/select/Select.txt");

			if(!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {	
					e.printStackTrace();
				}
			}

			try {
				FileOutputStream p = new FileOutputStream(f, false);

				for(int i = 0; i < 2; i++) {
					if(playerDropDowns.get(i).getReturnOBJ() != null) {
						p.write((playerDropDowns.get(i).getReturnOBJ().getName() + "\r\n").getBytes());
						p.write((difficultyButtons.get(i).getReturnOBJ() + "\r\n").getBytes());
					}
				}
				p.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

			f = new File("res/select/SelectScenario.txt");

			if(!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {	
					e.printStackTrace();
				}
			}



			sbg.enterState(Main.GAME_ID, 
					new FadeOutTransition(Color.black, Values.TRANSITION_FADE_TIME), 
					new FadeInTransition(Color.black, Values.TRANSITION_FADE_TIME));
		}

		UIManager.reset();
	}




	public Color getColor(int i)
	{
		if(i == Values.TEAM_ONE_ID)
		{
			return new Color(30, 233, 240);
		}
		else if(i == Values.TEAM_TWO_ID)
		{
			return new Color(255, 130, 60);
		}
		else
		{
			return new Color(225, 25, 225);
		}
	}

	private boolean hasPlayers() {
		for(DropDown d : playerDropDowns) {
			if(d.getReturnOBJ() == null)
				return false;
		}
		return true;
	}

	private static boolean isInteger(String s) {
		if(s.isEmpty()) return false;
		char[] temp = s.trim().toCharArray();
		for(int i = 0; i < temp.length; i++) {
			if(i == 0 && temp[i] == '-') {
				if(temp.length == 1) return false;
				else continue;
			}
			if(Character.digit(temp[i], 10) < 0) return false;
		}

		return true;
	}

	public void enter(GameContainer gc, StateBasedGame sbg) 
	{
		clear();
		gc.setMouseGrabbed(false);
		
		if(Math.random() < .005 || Splash.rock)
		{
			background = Images.rock.getScaledCopy(Main.getScreenWidth(), Main.getScreenHeight());
		}
		else
		{
			background = Images.bgPurpleBattle.getScaledCopy(Main.getScreenWidth(), Main.getScreenHeight());
		}
	}

	public static void clear()
	{
		players.clear();
		groups.clear();
		tempPlayers.clear();
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		
		if(background != null)
		{  
			background.draw();
		}
		
		float x = Main.getScreenWidth() * .025f;
		float y = Main.getScreenHeight() * .025f;
		float w = Main.getScreenWidth() * .275f;
		float h = w / 10 * 4;
		
		
		g.setColor(new Color(40, 40, 40, 120));
		g.fillRect(0, 0, Main.getScreenWidth() * .335f, Main.getScreenHeight());
		g.setLineWidth(5);
		g.setColor(new Color(20, 20, 20, 255));

		g.drawLine(Main.getScreenWidth() * .335f, 0, Main.getScreenWidth() * .335f, Main.getScreenHeight());
		
		Images.logo.draw(x, y, w, h);
		
		g.setColor(Color.white);

		Utility.drawStringRightTop(g, Fonts.ocr18, "Astraeus Version " + Values.VERSION, Main.getScreenWidth() - 15, 15);
		Utility.drawStringRightTop(g, Fonts.ocr18, "Released " + Values.RELEASE_DATE, Main.getScreenWidth() - 15, 35);

		UIManager.render(gc, g);
	}
}
