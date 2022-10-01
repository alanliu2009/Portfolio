package teams.student.power.resources;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import player.Player;
import teams.student.power.units.resourceUnits.Miner;

public class ResourceManagement {
	private Player p;

	private int timer;

	private ArrayList<MinerBuddies> minerBuds;
	private int buddyCapacity;
	private boolean topSection;
	
	public float resourceVelocityMax;
	public float resourceVelocityMin;
	
	private int clusterSize;
	private float clusterX;
	private float clusterY;
	private ArrayList<Resource> nearestResources;
	private ArrayList<Point> targetClusters;
	private ArrayList<Point> targettedClusters;
	private ArrayList<Resource> targettedResources;
	
	public ResourceManagement(Player p) {
		this.p = p;
		
		timer = 0;
		
		resourceVelocityMin = 2.0f;
		resourceVelocityMax = 2.2f;
		
		minerBuds = new ArrayList<MinerBuddies>();
		buddyCapacity = 3;
		topSection = true;
		
		clusterSize = 0;
		clusterX = 0;
		clusterY = 0;
		nearestResources = new ArrayList<Resource>();
		targettedResources = new ArrayList<Resource>();
		targetClusters = new ArrayList<Point>();
		targettedClusters = new ArrayList<Point>();
	}
	
	public ArrayList<Point> getTargetClusters() { return targetClusters; }
	
	public void update() {
		
		for(MinerBuddies m : minerBuds) {
			m.update();
		}
		assignNodes();
		
		if(timer % 6 == 0)
		{
			nearestResources = sortResources(ResourceManager.getResources());
			identifyClusters();
			targettedClusters.clear();
		}	
		
		targettedResources.clear();
	}
	
	//
	// resource stuff
	//
	
	public void addCluster(Point p)
	{
		targettedClusters.add(p);
	}
	
	public boolean checkCluster(Point cluster)
	{
		if(targettedClusters.size() == 0) return true;
		
		for(Point p : targettedClusters)
		{
			if(cluster == p) return false;
		}
		
		return true;
	}
	
	public void addResource(Resource r)
	{
		targettedResources.add(r);
	}
	
	public boolean checkResource(Resource resource)
	{
		if(targettedResources.size() == 0) return true;
		
		for(Resource r : targettedResources)
		{
			if(resource == r) return false;
		}
		
		return true;
	}
	
	public ArrayList<Resource> sortResources(ArrayList<Resource> r)
	{
		ArrayList<Float> nearestDistances = new ArrayList<Float>(); //keep the distances, what we'll be sorting with
		ArrayList<Resource> nearestResources = new ArrayList<Resource>(); //reorder the resource list based on the reordering
		
		//int steps = 0;
		
		for(Resource x : r)
		{
			if(x.getCurSpeed() < Resource.RESOURCE_SPEED * 0.8f)
			{
				if(p.getMyBase().getDistance(x) > 10000 && r.size() > 100)
				{
					
				}
				else
				{
					nearestDistances.add(p.getMyBase().getDistance(x));
					nearestResources.add(x);
				}
			}
		}
		
		Collections.sort(nearestDistances);
		
		for(int i = 0; i < nearestDistances.size(); i++)
		{
			for(int j = 0; j < nearestResources.size(); j++)
			{
				//steps++;
				if(p.getMyBase().getDistance(nearestResources.get(j)) == nearestDistances.get(i))
				{
					Resource temp = nearestResources.get(j);
					
					nearestResources.set(j, nearestResources.get(i));
					nearestResources.set(i, temp);
					break;
				}
			}
		}
		
		return(nearestResources);
	}
	
	public void identifyClusters()
	{
		targetClusters.clear();
		
		clusterSize = 0;
		clusterX = 0;
		clusterY = 0;
		
		for(int i = 0; i < nearestResources.size() - 1; i++)
		{
			if(nearestResources.get(i).getDistance(nearestResources.get(i + 1))
					< 450)
			{
				clusterSize++;
			} 
			else if(clusterSize > 2) 
			{				
				for(int j = i - 1; j > i - clusterSize - 1; j--)
				{			
					clusterX = clusterX + nearestResources.get(j).getCenterX();
					clusterY = clusterY + nearestResources.get(j).getCenterY();
				}
				
				clusterX /= clusterSize;
				clusterY /= clusterSize;
				
				targetClusters.add(new Point(clusterX, clusterY));
				
				clusterSize = 0;
				clusterX = 0;
				clusterY = 0;
			}
			else
			{	
				clusterSize = 0;
			}
		}
	}
	
