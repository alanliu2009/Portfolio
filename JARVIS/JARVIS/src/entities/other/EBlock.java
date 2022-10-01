package entities.other;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.Engine;
import core.Values;

public class EBlock extends EItem {
	public EBlock(int id, float x, float y) {
		super(x + 0.5f, y - 0.5f);

		this.sprite = game.displayManager.getBlockSprite(id);
		
		this.width = Values.EBlock_Size;
		this.height = Values.EBlock_Size;
		
		this.itemID = id;
		
		hitbox.setWidth(width + 0.01f);
		hitbox.setHeight(height + 0.01f);
	}
	
	public void renderOther(Graphics g) {
		g.setColor(Color.black);
		hitbox.drawHitBox(g);
	}
}