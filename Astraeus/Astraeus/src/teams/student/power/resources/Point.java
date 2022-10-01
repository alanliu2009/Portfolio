package teams.student.power.resources;

public class Point //just for storing points
{
	private float x;
	private float y;
	
	public Point(float x, float y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX() { return x; }
	public float getY() { return y; }
	
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	
	public float getDistance(Point p)
	{
		float deltaX = p.getX() - this.getX();
		float deltaY = p.getY() - this.getY();
		
		return (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
}