	public void draw(Graphics g) 
	{
		g.setColor(new Color(0, 255, 255));
		
		if(targetClusters != null)
		{
			for(Point p : targetClusters)
			{
				g.fillOval(p.getX() - 20, p.getY() - 20, 40, 40);
			}
		}
	}
	
	//
	// end of resource stuff
	//
	
	public ArrayList<MinerBuddies> getBuddies(){
		return minerBuds;
	}
	
	public void assignBuddies(Miner m) {
		boolean assigned = false;
		MinerBuddies b = null;
		int slot = -2;
		while(!assigned) {
			for(int i = 0; i < minerBuds.size(); i++) {
				b = minerBuds.get(i);
				slot = b.getEmptySlot();
				if(slot != -1) {
					b.addMiner(m, slot);
					assigned = true;
					//System.out.println("assigned a new miner");
					break;
				}
			}
			if(!assigned) {
				minerBuds.add(new MinerBuddies(p, buddyCapacity, topSection));
				//System.out.println("made a new buddy group: " + topSection);
				topSection = !topSection;
			}
		}
	}
	
	public void assignNodes() {
		ArrayList<Node> topNodes = new ArrayList<Node>();
		ArrayList<Node> botNodes = new ArrayList<Node>();
		ArrayList<Node> allNodes = NodeManager.getNodes();
		for(Node n : allNodes) {
			if(n.getCenterY() < 0) {
				topNodes.add(n);
			}else {
				botNodes.add(n);
			}
		}
		topNodes = sortNodes(topNodes);
		botNodes = sortNodes(botNodes);
		int topIndex = 0;
		int botIndex = 0;
		//for troubleshooting
//		if(!checkSorted(topNodes)) {
//			System.out.println("top not sorted!");
//		}
//		if(!checkSorted(botNodes)) {
//			System.out.println("bot not sorted!");
//		}
		for(MinerBuddies m : minerBuds) {
			if(minerBuds.size() == 0) break;
			if(m.getTarget() == null) 
			{
				if(m.getSectorAssignemnt() && topIndex < topNodes.size())
				{
					m.setTarget(topNodes.get(topIndex));
					topIndex++;
				}
				else if(botIndex < botNodes.size())
				{
					m.setTarget(botNodes.get(botIndex));
					botIndex++;
				}
			}
		}
		
	}
	
	public ArrayList<Node> sortNodes(ArrayList<Node> n)
	{
		ArrayList<Float> nearestDistances = new ArrayList<Float>(); //keep the distances, what we'll be sorting with
		ArrayList<Node> nearestNodes = n;
		
		for(Node nodes : n)
		{
			if(p.getMyBase().getDistance(nodes) > 10000 && n.size() > 20)
			{
				
			}
			else
			{
				nearestDistances.add(p.getMyBase().getDistance(nodes));
			}
		}
		
		//int steps = 0;
		
		Collections.sort(nearestDistances);
		
		for(int i = 0; i < nearestDistances.size(); i++)
		{
			for(int j = 0; j < nearestNodes.size(); j++)
			{
				//steps++;
				if(p.getMyBase().getDistance(nearestNodes.get(j)) == nearestDistances.get(i))
				{
					Node temp = nearestNodes.get(j);
					
					nearestNodes.set(j, nearestNodes.get(i));
					nearestNodes.set(i, temp);
					break;
				}
			}
		}
		
		return(nearestNodes);
	}
	
	//just for troubleshooting
	public boolean checkSorted(ArrayList<Node> nList) {
		float prevD = 0;
		
		for(int i = 0; i < nList.size(); i++) {
			if(nList.get(i).getDistance(p.getMyBase()) < prevD) {
				return false;
			}
			prevD = nList.get(i).getDistance(p.getMyBase());
		}
		return true;
	}
}
