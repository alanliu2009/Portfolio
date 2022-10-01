package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.newdawn.slick.Input;

import entities.living.Player;
import gamestates.Game;

// Tracks keys that are pressed down
public class KeyManager implements Predicate<Integer>{
	public static ArrayList<Integer> keyList = new ArrayList<Integer>();
	
	private Player player;
	private Input input;
	
	private float curSpeed = 10;
	
	public KeyManager(Game game) {
		this.input = game.getGC().getInput();
		this.player = game.getPlayer();
	}
	
	// Predicate required method
	public boolean test(Integer key) { return input.isKeyDown(key); }
	public void keyDown(int key) {
		switch(key) 
		{
			case Input.KEY_A:
				player.setXSpeed(-curSpeed);
				break;
			case Input.KEY_D:
				player.setXSpeed(curSpeed);
				break;
			case Input.KEY_S:
				player.fall();
				break;	
		}
	}
	
}