package teams.student.power.resources;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import objects.GameObject;
import objects.entity.node.Node;
import objects.entity.unit.Unit;
import player.Player;
import teams.student.power.units.resourceUnits.Miner;


public class MinerBuddies {
	private Miner[] miners;
	private Node target;
	private Player p;
	private Point center;
	private boolean sectorAssignment;
	
	public MinerBuddies(Player p, int capacity, boolean top) {
		target = null;
		miners = new Miner[capacity];
		this.p = p;
		center = new Point(0,0);
		
		sectorAssignment = top;
	}
	
	public void update() {
		updateCenter();
		removeDead();
		autoClearTarget();
		actuateTarget();
	}
	
	public void addMiner(Miner m, int slot) {
		miners[slot] = m;
	}
	
	public Miner[] getMiners(){
		return miners;
	}
	
	public int getEmptySlot() {
		for(int i = 0; i < miners.length; i++) {
			if(miners[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public Node getTarget() {
		return target;
	}
	
	public void setTarget(Node n) {
		target = n;
	}
	
	public void actuateTarget() {
		if(target!=null) {
			for(Miner m : miners) {
				if(m!=null) {
					m.harvest(target);
				}
			}
		}
	}
	
	//true means top, false means bottom
	public boolean getSectorAssignemnt() {
		return sectorAssignment;
	}
	
	//remove behavior in miners' action method if using this one
	public void harvest() {
		for(int i = 0; i < miners.length; i++) {
			miners[i].harvest(target);
		}
	}
	
	public void retreat() {
		for(int i = 0; i < miners.length; i++) {
			miners[i].moveTo(p.getMyBase());
		}
	}
	
	public void autoClearTarget() {
		if(target!= null) {
			if(target.isDead()) {
				target = null;
			}
		}
	}
	
	public void removeDead() {
		for(int i = miners.length-1; i >= 0; i--) {
			if(miners[i] != null && miners[i].isDead()) {
				miners[i] = null;
			}
		}
	}
	
	public void updateCenter() {
		center.setX(getCenterX());
		center.setY(getCenterY());
	}
	
	public float getCenterX() {
		float sum = 0;
		int total = 0;
		for(int i = 0; i < miners.length; i ++) {
			if(miners[i] != null) {	
				sum += miners[i].getCenterX();
				total++;
			}
		}
		return sum / total;
	}
	
	public float getCenterY() {
		float sum = 0;
		int total = 0;
		for(int i = 0; i < miners.length; i ++) {
			if(miners[i] != null) {
				sum += miners[i].getCenterY();
				total++;
			}
			
		}
		return sum / total;
	}
	
	public float getMaxDeviation() {
		float max = 0;
		float dist = 0;
		for(int i = 0; i < miners.length; i ++) {
			if(miners[i] != null) {
				dist = miners[i].getDistance(center.getX(), center.getY());
				if(dist > max) {
					max = dist;
				}
			}
		}
		return dist;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.green);
		float r = getMaxDeviation();
		g.drawOval(center.getX()-r, center.getY()-r, r*2, r*2);
		if(target != null) {
			g.drawLine(center.getX(), center.getY(), target.getCenterX(), target.getCenterY());
		}
		
	}

}
