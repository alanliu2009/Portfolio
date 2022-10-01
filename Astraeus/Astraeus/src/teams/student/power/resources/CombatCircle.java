package teams.student.power.resources;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import player.Player;

public class CombatCircle {
	private Player p;
	private Point center;
	private float radius;
	private boolean active;
	
	public CombatCircle(Player p, Point c, float r) {
		this.p = p;
		center = c;
		radius = r;
		active = false;
	}
	
	public void setState(boolean s) {
		active = s;
	}
	
	public boolean getState() {
		return active;
	}
	
	public void setCenter(float x, float y) {
		center.setX(x);
		center.setY(y);
	}
	
	public void setCenter(Point p) {
		center = p;
	}
	
	public float getCenterX() {
		return center.getX();
	}
	
	public float getCenterY() {
		return center.getY();
	}
	
	public Point getCenter() {
		return center;
	}
	
	public void setRadius(float r) {
		radius = r;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public void draw(Graphics g) {
		if(active) {
			g.setColor(Color.red);
			g.drawOval(center.getX()-radius, center.getY()-radius, radius*2, radius*2);
		}
	}
	
}
